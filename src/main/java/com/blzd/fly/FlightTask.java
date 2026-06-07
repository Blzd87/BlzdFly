package com.blzd.fly;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FlightTask extends BukkitRunnable {

    private final BlzdFly plugin;

    public FlightTask(BlzdFly plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

        for (Player player : Bukkit.getOnlinePlayers()) {

            if (!player.hasPermission("blzdfly.timed")) {
                continue;
            }

            if (!plugin.isFlightEnabled(player.getUniqueId())) {
                continue;
            }

            long remaining = plugin.getPlayerDataManager()
                    .getFlightSeconds(player.getUniqueId());

            if (!player.isFlying()) {
                continue;
            }

            if (player.isOnGround()) {
                continue;
            }

            remaining--;

            if (remaining <= 0) {

                plugin.getPlayerDataManager()
                        .setFlightSeconds(player.getUniqueId(), 0);
            
                disableFlight(player);
            
                player.sendTitle(
                        "§cFlight Expired",
                        "§7You have been safely landed.",
                        10,
                        40,
                        10
                );
            
                player.sendMessage("§cYour flight time has expired.");
            
                continue;
            }

            plugin.getPlayerDataManager()
                    .setFlightSeconds(player.getUniqueId(), remaining);

            player.sendActionBar(
                    Component.text("✈ Flight Time: " + TimeUtil.formatTime(remaining))
            );
        }
    }

    private void disableFlight(Player player) {

        plugin.setFlightEnabled(player.getUniqueId(), false);

        player.setFlying(false);
        player.setAllowFlight(false);

        Location safe = player.getLocation().clone();

        for (int y = safe.getBlockY(); y > safe.getWorld().getMinHeight(); y--) {

            safe.setY(y);

            if (!safe.getBlock().isPassable()) {

                safe.setY(y + 1);

                player.teleport(safe);

                break;
            }
        }
    }
}
