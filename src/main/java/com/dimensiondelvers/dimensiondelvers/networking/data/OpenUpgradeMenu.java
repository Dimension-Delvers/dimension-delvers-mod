package com.dimensiondelvers.dimensiondelvers.networking.data;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record OpenUpgradeMenu(String menu) implements CustomPacketPayload {
    public static final Type<OpenUpgradeMenu> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(DimensionDelvers.MODID, "open_menu"));

    public static final StreamCodec<ByteBuf, OpenUpgradeMenu> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            OpenUpgradeMenu::menu,
            OpenUpgradeMenu::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
