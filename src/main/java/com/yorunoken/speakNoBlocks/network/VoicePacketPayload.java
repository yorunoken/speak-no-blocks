package com.yorunoken.speakNoBlocks.network;

import com.yorunoken.speakNoBlocks.SpeakNoBlocks;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.List;

public record VoicePacketPayload(List<String> blockIds) implements CustomPayload {
    public static final CustomPayload.Id<VoicePacketPayload> ID = new CustomPayload.Id<>(Identifier.of(SpeakNoBlocks.MOD_ID, "block_ids"));

    public static final PacketCodec<RegistryByteBuf, VoicePacketPayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING.collect(PacketCodecs.toList()), VoicePacketPayload::blockIds, VoicePacketPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
