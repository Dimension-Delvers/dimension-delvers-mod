package com.wanderersoftherift.wotr.init;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.abilities.Serializable.PlayerCooldownData;
import com.wanderersoftherift.wotr.abilities.Serializable.PlayerDurationData;
import com.wanderersoftherift.wotr.item.skillgem.AbilitySlots;
import com.wanderersoftherift.wotr.server.inventorySnapshot.InventorySnapshot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, WanderersOfTheRift.MODID);

    public static final Supplier<AttachmentType<InventorySnapshot>> INVENTORY_SNAPSHOT = ATTACHMENT_TYPES.register("inventory_snapshot", () -> AttachmentType.builder(InventorySnapshot::new).serialize(InventorySnapshot.CODEC).build());
    public static final Supplier<AttachmentType<List<ItemStack>>> RESPAWN_ITEMS = ATTACHMENT_TYPES.register("respawn_items", () -> AttachmentType.builder(() -> (List<ItemStack>)new ArrayList<ItemStack>()).serialize(ItemStack.CODEC.listOf()).copyOnDeath().build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerCooldownData>> COOL_DOWNS = ATTACHMENT_TYPES.register(
            "cooldowns", () -> AttachmentType.serializable(PlayerCooldownData::new).build()
    );
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerDurationData>> DURATIONS = ATTACHMENT_TYPES.register(
            "durations", () -> AttachmentType.serializable(PlayerDurationData::new).build()
    );
    public static final Supplier<AttachmentType<AbilitySlots>> ABILITY_SLOTS = ATTACHMENT_TYPES.register("ability_slots", () -> AttachmentType.builder(AbilitySlots::new).serialize(AbilitySlots.CODEC).copyOnDeath().build());
}
