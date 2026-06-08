package com.blzd.fly;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClaimFlightTask extends BukkitRunnable {

    private final BlzdFly plugin;
    private final ClaimManager claimManager = new ClaimManager();

    private final Map<UUID, Integer> exitTimers = new HashMap<>();

    public ClaimFlightTask(BlzdFly plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

        for (Player player : Bukkit.getOnlinePlayers()) {

            if (!plugin.isFlightEnabled(player.getUniqueId())) {
                continue;
            }

            UUID uuid = player.getUniqueId();

            if (plugin.getFlightMode(uuid) == FlightMode.TIMED) {
            
                if (player.hasPermission("blzdfly.claim")
                        && claimManager.canUseClaimFlight(player)) {
            
                    plugin.setFlightMode(
                            uuid,
                            FlightMode.CLAIM
                    );
            
                    player.sendMessage(
                            "§aSwitched to Claim Flight."
                    );
                }
            
                continue;
            }

            if (plugin.getFlightMode(player.getUniqueId())
                    != FlightMode.CLAIM) {
                continue;
            }

            if (claimManager.canUseClaimFlight(player)) {

                if (exitTimers.containsKey(uuid)) {

                    exitTimers.remove(uuid);

                    player.sendMessage(
                            "§aClaim Flight restored."
                    );
                }

                continue;
            }

            if (!exitTimers.containsKey(uuid)) {

                exitTimers.put(uuid, 5);

                player.sendMessage(
                        "§eYou have left your claim."
                );

                player.sendMessage(
                        "§eClaim Flight will end in 5 seconds."
                );

                continue;
            }

            int timeLeft = exitTimers.get(uuid) - 1;

            if (timeLeft <= 0) {

                exitTimers.remove(uuid);
            
                long remaining = plugin.getPlayerDataManager()
                        .getFlightSeconds(uuid);
            
                if (player.hasPermission("blzdfly.timed")
                        && remaining > 0) {
            
                    plugin.setFlightMode(
                            uuid,
                            FlightMode.TIMED
                    );
            
                    player.sendMessage(
                            "§eSwitched to Timed Flight."
                    );
            
                    continue;
                }
            
                disableClaimFlight(player);
            
                player.sendTitle(
                        "§cClaim Flight Expired",
                        "§7You have been safely landed.",
                        10,
                        40,
                        10
                );
            
                player.sendMessage(
                        "§cClaim Flight expired."
                );
            
                continue;
            }

            exitTimers.put(uuid, timeLeft);
        }
    }

    private void disableClaimFlight(Player player) {

        plugin.disableFlight(player.getUniqueId());

        player.setFlying(false);
        player.setAllowFlight(false);

        Location safe = player.getLocation().clone();

        for (int y = safe.getBlockY();
             y > safe.getWorld().getMinHeight();
             y--) {

            safe.setY(y);

            if (!safe.getBlock().isPassable()) {

                safe.setY(y + 1);

                player.teleport(safe);

                break;
            }
        }
    }
}
