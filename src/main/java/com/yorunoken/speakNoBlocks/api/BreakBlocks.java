package com.yorunoken.speakNoBlocks.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yorunoken.speakNoBlocks.SpeakNoBlocks;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BreakBlocks implements HttpHandler {
    private final SpeakNoBlocks plugin;

    public BreakBlocks(SpeakNoBlocks plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        plugin.getLogger().info("Handling /break");
        if ("POST".equals(exchange.getRequestMethod())) {
            handlePayload(exchange);
            return;
        }

        // return bad method if no POST
        exchange.sendResponseHeaders(405, -1);
    }

    private void sendResponse(HttpExchange exchange, int code, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, response.getBytes(StandardCharsets.UTF_8).length);
        exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
        exchange.getResponseBody().close();
    }

    private void handlePayload(HttpExchange exchange) throws IOException {
        Gson gson = new Gson();

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        JsonObject payload = gson.fromJson(body, JsonObject.class);

        if (payload == null || !payload.has("user") || !payload.has("blocks")) {
            sendResponse(exchange, 400, "{\"error\": \"Missing 'user' or 'block' in JSON\"}");
            return;
        }

        String userInitiated = payload.get("user").getAsString();
        JsonArray targetBlocks = payload.get("blocks").getAsJsonArray();

        plugin.getLogger().info("Parsed user: " + userInitiated);

        Bukkit.getScheduler().runTask(plugin, () -> {
            // get all online players, and loop through them to break their blocks
            Collection<? extends Player> players = Bukkit.getOnlinePlayers();

            for (Player player : players) {
                // commence block breaking for each player
                breakBlocks(player, targetBlocks);
            }
        });

        sendResponse(exchange, 200, "{\"status\": \"success\"}");
    }

    private void breakBlocks(Player player, JsonArray blocksArray) {
        Set<Material> targets = new HashSet<>();

        for (JsonElement element : blocksArray) {
            String blockName = element.getAsString();
            Material mat = Material.matchMaterial(blockName);

            if (mat != null) {
                targets.add(mat);
                continue;
            }
            plugin.getLogger().info("Invalid block name: (check API)" + blockName);
        }

        Location location = player.getLocation();

        int r = plugin.radius;
        int minX = location.getBlockX() - r;
        int maxX = location.getBlockX() + r;

        int minZ = location.getBlockZ() - r;
        int maxZ = location.getBlockZ() + r;

        int minY = -64;
        int maxY = 319;

        World world = location.getWorld();
        if (world == null) {
            plugin.getLogger().info("Could not find world.");
            return;
        }

        int blocksBroken = 0;

        // go from the lowest coordinates to the highest
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    // get block
                    Block block = world.getBlockAt(x, y, z);
                    if (targets.contains(block.getType())) {
                        world.spawnParticle(Particle.CLOUD, block.getLocation().add(0.5, 0.5, 0.5), 1, 0, 0, 0, 0.1);
                        block.setType(Material.AIR);
                        blocksBroken++;
                    }
                }
            }
        }

        if (blocksBroken > 0) {
            player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0f, 1.0f);
        }
    }
}
