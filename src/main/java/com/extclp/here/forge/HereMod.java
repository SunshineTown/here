package com.extclp.here.forge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Mod("here")
public class HereMod {

    public static HereConfig config;

    public HereMod() throws IOException {
        setupConfig();
        MinecraftForge.EVENT_BUS.addListener(HereMod::onServerStart);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void onServerStart(FMLServerStartingEvent event){
        HereCommand.register(event.getCommandDispatcher());
    }

    @SubscribeEvent
    public void onChat(ServerChatEvent event) {
        if(HereMod.config.chat_events.contains(event.getMessage())){
            event.getPlayer().server.getCommandManager().handleCommand(event.getPlayer().getCommandSource(), "/here");
        }
    }

    private static void setupConfig() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
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
    }
}
