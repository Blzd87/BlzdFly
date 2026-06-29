package com.blzd.fly;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClaimFlightTask extends BukkitRunnable {

    private final BlzdFly plugin;
    private final ClaimManager claimManager = new ClaimManager();

    private final Map<UUID, Boolean> wasInClaim = new HashMap<>();
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

            if (!player.getAllowFlight()) {
                player.setAllowFlight(true);
            }

            UUID uuid = player.getUniqueId();

            boolean inClaim = claimManager.canUseClaimFlight(player);
            boolean previouslyInClaim = wasInClaim.getOrDefault(uuid, inClaim);
            
            wasInClaim.put(uuid, inClaim);

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

            if (plugin.getFlightMode(uuid)
                    != FlightMode.CLAIM) {
                continue;
            }

            if (inClaim) {

                if (exitTimers.containsKey(uuid)) {

                    exitTimers.remove(uuid);

                    player.sendMessage(
                            "§aClaim Flight restored."
                    );

                    player.sendActionBar(
                            net.kyori.adventure.text.Component.empty()
                    );
                }

                continue;
            }

            if (!exitTimers.containsKey(uuid)) {

                // Player didn't just leave a claim
                if (!previouslyInClaim) {
                    continue;
                }
            
                // Walking on the ground?
                if (player.isOnGround()) {
            
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
            
                    } else {
            
                        disableClaimFlight(player);
            
                        player.sendMessage(
                                "§cClaim Flight disabled."
                        );
                    }
            
                    continue;
                }
            
                // Flying -> start grace period
                exitTimers.put(
                        uuid,
                        plugin.getConfig().getInt(
                                "claim-flight.grace-seconds",
                                5
                        )
                );

                player.sendMessage(
                        "§eYou have left your claim."
                );

                int graceSeconds = plugin.getConfig().getInt(
                        "claim-flight.grace-seconds",
                        5
                );
                
                player.sendMessage(
                        "§eClaim Flight will end in "
                                + graceSeconds
                                + " seconds."
                );

                continue;
            }

            int timeLeft = exitTimers.get(uuid) - 1;

            player.sendActionBar(
                    net.kyori.adventure.text.Component.text(
                            "⚠ Leaving Claim: " + timeLeft + "s"
                    )
            );

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

                    player.sendActionBar(
                            net.kyori.adventure.text.Component.empty()
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

        UUID uuid = player.getUniqueId();
    
        exitTimers.remove(uuid);
        wasInClaim.remove(uuid);
    
        plugin.disableFlight(uuid);
    
        SafeLandingUtil.safeLand(player);
    }
}
