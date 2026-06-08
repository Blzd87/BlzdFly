package com.blzd.fly;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class FlyTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args) {

        List<String> completions = new ArrayList<>();

        if (args.length == 1) {

            if (sender.hasPermission("blzdfly.admin")) {

                completions.add("give");
                completions.add("remove");
                completions.add("set");
                completions.add("time");
                completions.add("reload");
            }

            completions.add("info");
            completions.add("claimtest");
            completions.add("ownercheck");
            completions.add("claiminfo");

            return completions;
        }

        if (args.length == 2) {

            if (args[0].equalsIgnoreCase("give")
                    || args[0].equalsIgnoreCase("remove")
                    || args[0].equalsIgnoreCase("set")
                    || args[0].equalsIgnoreCase("time")) {

                Bukkit.getOnlinePlayers()
                        .forEach(player ->
                                completions.add(player.getName()));
            }
        }

        if (args.length == 3) {

            if (args[0].equalsIgnoreCase("give")
                    || args[0].equalsIgnoreCase("remove")
                    || args[0].equalsIgnoreCase("set")) {

                completions.add("1m");
                completions.add("5m");
                completions.add("10m");
                completions.add("30m");
                completions.add("1h");
                completions.add("12h");
                completions.add("24h");
            }
        }

        return completions;
    }
}
