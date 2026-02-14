package com.yorunoken.speakNoBlocks;

import com.yorunoken.speakNoBlocks.config.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import java.util.Set;

public class BlockHandler {
    private final MinecraftServer server;
    private final Set<Block> targetBlocks;

    public BlockHandler(MinecraftServer server, Set<Block> targetBlocks) {
        this.server = server;
        this.targetBlocks = targetBlocks;
    }

    public void breakBlocks() {
        int r = ModConfig.get().radius;

        // hard-coded limits of Minecraft
        int minY = -64;
        int maxY = 319;

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ServerWorld world = player.getServerWorld();
            BlockPos playerPos = player.getBlockPos();

            int minX = playerPos.getX() - r;
            int maxX = playerPos.getX() + r;
            int minZ = playerPos.getZ() - r;
            int maxZ = playerPos.getZ() + r;

            BlockPos.Mutable pos = new BlockPos.Mutable();
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    for (int y = minY; y <= maxY; y++) {
                        pos.set(x, y, z);

                        BlockState state = world.getBlockState(pos);

                        if (targetBlocks.contains(state.getBlock())) {
                            // world.addBlockBreakParticles(pos, state);
                            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                        }
                    }
                }
            }
        }
    }
}
