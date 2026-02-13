package com.yorunoken.speakNoBlocks;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;

public final class SpeakNoBlocks extends JavaPlugin {
    public int radius;
    public final int DEFAULT_RADIUS = 16;
    public boolean enabled = true;

    @Override
    public void onEnable() {
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "speaknoblocks:voice_cmd", new VoicePacketHandler(this));

        saveDefaultConfig();
        radius = getConfig().getInt("radius", DEFAULT_RADIUS);

        if (getCommand("speaknoblocks") != null) {
            var controller = new Controller(this);
            Objects.requireNonNull(getCommand("speaknoblocks")).setExecutor(controller);
            Objects.requireNonNull(getCommand("speaknoblocks")).setTabCompleter(controller);
        }

        getLogger().info("Speak No Blocks: Native Networking Ready!");
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
        getLogger().info("Speak No Blocks: Disabled.");
    }
}