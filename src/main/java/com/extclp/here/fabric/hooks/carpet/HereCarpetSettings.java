package com.extclp.here.fabric.hooks.carpet;

import carpet.settings.Rule;
import carpet.settings.RuleCategory;

public class HereCarpetSettings{

    @Rule(desc = "Enable /here command", category = RuleCategory.COMMAND)
    public static String commandHere = "true";

    @Rule(desc = "Enable /glowing command", category = RuleCategory.COMMAND)
    public static String commandGlowing = "true";
}