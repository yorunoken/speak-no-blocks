package com.yorunoken.speakNoBlocks.client.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yorunoken.speakNoBlocks.config.ModConfig;
import com.yorunoken.speakNoBlocks.network.VoicePacketPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class VoskHandler {
    private Model model;
    private Recognizer recognizer;
    private Thread listenerThread;
    private boolean isRunning = false;
    public static String status = "Stopped";

    private static final float SAMPLE_RATE = 16000.0f;

    public boolean isActive() {
        return isRunning;
    }

    public void start() {
        if (isRunning) return;

        status = "Starting...";

        listenerThread = new Thread(this::runRecognitionLoop, "Vosk Listener thread");
        listenerThread.start();
    }

    public void stop() {
        if (!isRunning) return;

        status = "Stopping...";
        isRunning = false;

        if (listenerThread != null) {
            listenerThread.interrupt();
        }
    }

    private void runRecognitionLoop() {
        try {
            LibVosk.setLogLevel(LogLevel.DEBUG);

            String modelPath = MinecraftClient.getInstance().runDirectory.getAbsolutePath() + "/vosk-model";
            System.out.println("loading vosk from: " + modelPath);

            this.model = new Model(modelPath);
            this.recognizer = new Recognizer(model, SAMPLE_RATE);

            AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.err.println("Microphone not supported with this format!");
                return;
            }

            TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open();
            microphone.start();

            System.out.println("Vosk is listening...");
            isRunning = true;
            status = "Listening";

            int bufferSize = 2000;
            byte[] b = new byte[bufferSize];

            while (isRunning) {
                int bytesRead = microphone.read(b, 0, b.length);

                if (bytesRead > 0) {
                    String jsonStr = null;

                    if (recognizer.acceptWaveForm(b, bytesRead)) {
                        jsonStr = recognizer.getResult();
                    } else {
                        jsonStr = recognizer.getPartialResult();
                    }
                    processVoiceCommand(jsonStr);
                }
            }

            microphone.close();
            recognizer.close();
            model.close();
            status = "Stopped";
        } catch(Exception e) {
            e.printStackTrace();
            status = "Error: " + e.getMessage();
            isRunning = false;
        }
    }

    private void processVoiceCommand(String json) {
        if (json == null || json.isEmpty()) return;

        try {
            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
            String text = "";

            // Handle for complete sentences *and* partial sentences
            if (obj.has("text")) text = obj.get("text").getAsString();
            else if (obj.has("partial")) text = obj.get("partial").getAsString();

            if (text.isEmpty()) return;

            Map<String, List<String>> commands = ModConfig.get().getCommandMap();

            Optional<String> match = commands.keySet().stream()
                    .filter(text::contains)
                    .max(Comparator.comparingInt(String::length));

            if (match.isPresent()) {
                String keyword = match.get();
                List<String> blocks = commands.get(keyword);
                System.out.println("Matched: " + keyword);
                System.out.println("Blocks to destroy: " + blocks);

                ClientPlayNetworking.send(new VoicePacketPayload(blocks));
                recognizer.reset();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
