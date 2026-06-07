package com.blzd.template;

import org.bukkit.plugin.java.JavaPlugin;

public class BlzdPluginTemplate extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Blzd Plugin Template enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Blzd Plugin Template disabled!");
    }
}
