package com.blzd.fly;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

    private final BlzdFly plugin;

    public FlyCommand(BlzdFly plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {

        if (args.length == 0) {

            if (!(sender instanceof Player player)) {
                sender.sendMessage("Players only.");
                return true;
            }

            if (player.hasPermission("blzdfly.permanent")) {

                if (player.getAllowFlight()) {
            
                    player.setFlying(false);
                    player.setAllowFlight(false);
            
                    player.sendMessage("§cFlight disabled.");
            
                } else {
            
                    player.setAllowFlight(true);
            
                    player.sendMessage("§aFlight enabled.");
                }
            
                return true;
            }
            
            if (!player.hasPermission("blzdfly.timed")) {
                player.sendMessage("§cYou do not have permission.");
                return true;
            }
            
            long remaining = plugin.getPlayerDataManager()
                    .getFlightSeconds(player.getUniqueId());
            
            if (remaining <= 0) {
                player.sendMessage("§cYou have no flight time remaining.");
                return true;
            }
            
            boolean enabled =
                    plugin.isFlightEnabled(player.getUniqueId());
            
            if (enabled) {
            
                plugin.setFlightEnabled(player.getUniqueId(), false);
            
                player.setFlying(false);
                player.setAllowFlight(false);
            
                player.sendMessage("§cFlight disabled.");
            
            } else {
            
                plugin.setFlightEnabled(player.getUniqueId(), true);
            
                player.setAllowFlight(true);
            
                player.sendMessage("§aFlight enabled.");
            }
            
            return true;
        }

        if (args.length == 1
                && args[0].equalsIgnoreCase("reload")) {

            if (!sender.hasPermission("blzdfly.admin")) {
                sender.sendMessage("§cNo permission.");
                return true;
            }

            plugin.reloadConfig();
            plugin.getPlayerDataManager().reload();

            sender.sendMessage("§aBlzdFly reloaded.");

            return true;
        }

        if (args.length == 3
                && sender.hasPermission("blzdfly.admin")) {

            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                sender.sendMessage("§cPlayer not found.");
                return true;
            }

            long seconds = TimeUtil.parseTime(args[2]);

            if (seconds < 0) {
                sender.sendMessage("§cInvalid time.");
                return true;
            }

            switch (args[0].toLowerCase()) {

                case "give" -> {

                    plugin.getPlayerDataManager()
                            .addFlightSeconds(
                                    target.getUniqueId(),
                                    seconds
                            );

                    sender.sendMessage(
                            "§aAdded "
                                    + TimeUtil.formatTime(seconds)
                                    + " to "
                                    + target.getName()
                    );
                }

                case "remove" -> {

                    plugin.getPlayerDataManager()
                            .removeFlightSeconds(
                                    target.getUniqueId(),
                                    seconds
                            );

                    sender.sendMessage(
                            "§aRemoved "
                                    + TimeUtil.formatTime(seconds)
                                    + " from "
                                    + target.getName()
                    );
                }

                case "set" -> {

                    plugin.getPlayerDataManager()
                            .setFlightSeconds(
                                    target.getUniqueId(),
                                    seconds
                            );

                    sender.sendMessage(
                            "§aSet "
                                    + target.getName()
                                    + " to "
                                    + TimeUtil.formatTime(seconds)
                    );
                }

                default -> {
                    return false;
                }
            }

            return true;
        }

        if (args.length == 2
                && args[0].equalsIgnoreCase("time")
                && sender.hasPermission("blzdfly.admin")) {

            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                sender.sendMessage("§cPlayer not found.");
                return true;
            }

            long remaining =
                    plugin.getPlayerDataManager()
                            .getFlightSeconds(target.getUniqueId());

            sender.sendMessage(
                    "§a"
                            + target.getName()
                            + " has "
                            + TimeUtil.formatTime(remaining)
                            + " remaining."
            );

            return true;
        }

        return false;
    }
}
