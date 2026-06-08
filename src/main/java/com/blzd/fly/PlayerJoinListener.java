package com.blzd.fly;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

private final BlzdFly plugin;

public PlayerJoinListener(BlzdFly plugin) {
    this.plugin = plugin;
}

@EventHandler
public void onJoin(PlayerJoinEvent event) {

    Player player = event.getPlayer();

    if (!plugin.isFlightEnabled(player.getUniqueId())
            && player.getAllowFlight()) {

        SafeLandingUtil.safeLand(player);

        player.sendMessage(
                "§eYour previous flight session ended due to a server restart."
        );
    }
}

}
