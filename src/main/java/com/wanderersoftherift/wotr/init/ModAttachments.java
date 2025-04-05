package com.wanderersoftherift.wotr.init;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.abilities.AttachedEffectData;
import com.wanderersoftherift.wotr.abilities.Serializable.PlayerCooldownData;
import com.wanderersoftherift.wotr.abilities.Serializable.PlayerDurationData;
import com.wanderersoftherift.wotr.abilities.effects.marker.EffectDisplayData;
import com.wanderersoftherift.wotr.item.skillgem.AbilitySlots;
import com.wanderersoftherift.wotr.server.inventorySnapshot.InventorySnapshot;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@EventBusSubscriber(modid = WanderersOfTheRift.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, WanderersOfTheRift.MODID);

    public static final Supplier<AttachmentType<InventorySnapshot>> INVENTORY_SNAPSHOT = ATTACHMENT_TYPES.register("inventory_snapshot", () -> AttachmentType.builder(InventorySnapshot::new).serialize(InventorySnapshot.CODEC).build());
    public static final Supplier<AttachmentType<List<ItemStack>>> RESPAWN_ITEMS = ATTACHMENT_TYPES.register("respawn_items", () -> AttachmentType.builder(() -> (List<ItemStack>)new ArrayList<ItemStack>()).serialize(ItemStack.CODEC.listOf()).copyOnDeath().build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerCooldownData>> ABILITY_COOLDOWNS = ATTACHMENT_TYPES.register(
            "cooldowns", () -> AttachmentType.builder(PlayerCooldownData::new).serialize(PlayerCooldownData.CODEC).build()
    );
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerDurationData>> DURATIONS = ATTACHMENT_TYPES.register(
            "durations", () -> AttachmentType.serializable(PlayerDurationData::new).build()
    );
    public static final Supplier<AttachmentType<AbilitySlots>> ABILITY_SLOTS = ATTACHMENT_TYPES.register("ability_slots", () -> AttachmentType.builder(AbilitySlots::new).serialize(AbilitySlots.CODEC).copyOnDeath().build());

    public static final Supplier<AttachmentType<AttachedEffectData>> ATTACHED_EFFECTS = ATTACHMENT_TYPES.register("attached_effects", () -> AttachmentType.builder(AttachedEffectData::new).serialize(AttachedEffectData.CODEC).build());
    public static final Supplier<AttachmentType<EffectDisplayData>> EFFECT_DISPLAY = ATTACHMENT_TYPES.register("effect_display", () -> AttachmentType.builder(() -> new EffectDisplayData()).build());

    @SubscribeEvent
    public static void serverTick(ServerTickEvent.Pre event) {
        for (ServerLevel level : event.getServer().getAllLevels()) {
            AttachedEffectData.tick(level);
        }
    }

}
