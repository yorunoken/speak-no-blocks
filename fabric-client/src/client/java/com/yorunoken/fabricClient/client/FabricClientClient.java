package com.yorunoken.fabricClient.client;

import com.yorunoken.fabricClient.client.util.VoicePayload;
import net.fabricmc.api.ClientModInitializer;
import com.yorunoken.fabricClient.client.util.SpeechHandler;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;


public class FabricClientClient implements ClientModInitializer {

    public static SpeechHandler speechHandler;
    @Override
    public void onInitializeClient() {
        PayloadTypeRegistry.playC2S().register(VoicePayload.ID, VoicePayload.CODEC);

        speechHandler = new SpeechHandler();
        speechHandler.start();
        System.out.println("Started speech handler");
    }
}
