package com.yorunoken.speakNoBlocks.client.gui;

import com.yorunoken.speakNoBlocks.client.SpeakNoBlocksClient;
import com.yorunoken.speakNoBlocks.client.util.VoskHandler;
import com.yorunoken.speakNoBlocks.client.util.VoskDownloader;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class VoskScreen extends Screen {

    private final Screen parent;
    private ButtonWidget downloadBtn;
    private ButtonWidget toggleBtn;

    public VoskScreen(Screen parent) {
        super(Text.of("Speak No Blocks Configuration"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int buttonWidth = 200;
        int buttonHeight = 20;
        int centerX = this.width / 2 - buttonWidth / 2;
        int startY = this.height / 4 + 24;

        this.toggleBtn = ButtonWidget.builder(Text.of("Start Voice"), button -> {
                    VoskHandler handler = SpeakNoBlocksClient.speechHandler;
                    if (handler.isActive()) {
                        handler.stop();
                    } else {
                        handler.start();
                    }
                })
                .dimensions(centerX, startY, buttonWidth, buttonHeight)
                .build();
        this.addDrawableChild(toggleBtn);

        this.downloadBtn = ButtonWidget.builder(Text.of("Download Model (1.8G)"), button -> {
                    button.active = false;
                    if (SpeakNoBlocksClient.speechHandler.isActive()) {
                        SpeakNoBlocksClient.speechHandler.stop();
                    }
                    VoskDownloader.startDownload(() -> {
                        if (this.client != null) {
                            this.client.execute(() -> button.active = true);
                        }
                    });
                })
                .dimensions(centerX, startY + 48, buttonWidth, buttonHeight)
                .build();
        this.addDrawableChild(downloadBtn);

        this.addDrawableChild(ButtonWidget.builder(Text.of("Done"), button -> this.close())
                .dimensions(centerX, this.height - 40, buttonWidth, buttonHeight)
                .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);

        String voiceStatus = VoskHandler.status;
        int voiceColor = voiceStatus.equals("Listening") ? 0x55FF55 : (voiceStatus.startsWith("Error") ? 0xFF5555 : 0xAAAAAA);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.of("Voice Status: " + voiceStatus), this.width / 2, this.toggleBtn.getY() - 15, voiceColor);

        String dlStatus = VoskDownloader.status;
        int dlColor = dlStatus.startsWith("Done") ? 0x55FF55 : (dlStatus.startsWith("Error") ? 0xFF5555 : 0xFFFFFF);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.of("Model Status: " + dlStatus), this.width / 2, this.downloadBtn.getY() - 15, dlColor);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }
}