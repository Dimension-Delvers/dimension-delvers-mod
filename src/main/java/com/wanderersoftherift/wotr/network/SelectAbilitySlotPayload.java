package com.wanderersoftherift.wotr.network;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.init.ModAttachments;
import com.wanderersoftherift.wotr.item.skillgem.AbilitySlots;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record SelectAbilitySlotPayload(int slot) implements CustomPacketPayload {
    public static final Type<SelectAbilitySlotPayload> ID = new Type<>(WanderersOfTheRift.id("select_ability_slot_request"));
    public static final StreamCodec<ByteBuf, SelectAbilitySlotPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SelectAbilitySlotPayload::slot,
            SelectAbilitySlotPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    public void handleOnServer(IPayloadContext context) {
        AbilitySlots abilitySlots = context.player().getData(ModAttachments.ABILITY_SLOTS);
        abilitySlots.setSelectedSlot(slot);
    }
}
