package com.wanderersoftherift.wotr.networking.data;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CooldownActivated(int slot, int cooldownLength) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CooldownActivated> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(WanderersOfTheRift.MODID, "cooldown_activated"));

    public static final StreamCodec<ByteBuf, CooldownActivated> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            CooldownActivated::slot,
            ByteBufCodecs.INT,
            CooldownActivated::cooldownLength,
            CooldownActivated::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
