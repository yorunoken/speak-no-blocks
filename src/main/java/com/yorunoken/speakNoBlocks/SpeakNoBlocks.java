package com.yorunoken.speakNoBlocks;

import com.yorunoken.speakNoBlocks.config.ModConfig;
import com.yorunoken.speakNoBlocks.network.VoicePacketPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpeakNoBlocks implements ModInitializer {
    public static final String MOD_ID = "speak-no-blocks";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // load config
        ModConfig.load();

        // register the bridge
        PayloadTypeRegistry.playC2S().register(VoicePacketPayload.ID, VoicePacketPayload.CODEC);


        ServerPlayNetworking.registerGlobalReceiver(VoicePacketPayload.ID, ((payload, context) -> {
            MinecraftServer server = context.server();

            server.execute(() -> {
                Set<Block> targetBlocks = new HashSet<>();

                System.out.println("Received block IDs from " + context.player().getName().getString());
                for (String blockId : payload.blockIds()) {
                    System.out.println(" - " + blockId);
                    targetBlocks.add(Registries.BLOCK.get(Identifier.tryParse(blockId)));
                }

                new BlockHandler(server, targetBlocks).breakBlocks();
            });
        }));
    }
}
