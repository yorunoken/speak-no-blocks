package com.yorunoken.speakNoBlocks;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;

public final class SpeakNoBlocks extends JavaPlugin {
    public HttpServer server;
    public final int DEFAULT_PORT = 8080;
    public int radius;
    public final int DEFAULT_RADIUS = 16;

    @Override
    public void onEnable() {
        saveDefaultConfig(); // save config.yml
        radius = getConfig().getInt("radius", DEFAULT_RADIUS);

        if (getCommand("speaknoblocks") != null) {
            var controller = new Controller(this);
            Objects.requireNonNull(getCommand("speaknoblocks")).setExecutor(controller);
            Objects.requireNonNull(getCommand("speaknoblocks")).setTabCompleter(controller);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Shutting down web server");
        if (server != null) {
            server.stop(0);
        }
    }
}
