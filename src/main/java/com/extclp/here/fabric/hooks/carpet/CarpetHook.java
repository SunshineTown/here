package com.extclp.here.fabric.hooks.carpet;

import carpet.CarpetServer;
import carpet.settings.SettingsManager;
import net.minecraft.server.command.ServerCommandSource;

public class CarpetHook {

    private static boolean hooked;

    public static void hookCarpet(){
        try {
            CarpetServer.extensions.add(new HereCarpetExtension());
            hooked = true;
        }catch (Throwable ignored){
            // ignored
        }
    }

    public static boolean canUseHereCommand(ServerCommandSource source){
        if(hooked){
            return SettingsManager.canUseCommand(source, HereCarpetSettings.commandHere);
        }else {
            return true;
        }
    }

    public static boolean canUseGlowingCommand(ServerCommandSource source){
        if(hooked){
            return SettingsManager.canUseCommand(source, HereCarpetSettings.commandGlowing);
        }else {
            return true;
        }
    }
}
