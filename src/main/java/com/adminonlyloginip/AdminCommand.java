package com.adminonlyloginip;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminCommand implements CommandExecutor, TabCompleter {

    private final AdminOnlyLoginIP plugin;

    public AdminCommand(AdminOnlyLoginIP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
            return true;
        }

        String sub = args[0].toLowerCase();

        if (sub.equals("add")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(ChatColor.RED + "Only players can use this.");
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /AdminOnlyLoginIP add <nickname>");
                return true;
            }
            String targetNick = args[1];
            String ip = player.getAddress().getAddress().getHostAddress();
            plugin.getStore().add(player.getUniqueId(), targetNick, ip);
            sender.sendMessage(ChatColor.GREEN + "Saved: " + targetNick + " | " + player.getName() + " | " + ip);
            return true;
        }

        if (sub.equals("delete") || sub.equals("remove") || sub.equals("del")) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /AdminOnlyLoginIP delete <nickname>");
                return true;
            }
            String targetNick = args[1];
            if (plugin.getStore().delete(targetNick)) {
                sender.sendMessage(ChatColor.GREEN + "Deleted entry for: " + targetNick);
            } else {
                sender.sendMessage(ChatColor.RED + "No entry found for: " + targetNick);
            }
            return true;
        }

        sendUsage(sender);
        return true;
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY + "AdminOnlyLoginIP v1.0");
        sender.sendMessage(ChatColor.YELLOW + "/AdminOnlyLoginIP add <nick>" + ChatColor.GRAY + " - save your UUID+IP");
        sender.sendMessage(ChatColor.YELLOW + "/AdminOnlyLoginIP delete <nick>" + ChatColor.GRAY + " - remove by nickname");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            if ("add".startsWith(args[0].toLowerCase())) completions.add("add");
            if ("delete".startsWith(args[0].toLowerCase()) || "del".startsWith(args[0].toLowerCase())) completions.add("delete");
            return completions;
        }
        if (args.length == 2 && (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("del"))) {
            String prefix = args[1].toLowerCase();
            return plugin.getStore().getAll().stream()
                    .map(e -> e.nickname())
                    .filter(n -> n.toLowerCase().startsWith(prefix))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}
