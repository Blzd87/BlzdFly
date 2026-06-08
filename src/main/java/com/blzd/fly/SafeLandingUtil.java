package com.blzd.fly;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SafeLandingUtil {

public static void safeLand(Player player) {

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
