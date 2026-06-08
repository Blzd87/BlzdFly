package com.blzd.fly;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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

            if (plugin.getFlightMode(player.getUniqueId())
                    != FlightMode.CLAIM) {
                continue;
            }

            if (!claimManager.canUseClaimFlight(player)) {

                player.sendMessage(
                        "§cDEBUG: Left valid claim."
                );
            }
        }
    }
}
