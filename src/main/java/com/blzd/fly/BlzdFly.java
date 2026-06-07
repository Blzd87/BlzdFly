package com.blzd.fly;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BlzdFly extends JavaPlugin {

    private PlayerDataManager playerDataManager;

    private final Set<UUID> activeFlight = new HashSet<>();

    @Override
    public void onEnable() {

        saveDefaultConfig();

        playerDataManager = new PlayerDataManager(this);

        if (getCommand("fly") != null) {
            getCommand("fly").setExecutor(new FlyCommand(this));
        }

        new FlightTask(this).runTaskTimer(this, 20L, 20L);

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
        return activeFlight.contains(uuid);
    }

    public void setFlightEnabled(UUID uuid, boolean enabled) {

        if (enabled) {
            activeFlight.add(uuid);
        } else {
            activeFlight.remove(uuid);
        }
    }
}
