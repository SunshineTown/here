package com.extclp.here.bukkit;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.List;
import java.util.Map;

public final class HerePlugin extends JavaPlugin implements Listener {

    private List<String> chat_event;

    private String broadcast_message;

    private Map<String, String> worlds_name = Maps.newHashMap();

    private int glowing_time;

    private String glowing_message;

    private String glowing_effect_removed_message;

    @Override
    public void onEnable() {
        YamlConfiguration defaultConfig = new YamlConfiguration();
        defaultConfig.set("chat_event", Lists.newArrayList("！！here", "！here", "!!here", "!here"));
        defaultConfig.set("worlds_name", Maps.newHashMap(ImmutableMap.of(
                "world", "&2主世界", "world_nether", "&c地狱", "world_end", "&d末地")));
        defaultConfig.set("broadcast_message", "%s &r在 %s &b[x:%s, y:%s, z:%s, dim:%s] &r向各位打招呼");
        defaultConfig.set("glowing_time", 15);
        defaultConfig.set("glowing_message", "你将会被高亮%s秒");
        defaultConfig.set("glowing_effect_removed_message", "已移除你的高亮效果");
        File configFile = new File(getDataFolder(), "config.yml");
        YamlConfiguration config;
        if(configFile.exists()){
            config = YamlConfiguration.loadConfiguration(configFile);
        }else {
            try {
                defaultConfig.save(configFile);
            }catch (Exception e){
                e.printStackTrace();
                setEnabled(false);
                return;
            }
            config = new YamlConfiguration();
        }
        config.setDefaults(defaultConfig);
        chat_event = getConfig().getStringList("chat_event");

        ConfigurationSection worldsNameConfiguration = getConfig().getConfigurationSection("worlds_name");
        for (String worldName : worldsNameConfiguration.getKeys(false)) {
            worlds_name.put(worldName,color(worldsNameConfiguration.getString(worldName)));
        }
        broadcast_message = color(config.getString("broadcast_message"));
        glowing_time = config.getInt("glowing_time");
        glowing_message = color(config.getString("glowing_message"));
        glowing_effect_removed_message = color(config.getString("glowing_effect_removed_message"));
        getServer().getPluginManager().registerEvents(this, this);
    }

    private String color(String str){
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event){
        if (chat_event.contains(event.getMessage())) {
            getServer().getScheduler().runTask(this, ()-> here(event.getPlayer()));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof  Player)){
            sender.sendMessage(ChatColor.RED + "Only Player");
        } else if(command.getName().equalsIgnoreCase("here")){
            here((Player) sender);
        }else if(command.getName().equalsIgnoreCase("glowing")){
            if(args.length == 0){
                glowing(((Player) sender));
            } else if(args.length == 1){
                if(args[0].equalsIgnoreCase("clear")){
                    ((Player) sender).removePotionEffect(PotionEffectType.GLOWING);
                    sender.sendMessage(glowing_effect_removed_message);
                } else {
                    int glowingTime;
                    try {
                        glowingTime = Integer.parseInt(args[0]);
                    }catch (Exception e){
                        sender.sendMessage(ChatColor.RED + "高亮时间必须是整数");
                        return false;
                    }
                    glowing(((Player) sender), glowingTime);
                }
            }
        }
        return true;
    }

    private void here(Player player){
        String worldName = worlds_name.get(player.getWorld().getName());
        if(worldName == null){
            worldName = player.getWorld().getName();
        }
        String broadcastMessage = String.format(broadcast_message, player.getDisplayName(), worldName,
                player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ(),
                player.getWorld().getEnvironment().getId());
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(broadcastMessage);
        }
        Bukkit.getConsoleSender().sendMessage(broadcastMessage);
        glowing(player);
    }

    private void glowing(Player player){
        glowing(player, glowing_time);
    }

    private void glowing(Player player, int glowingTime){
        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,glowingTime * 20 , 1));
        player.sendMessage(String.format(glowing_message, glowingTime));
    }
}
