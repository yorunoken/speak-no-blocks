package com.yorunoken.speakNoBlocks.client.util;

import net.minecraft.client.MinecraftClient;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.*;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class VoskDownloader {

    private static final String MODEL_URL = "https://alphacephei.com/vosk/models/vosk-model-en-us-0.22.zip";

    public static String status = "Ready";
    public static boolean isDownloading = false;
    public static float progress = 0.0f;

    public static void startDownload(Runnable onComplete) {
        if (isDownloading) return;
        isDownloading = true;
        progress = 0.0f;

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

                status = "Starting Download...";

                URL url = URI.create(MODEL_URL).toURL();
                URLConnection connection = url.openConnection();
                long fileSize = connection.getContentLengthLong();

                try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
                     FileOutputStream out = new FileOutputStream(tempZip.toFile())) {

                    byte[] dataBuffer = new byte[8192]; // 8KB buffer
                    int bytesRead;
                    long totalRead = 0;

                    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                        out.write(dataBuffer, 0, bytesRead);
                        totalRead += bytesRead;

                        if (fileSize > 0) {
                            progress = (float) totalRead / fileSize;
                            int percent = (int) (progress * 100);
                            status = "Downloading: " + percent + "%";
                        }
                    }
                }

                status = "Unzipping (this may take a moment)...";
                unzip(tempZip, tempExtractDir);

                Path modelRoot = findModelRoot(tempExtractDir);

                if (modelRoot == null) {
                    throw new IOException("Downloaded zip does not contain a valid model structure!");
                }

                status = "Installing...";
                if (!Files.exists(finalDest.getParent())) Files.createDirectories(finalDest.getParent());
                Files.move(modelRoot, finalDest, StandardCopyOption.ATOMIC_MOVE);

                deletePath(tempZip);
                deletePath(tempExtractDir);

                status = "Done! Start the model";
                isDownloading = false;
                progress = 1.0f;
                onComplete.run();

            } catch (Exception e) {
                e.printStackTrace();
                status = "Error: " + e.getMessage();
                isDownloading = false;
                progress = 0.0f;
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