package com.extclp.here.fabric;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class HereConfig {

    public List<String> chat_events =  Lists.newArrayList("!here", "!!here", "！here", "！！here");

    public Map<String, String> worlds_name = Maps.newHashMap(ImmutableMap.of(
            "minecraft:overworld", "§2主世界", "minecraft:the_nether", "§c地狱", "minecraft:the_end", "§d末地"));

    public Map<String, Integer> dimensions_id = Maps.newHashMap(ImmutableMap.of(
            "minecraft:overworld", 0, "minecraft:the_nether", -1, "minecraft:the_end", 1));

    public String broadcast_message = "%s §r在 %s §b[x:%s, y:%s, z:%s, dim:%s] §r向各位打招呼";

    public String glowing_message = "你将会被高亮%s秒";

    public int glowing_time = 15;

    public String glowing_effect_removed_message = "§8已移除你的高亮效果";
}
