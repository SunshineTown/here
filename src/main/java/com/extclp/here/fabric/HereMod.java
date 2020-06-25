package com.extclp.here.fabric;

import com.extclp.here.fabric.hooks.carpet.CarpetHook;
import com.extclp.here.fabric.hooks.viaversion.ViaVersionHook;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HereMod implements ModInitializer {

    public static HereConfig config;

    @Override
    public void onInitialize() {
        if (setupConfig()) {
            CommandRegistrationCallback.EVENT.register(HereMod::registerCommand);
            CarpetHook.hookCarpet();
            ViaVersionHook.hookViaVersion();
        }
    }

    private static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        HereCommand.register(dispatcher);
    }

    private static boolean setupConfig() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            Path configPath = Paths.get("config", "here.json");
            if (Files.exists(configPath)) {
                try (Reader reader = Files.newBufferedReader(configPath)) {
                    config = gson.fromJson(reader, HereConfig.class);
                }
            } else {
                Files.createDirectories(configPath.getParent());
                config = new HereConfig();
            }
            try (Writer writer = Files.newBufferedWriter(configPath)) {
                writer.write(gson.toJson(config));
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
