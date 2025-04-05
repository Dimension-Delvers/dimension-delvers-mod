package com.wanderersoftherift.wotr.networking.data;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record UseAbility(int slot) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UseAbility> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(WanderersOfTheRift.MODID, "ability_type"));

    public static final StreamCodec<ByteBuf, UseAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            UseAbility::slot,
            UseAbility::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
