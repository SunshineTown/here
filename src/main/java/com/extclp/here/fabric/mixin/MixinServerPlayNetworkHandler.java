package com.extclp.here.fabric.mixin;

import com.extclp.here.fabric.HereMod;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class MixinServerPlayNetworkHandler {

    @Shadow public ServerPlayerEntity player;

    @Shadow @Final private MinecraftServer server;

    @Inject(method = "onChatMessage",
            at = @At(value = "INVOKE",shift = At.Shift.AFTER,
                    target = "Lnet/minecraft/server/PlayerManager;broadcastChatMessage(Lnet/minecraft/text/Text;Z)V"))
    private void onChat(ChatMessageC2SPacket packet, CallbackInfo ci){
        if(HereMod.config.chat_events.contains(packet.getChatMessage())){
            server.getCommandManager().execute(player.getCommandSource(), "/here");
        }
    }
}
