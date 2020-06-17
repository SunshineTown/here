package com.extclp.here.fabric;

import com.extclp.here.fabric.hooks.carpet.CarpetHook;
import com.extclp.here.fabric.utils.Texts;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

public class HereCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher)  {
        dispatcher.register(CommandManager.literal("here")
                .requires(source -> source.getEntity() instanceof ServerPlayerEntity &&
                        CarpetHook.canUseHereCommand(source))
                .executes(context -> glowing(context.getSource())));
        dispatcher.register(CommandManager.literal("glowing")
                .requires(CarpetHook::canUseGlowingCommand)
                .executes(context -> glowing(context.getSource().getPlayer()))
                .then(CommandManager.literal("clear")
                        .executes(context -> clearGlowing(context.getSource().getPlayer())))
                .then(CommandManager.argument("glowing_time", IntegerArgumentType.integer(1, 86400))
                        .executes(context -> glowing(context.getSource().getPlayer(),
                                IntegerArgumentType.getInteger(context, "glowing_time"))))
        );
    }

    private static String getWorldDisplayName(ServerWorld world){
        int dimensionID = world.getDimension().getType().getRawId();
        String displayName = HereMod.config.dimensions_name.get(dimensionID);
        if(displayName != null){
            return displayName;
        }else {
            return Integer.toString(dimensionID);
        }
    }

    private static int glowing(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();
        BlockPos blockPos = player.getBlockPos();
        source.getMinecraftServer().getPlayerManager().broadcastChatMessage(
                Texts.of(HereMod.config.broadcast_message, player.getDisplayName(),
                        getWorldDisplayName(source.getWorld()),
                        blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                        player.getServerWorld().getDimension().getType().getRawId()
                ), true);
        return glowing(player);
    }

    private static int glowing(ServerPlayerEntity player) {
        return glowing(player, HereMod.config.glowing_time);
    }

    private static int glowing(ServerPlayerEntity player, int glowing_time){
        if(glowing_time > 0){
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, glowing_time * 20));
            player.sendMessage(Texts.of(HereMod.config.glowing_message, glowing_time));
        }
        return 1;
    }

    private static int clearGlowing(ServerPlayerEntity player){
        player.removeStatusEffect(StatusEffects.GLOWING);
        player.sendMessage(new LiteralText(HereMod.config.glowing_effect_removed_message).styled(style -> style.setColor(Formatting.DARK_GREEN)));
        return 1;
    }
}
