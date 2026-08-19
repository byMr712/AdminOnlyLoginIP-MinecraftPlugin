package com.adminonlyloginip;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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

        if (!sender.hasPermission("adminonlyloginip.admin")) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        }

        String sub = args[0].toLowerCase();

        if (sub.equals("add")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(ChatColor.RED + "Only players can use this.");
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /aoli add <nickname>");
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
                sender.sendMessage(ChatColor.RED + "Usage: /aoli delete <nickname>");
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

        if (sub.equals("on")) {
            plugin.setData("enabled", true);
            sender.sendMessage(ChatColor.GREEN + "AdminOnlyLoginIP enabled.");
            return true;
        }

        if (sub.equals("off")) {
            plugin.setData("enabled", false);
            sender.sendMessage(ChatColor.YELLOW + "AdminOnlyLoginIP disabled.");
            return true;
        }

        if (sub.equals("reload")) {
            plugin.reloadData();
            sender.sendMessage(ChatColor.GREEN + "Config reloaded. " + plugin.getStore().getAll().size() + " entries loaded.");
            return true;
        }

        if (sub.equals("list")) {
            var entries = plugin.getStore().getAll();
            if (entries.isEmpty()) {
                sender.sendMessage(ChatColor.GRAY + "No entries.");
            } else {
                for (AdminStore.StoredEntry e : entries) {
                    sender.sendMessage(ChatColor.YELLOW + e.nickname() + ChatColor.GRAY + " | UUID: " + e.uuid() + " | IP: " + e.ip());
                }
            }
            return true;
        }

        sendUsage(sender);
        return true;
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY + "AdminOnlyLoginIP v1.0");
        sender.sendMessage(ChatColor.YELLOW + "/aoli add <nick>" + ChatColor.GRAY + " - save your UUID+IP");
        sender.sendMessage(ChatColor.YELLOW + "/aoli delete <nick>" + ChatColor.GRAY + " - remove by nickname");
        sender.sendMessage(ChatColor.YELLOW + "/aoli on" + ChatColor.GRAY + " - enable IP check");
        sender.sendMessage(ChatColor.YELLOW + "/aoli off" + ChatColor.GRAY + " - disable IP check");
        sender.sendMessage(ChatColor.YELLOW + "/aoli reload" + ChatColor.GRAY + " - reload config");
        sender.sendMessage(ChatColor.YELLOW + "/aoli list" + ChatColor.GRAY + " - show all entries");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            Set<String> completions = new LinkedHashSet<>();
            String prefix = args[0].toLowerCase();
            if ("add".startsWith(prefix)) completions.add("add");
            if ("delete".startsWith(prefix) || "del".startsWith(prefix)) completions.add("delete");
            if ("on".startsWith(prefix)) completions.add("on");
            if ("off".startsWith(prefix)) completions.add("off");
            if ("reload".startsWith(prefix)) completions.add("reload");
            if ("list".startsWith(prefix)) completions.add("list");
            return new ArrayList<>(completions);
        }
        if (args.length == 2) {
            String sub = args[0].toLowerCase();
            if (sub.equals("add") || sub.equals("delete") || sub.equals("del")) {
                String nickPrefix = args[1].toLowerCase();
                return plugin.getStore().getAll().stream()
                        .map(e -> e.nickname())
                        .filter(n -> n.toLowerCase().startsWith(nickPrefix))
                        .collect(Collectors.toList());
            }
        }
        return List.of();
    }
}
