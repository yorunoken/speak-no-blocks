package com.yorunoken.speakNoBlocks.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ModConfig {
    public int radius = 16;

    public List<VoiceEntry> voiceCommands = new ArrayList<>();

    private transient Map<String, List<String>> commandCache = null;

    private static ModConfig INSTANCE;
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("speak-no-blocks.jsonc").toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().create();

    public static class VoiceEntry {
        public List<String> triggers;
        public List<String> blocks;
    }

    public static ModConfig get() {
        if (INSTANCE == null) {
            load();
        }
        return INSTANCE;
    }

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                INSTANCE = GSON.fromJson(reader, ModConfig.class);
                if (INSTANCE == null) INSTANCE = new ModConfig();
            } catch (IOException e) {
                e.printStackTrace();
                INSTANCE = new ModConfig();
            }
        } else {
            INSTANCE = new ModConfig();
            INSTANCE.loadDefaultsFromJar();
            save();
        }

        INSTANCE.buildCache();
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDefaultsFromJar() {
        String path = "/assets/speak-no-blocks/default_definitions.jsonc";

        try (InputStream in = ModConfig.class.getResourceAsStream(path)) {
            if (in != null) {
                Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
                List<VoiceEntry> defaults = GSON.fromJson(reader, new TypeToken<List<VoiceEntry>>(){}.getType());

                if (defaults != null) {
                    this.voiceCommands.addAll(defaults);
                    System.out.println("SpeakNoBlocks: Loaded default definitions from JAR.");
                }
            } else {
                System.err.println("SpeakNoBlocks: Could not find default_definitions.jsonc in JAR at " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, List<String>> getCommandMap() {
        if (commandCache == null) buildCache();
        return commandCache;
    }

    private void buildCache() {
        commandCache = new HashMap<>();
        if (voiceCommands != null) {
            for (VoiceEntry entry : voiceCommands) {
                for (String trigger : entry.triggers) {
                    commandCache.put(trigger.toLowerCase(), entry.blocks);
                }
            }
        }
    }
}