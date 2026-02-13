package com.yorunoken.speakNoBlocks;

import com.sun.net.httpserver.HttpServer;
import com.yorunoken.speakNoBlocks.api.BreakBlocks;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.net.InetSocketAddress;
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
        // We only have suggestions for the first argument (start, stop, reload)
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            suggestions.add("start");
            suggestions.add("stop");
            suggestions.add("reload");

            return suggestions;
        }

        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String s, @NonNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /snb <start|stop|reload>");
            return true;
        }

        try {
            switch (args[0].toLowerCase()) {
                case "start":
                    startServerListener(sender);
                    sender.sendMessage(ChatColor.GREEN + "Speak No Blocks listener started.");
                    break;

                case "stop":
                    stopServerListener(sender);
                    sender.sendMessage(ChatColor.RED + "Speak No Blocks listener stopped.");
                    break;

                case "reload":
                    performReload(sender);
                    sender.sendMessage(ChatColor.YELLOW + "Configuration reloaded.");
                    break;

                default:
                    sender.sendMessage(ChatColor.RED + "Unknown subcommand. Use start, stop, or reload.");
                    break;
            }
        } catch(IOException e) {
            sender.sendMessage(ChatColor.RED + "There was an error while executing your command.");
        }

        return true;
    }

    private void startServerListener(CommandSender sender) throws IOException {
        if (plugin.server != null) {
            sender.sendMessage(ChatColor.YELLOW + "Web server already listening.");
            return;
        }

        int port = plugin.getConfig().getInt("port", plugin.DEFAULT_PORT);
        plugin.server = HttpServer.create(new InetSocketAddress(port), 0);
        plugin.server.createContext("/break", new BreakBlocks(this.plugin));
        plugin.server.setExecutor(null);
        plugin.server.start();
    }

    private void stopServerListener(CommandSender sender) throws IOException {
        if (plugin.server == null) {
            sender.sendMessage(ChatColor.YELLOW + "Web server already stopped.");
            return;
        }

        plugin.server.stop(0);
        plugin.server = null;
    }

    private void performReload(CommandSender sender) throws IOException {
        if (plugin.server != null) {
            stopServerListener(sender);
        }

        plugin.reloadConfig(); // reloads config
        sender.sendMessage(ChatColor.YELLOW + "Configuration file reloaded from disk.");
        plugin.radius = plugin.getConfig().getInt("radius", plugin.DEFAULT_RADIUS);

        startServerListener(sender);
        sender.sendMessage(ChatColor.GREEN + "Speak No Blocks listener re-started.");
    }
}
