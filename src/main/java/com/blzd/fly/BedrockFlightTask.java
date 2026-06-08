package com.blzd.fly;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class BedrockFlightTask extends BukkitRunnable {

    private final BlzdFly plugin;

    public BedrockFlightTask(BlzdFly plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
    
        if (!plugin.getConfig().getBoolean("flight.bedrock-flight")) {
            return;
        }
    
        for (Player player : Bukkit.getOnlinePlayers()) {
    
            if (!player.isFlying()) {
                continue;
            }
    
            if (!plugin.isFlightEnabled(player.getUniqueId())) {
                continue;
            }
    
            var velocity = player.getVelocity();
    
            if (Math.abs(velocity.getX()) < 0.001
                    && Math.abs(velocity.getZ()) < 0.001) {
            
                player.setVelocity(
                        new Vector(
                                0,
                                velocity.getY(),
                                0
                        )
                );
            }
        }
    }
}
