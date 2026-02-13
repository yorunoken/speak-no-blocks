package com.yorunoken.speakNoBlocks;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.HashSet;
import java.util.Set;

public class VoicePacketHandler implements PluginMessageListener {
    private final SpeakNoBlocks plugin;

    public VoicePacketHandler(SpeakNoBlocks plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("speaknoblocks:voice_cmd")) return;
        ByteArrayDataInput in = ByteStreams.newDataInput(message);

        try {
            int count = in.readInt();
            Set<Material> targets = new HashSet<>();

            for (int i = 0; i < count; i++) {
                String blockName = in.readUTF();
                Material mat = Material.matchMaterial(blockName);
                if (mat != null) targets.add(mat);
            }

            if (!targets.isEmpty()) {
                breakBlocks(targets);
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to parse voice packet from " + player.getName());
            e.printStackTrace();
        }
    }

    private void breakBlocks(Set<Material> targets) {
        int r = plugin.radius;
        int minY = -64;
        int maxY = 319;

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            org.bukkit.Location location = player.getLocation();
            World world = location.getWorld();
            if (world == null) continue;

            int minX = location.getBlockX() - r;
            int maxX = location.getBlockX() + r;
            int minZ = location.getBlockZ() - r;
            int maxZ = location.getBlockZ() + r;

            int blocksBrokenForThisPlayer = 0;

            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    for (int y = minY; y <= maxY; y++) {
                        Block block = world.getBlockAt(x, y, z);
                        if (targets.contains(block.getType())) {
                            world.spawnParticle(Particle.CLOUD, block.getLocation().add(0.5, 0.5, 0.5), 1, 0, 0, 0, 0.1);
                            block.setType(Material.AIR);
                            blocksBrokenForThisPlayer++;
                        }
                    }
                }
            }

            if (blocksBrokenForThisPlayer > 0) {
                player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0f, 1.0f);
            }
        }
    }
}