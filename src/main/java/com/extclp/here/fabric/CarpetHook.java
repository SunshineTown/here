package com.extclp.here.fabric;

import carpet.CarpetServer;
import carpet.settings.Rule;
import carpet.settings.RuleCategory;
import carpet.settings.SettingsManager;
import net.minecraft.server.command.ServerCommandSource;

public class CarpetHook {

    private static class Settings{

        @Rule(desc = "/here command permission", category = RuleCategory.COMMAND)
        public static String commandHere = "true";

        @Rule(desc = "/glowing command permission", category = RuleCategory.COMMAND)
        public static String commandGlowing = "true";
    }

    private static boolean hooked;

    public static void hookCarpet(){
        try {
            CarpetServer.settingsManager.parseSettingsClass(CarpetHook.class);
            hooked = true;
        }catch (Throwable ignore){}
    }

    public static boolean canUseHereCommand(ServerCommandSource source){
        if(hooked){
            return SettingsManager.canUseCommand(source, Settings.commandHere);
        }else {
            return true;
        }
    }

    public static boolean canUseGlowingCommand(ServerCommandSource source){
        if(hooked){
            return SettingsManager.canUseCommand(source, Settings.commandGlowing);
        }else {
            return true;
        }
    }
}
