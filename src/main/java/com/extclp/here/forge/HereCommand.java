package com.extclp.here.forge;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

public class HereCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher)  {
        dispatcher.register(Commands.literal("here")
                .requires(source -> source.getEntity() instanceof ServerPlayerEntity)
                .executes(context -> glowing(context.getSource())));
        dispatcher.register(Commands.literal("glowing")
                .executes(context -> glowing(context.getSource().asPlayer()))
                .then(Commands.literal("clear")
                        .executes(context -> clearGlowing(context.getSource().asPlayer())))
                .then(Commands.argument("glowing_time", IntegerArgumentType.integer(1, 86400))
                        .executes(context -> glowing(context.getSource().asPlayer(),
                                IntegerArgumentType.getInteger(context, "glowing_time"))))
        );
    }

        private static String getWorldDisplayName(ServerWorld world){
        int dimensionID = world.getDimension().getType().getId();
        String displayName = HereMod.config.dimensions_name.get(dimensionID);
        if(displayName != null){
            return displayName;
        }else {
            return Integer.toString(dimensionID);
        }
    }

    private static int glowing(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        BlockPos blockPos = player.getPosition();
        source.getServer().getPlayerList().sendMessage(
                new TranslationTextComponent(HereMod.config.broadcast_message, player.getDisplayName(),
                        getWorldDisplayName(source.getWorld()),
                        blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                        player.getServerWorld().getDimension().getType().getId()
                ), true);
        return glowing(player);
    }

    private static int glowing(ServerPlayerEntity player) {
        return glowing(player, HereMod.config.glowing_time);
    }

    private static int glowing(ServerPlayerEntity player, int glowing_time){
        if(glowing_time > 0){
            player.addPotionEffect(new EffectInstance(Effects.GLOWING, glowing_time * 20));
            player.sendMessage(new TranslationTextComponent(HereMod.config.glowing_message, glowing_time));
        }
        return 1;
    }

    private static int clearGlowing(ServerPlayerEntity player){
        player.removePotionEffect(Effects.GLOWING);
        player.sendMessage(new TranslationTextComponent(HereMod.config.glowing_effect_removed_message));
        return 1;
    }
}
