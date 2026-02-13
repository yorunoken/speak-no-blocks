package com.yorunoken.speakNoBlocks.client.util;

import net.minecraft.client.MinecraftClient;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class VoskDownloader {

    private static final String MODEL_URL = "https://alphacephei.com/vosk/models/vosk-model-en-us-0.22.zip";

    public static String status = "Ready";
    public static boolean isDownloading = false;

    public static void startDownload(Runnable onComplete) {
        if (isDownloading) return;
        isDownloading = true;

        new Thread(() -> {
            try {
                Path gameDir = MinecraftClient.getInstance().runDirectory.toPath();
                Path finalDest = gameDir.resolve("vosk-model");
                Path tempZip = gameDir.resolve("vosk_temp.zip");
                Path tempExtractDir = gameDir.resolve("vosk_temp_extract");

                status = "Cleaning up...";
                deletePath(finalDest);
                deletePath(tempZip);
                deletePath(tempExtractDir);

                status = "Downloading Model...";
                try (InputStream in = new URL(MODEL_URL).openStream()) {
                    Files.copy(in, tempZip, StandardCopyOption.REPLACE_EXISTING);
                }

                status = "Unzipping...";
                unzip(tempZip, tempExtractDir);

                Path modelRoot = findModelRoot(tempExtractDir);

                if (modelRoot == null) {
                    throw new IOException("Downloaded zip does not contain a valid model structure!");
                }

                status = "Installing...";
                Files.move(modelRoot, finalDest, StandardCopyOption.ATOMIC_MOVE);

                deletePath(tempZip);
                deletePath(tempExtractDir);

                status = "Done! Restart the model";
                isDownloading = false;
                onComplete.run();

            } catch (Exception e) {
                e.printStackTrace();
                status = "Error: " + e.getMessage();
                isDownloading = false;
            }
        }).start();
    }

    private static Path findModelRoot(Path startDir) throws IOException {
        try (Stream<Path> walk = Files.walk(startDir, 2)) { // Search depth of 2 is enough
            return walk.filter(p -> Files.isDirectory(p) &&
                            (Files.exists(p.resolve("conf")) || Files.exists(p.resolve("final.mdl"))))
                    .findFirst()
                    .orElse(null);
        }
    }

    private static void unzip(Path zipFile, Path destDir) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path newPath = destDir.resolve(entry.getName()).normalize();
                if (entry.isDirectory()) {
                    Files.createDirectories(newPath);
                } else {
                    Files.createDirectories(newPath.getParent());
                    try (OutputStream os = Files.newOutputStream(newPath)) {
                        zis.transferTo(os);
                    }
                }
            }
        }
    }

    private static void deletePath(Path path) {
        try {
            if (Files.exists(path)) {
                if (Files.isDirectory(path)) {
                    FileUtils.deleteDirectory(path.toFile());
                } else {
                    Files.delete(path);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to delete: " + path);
            e.printStackTrace();
        }
    }
}