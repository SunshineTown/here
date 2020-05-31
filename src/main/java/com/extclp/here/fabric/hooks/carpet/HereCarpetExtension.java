package com.extclp.here.fabric.hooks.carpet;

import carpet.CarpetExtension;
import carpet.CarpetServer;

public class HereCarpetExtension implements CarpetExtension {
    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(HereCarpetSettings.class);
    }
}
