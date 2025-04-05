package com.wanderersoftherift.wotr.networking.data;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

//TODO this is purely for testing for now, probably handle the upgrade claiming slightly differently
public record ClaimUpgrade(String upgrade_location) implements CustomPacketPayload {
    public static final Type<ClaimUpgrade> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(WanderersOfTheRift.MODID, "upgrade_type"));

    public static final StreamCodec<ByteBuf, ClaimUpgrade> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ClaimUpgrade::upgrade_location,
            ClaimUpgrade::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
