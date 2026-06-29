package com.blzd.fly;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class GameModeListener implements Listener {

    private final BlzdFly plugin;

    public GameModeListener(BlzdFly plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
    
        if (!event.getNewGameMode().name().equals("SURVIVAL")
                && !event.getNewGameMode().name().equals("ADVENTURE")) {
            return;
        }
    
        Player player = event.getPlayer();
    
        Bukkit.getScheduler().runTask(plugin, () -> {
    
            if (!plugin.isFlightEnabled(player.getUniqueId())) {
                return;
            }
    
            player.setAllowFlight(true);
    
            if (!player.isOnGround()) {
                player.setFlying(true);
            }
        });
    }
}
