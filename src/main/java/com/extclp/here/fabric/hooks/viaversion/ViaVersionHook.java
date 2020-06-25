package com.extclp.here.fabric.hooks.viaversion;

import net.minecraft.server.network.ServerPlayerEntity;
import us.myles.ViaVersion.api.Via;

public class ViaVersionHook {

    private static boolean hooked;

    public static void hookViaVersion(){
        try {
            Class.forName("us.myles.ViaVersion.api.Via");
            hooked = true;
        }catch (Throwable ignored){
            // ignored
        }
    }

    public static boolean is1_15AboveVersion(ServerPlayerEntity player){
        if(hooked){
            try {
                return Via.getAPI().getPlayerVersion(player.getUuid()) > 578;
            }catch (Throwable ignored){
                return true;
            }
        } else {
            return true;
        }
    }
}
