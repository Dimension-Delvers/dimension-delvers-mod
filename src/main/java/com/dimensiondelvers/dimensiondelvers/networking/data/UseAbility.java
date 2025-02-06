package com.dimensiondelvers.dimensiondelvers.networking.data;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record UseAbility(String ability_location) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UseAbility> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(DimensionDelvers.MODID, "ability_type"));

    public static final StreamCodec<ByteBuf, UseAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            UseAbility::ability_location,
            UseAbility::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
