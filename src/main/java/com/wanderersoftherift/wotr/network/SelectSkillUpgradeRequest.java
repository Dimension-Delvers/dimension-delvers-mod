package com.wanderersoftherift.wotr.network;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record SelectSkillUpgradeRequest(int choice, int selection) implements CustomPacketPayload {
    public static final Type<SelectSkillUpgradeRequest> ID = new Type<>(WanderersOfTheRift.id("select_skill_upgrade_request"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SelectSkillUpgradeRequest> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SelectSkillUpgradeRequest::choice,
            ByteBufCodecs.INT, SelectSkillUpgradeRequest::selection,
            SelectSkillUpgradeRequest::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
