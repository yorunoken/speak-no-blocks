package com.yorunoken.speakNoBlocks.client;

import net.fabricmc.api.ClientModInitializer;
import com.yorunoken.speakNoBlocks.client.util.VoskHandler;

public class SpeakNoBlocksClient implements ClientModInitializer {

    public static VoskHandler speechHandler;
    @Override
    public void onInitializeClient() {
        speechHandler = new VoskHandler();
        speechHandler.start();
        System.out.println("Started speech handler");
    }
}
