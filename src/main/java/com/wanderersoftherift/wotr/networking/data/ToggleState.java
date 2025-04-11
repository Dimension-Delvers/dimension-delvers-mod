package com.wanderersoftherift.wotr.networking.data;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ToggleState(String ability_location, boolean toggle) implements CustomPacketPayload {
    public static final Type<ToggleState> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(WanderersOfTheRift.MODID, "toggle_changed"));

    public static final StreamCodec<ByteBuf, ToggleState> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ToggleState::ability_location,
            ByteBufCodecs.BOOL,
            ToggleState::toggle,
            ToggleState::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
