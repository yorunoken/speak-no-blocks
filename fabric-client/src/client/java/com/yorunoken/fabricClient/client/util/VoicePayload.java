package com.yorunoken.fabricClient.client.util;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import java.util.List;

public record VoicePayload(List<String> blockIds) implements CustomPayload {

    public static final CustomPayload.Id<VoicePayload> ID = new CustomPayload.Id<>(Identifier.of("speaknoblocks", "voice_cmd"));

    public static final PacketCodec<RegistryByteBuf, VoicePayload> CODEC = PacketCodec.of(
            (value, buf) -> {
                // Write the number of blocks as a standard Int
                buf.writeInt(value.blockIds().size());

                for (String id : value.blockIds()) {
                    byte[] bytes = id.getBytes(java.nio.charset.StandardCharsets.UTF_8);

                    buf.writeShort(bytes.length);
                    buf.writeBytes(bytes);
                }
            },
            buf -> {
                int size = buf.readInt();
                java.util.List<String> ids = new java.util.ArrayList<>();
                for (int i = 0; i < size; i++) {
                    short len = buf.readShort();
                    byte[] bytes = new byte[len];
                    buf.readBytes(bytes);
                    ids.add(new String(bytes, java.nio.charset.StandardCharsets.UTF_8));
                }
                return new VoicePayload(ids);
            }
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}