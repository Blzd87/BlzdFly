package com.blzd.fly;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BlzdFly extends JavaPlugin {

    private PlayerDataManager playerDataManager;

    private final Map<UUID, FlightMode> activeFlight = new HashMap<>();

    @Override
    public void onEnable() {

        saveDefaultConfig();

        playerDataManager = new PlayerDataManager(this);

        if (getCommand("fly") != null) {

            getCommand("fly").setExecutor(
                    new FlyCommand(this)
            );
        
            getCommand("fly").setTabCompleter(
                    new FlyTabCompleter()
            );
        }

        new FlightTask(this).runTaskTimer(this, 20L, 20L);

        new ClaimFlightTask(this).runTaskTimer(this, 20L, 20L);

        new BedrockFlightTask(this).runTaskTimer(this, 1L, 1L);

        if (getServer().getPluginManager().getPlugin("GriefDefender") != null) {
            getLogger().info("GriefDefender detected.");
        } else {
            getLogger().info("GriefDefender not found.");
        }
        
        getLogger().info("BlzdFly enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("BlzdFly disabled!");
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public boolean isFlightEnabled(UUID uuid) {
    return activeFlight.containsKey(uuid);
    }
    
    public FlightMode getFlightMode(UUID uuid) {
        return activeFlight.get(uuid);
    }
    
    public void setFlightMode(UUID uuid, FlightMode mode) {
        activeFlight.put(uuid, mode);
    }
    
    public void disableFlight(UUID uuid) {
        activeFlight.remove(uuid);
    }
}
