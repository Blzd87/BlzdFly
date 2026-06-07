package com.blzd.fly;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerDataManager {

    private final BlzdFly plugin;

    private File file;
    private FileConfiguration config;

    public PlayerDataManager(BlzdFly plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {

        file = new File(plugin.getDataFolder(), "players.yml");

        if (!file.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public void reload() {
        load();
    }

    public long getFlightSeconds(UUID uuid) {
        return config.getLong("players." + uuid + ".flight-seconds", 0);
    }

    public void setFlightSeconds(UUID uuid, long seconds) {
        config.set("players." + uuid + ".flight-seconds", seconds);
        save();
    }

    public void addFlightSeconds(UUID uuid, long seconds) {
        setFlightSeconds(uuid, getFlightSeconds(uuid) + seconds);
    }

    public void removeFlightSeconds(UUID uuid, long seconds) {

        long remaining = getFlightSeconds(uuid) - seconds;

        if (remaining < 0) {
            remaining = 0;
        }

        setFlightSeconds(uuid, remaining);
    }

    private void save() {

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
