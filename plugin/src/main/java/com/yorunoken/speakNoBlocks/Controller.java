package com.yorunoken.speakNoBlocks;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Controller implements CommandExecutor, TabCompleter {
    private final SpeakNoBlocks plugin;

    public Controller(SpeakNoBlocks plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            suggestions.add("reload");
            suggestions.add("toggle");
            return suggestions;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String s, @NonNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /snb <reload|toggle>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                plugin.reloadConfig();
                plugin.radius = plugin.getConfig().getInt("radius", plugin.DEFAULT_RADIUS);
                sender.sendMessage(ChatColor.YELLOW + "SpeakNoBlocks config reloaded. Radius: " + plugin.radius);
                break;

            case "toggle":
                plugin.enabled = !plugin.enabled;
                String state = plugin.enabled ? ChatColor.GREEN + "ENABLED" : ChatColor.RED + "DISABLED";
                sender.sendMessage(ChatColor.GRAY + "Voice block breaking is now " + state);
                break;

            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand.");
                break;
        }

        return true;
    }
}