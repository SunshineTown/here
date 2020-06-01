package com.extclp.here.forge;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class HereConfig {

    public List<String> chat_events =  Lists.newArrayList("!here", "!!here", "！here", "！！here");

    public Map<Integer, String> dimensions_name = Maps.newHashMap(ImmutableMap.of(
            0, "§2主世界", -1, "§c地狱", 1, "§d末地"));

    public String broadcast_message = "%s §r在 %s §b[x:%s, y:%s, z:%s, dim:%s] §r向各位打招呼";

    public String glowing_message = "你将会被高亮%s秒";

    public int glowing_time = 15;

    public String glowing_effect_removed_message = "已移除你的高亮效果";
}
