package com.wanderersoftherift.wotr.network;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.init.ModAttachments;
import com.wanderersoftherift.wotr.item.skillgem.AbilitySlots;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record AbilitySlotsUpdateMessage(int slot, ItemStack stack) implements CustomPacketPayload {
    public static final Type<AbilitySlotsUpdateMessage> ID = new Type<>(WanderersOfTheRift.id("ability_slots_update"));
    public static final StreamCodec<RegistryFriendlyByteBuf, AbilitySlotsUpdateMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, AbilitySlotsUpdateMessage::slot,
            ItemStack.OPTIONAL_STREAM_CODEC, AbilitySlotsUpdateMessage::stack,
            AbilitySlotsUpdateMessage::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    public void handleOnClient(IPayloadContext context) {
        AbilitySlots abilitySlots = context.player().getData(ModAttachments.ABILITY_SLOTS);
        if (slot >= 0 && slot < abilitySlots.getSlots()) {
            abilitySlots.setStackInSlot(slot, stack);
        }
    }
}
