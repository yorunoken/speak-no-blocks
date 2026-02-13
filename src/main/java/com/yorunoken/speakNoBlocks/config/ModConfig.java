package com.yorunoken.speakNoBlocks.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    public int radius = 16;

    private static ModConfig INSTANCE;
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("speak-no-blocks.json").toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

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
            } catch (IOException e) {
                e.printStackTrace();
                INSTANCE = new ModConfig(); // Fallback
            }
        } else {
            INSTANCE = new ModConfig();
            save(); // Create the default file
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}