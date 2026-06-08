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

            var input = player.getCurrentInput();

            boolean moving =
                    input.isForward()
                    || input.isBackward()
                    || input.isLeft()
                    || input.isRight();

            if (!moving) {

                Vector velocity = player.getVelocity();

                velocity.setX(0);
                velocity.setZ(0);

                player.setVelocity(velocity);
            }
        }
    }
}
