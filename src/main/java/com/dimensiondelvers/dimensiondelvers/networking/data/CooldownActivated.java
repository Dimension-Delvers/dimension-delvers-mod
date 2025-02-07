package com.dimensiondelvers.dimensiondelvers.networking.data;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CooldownActivated(String ability_location, int cooldownLength) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CooldownActivated> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(DimensionDelvers.MODID, "cooldown_activated"));

    public static final StreamCodec<ByteBuf, CooldownActivated> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            CooldownActivated::ability_location,
            ByteBufCodecs.INT,
            CooldownActivated::cooldownLength,
            CooldownActivated::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
