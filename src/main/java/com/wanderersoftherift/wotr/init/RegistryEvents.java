package com.wanderersoftherift.wotr.init;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.item.runegem.RunegemData;
import com.wanderersoftherift.wotr.abilities.effects.marker.EffectMarker;
import com.wanderersoftherift.wotr.item.skillgem.Upgrade;
import com.wanderersoftherift.wotr.modifier.Modifier;
import com.wanderersoftherift.wotr.modifier.effect.AbstractModifierEffect;
import com.wanderersoftherift.wotr.world.level.levelgen.theme.RiftTheme;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber(modid = WanderersOfTheRift.MODID, bus = EventBusSubscriber.Bus.MOD)
public class RegistryEvents {

    public static final ResourceKey<Registry<EffectMarker>> EFFECT_MARKER_REGISTRY = ResourceKey.createRegistryKey(WanderersOfTheRift.id("effect_marker"));

    @SubscribeEvent
    static void registerRegistries(NewRegistryEvent event) {
        event.register(ModModifierEffects.MODIFIER_TYPE_REGISTRY);
        event.register(ModInputBlockStateTypes.INPUT_BLOCKSTATE_TYPE_REGISTRY);
        event.register(ModOutputBlockStateTypes.OUTPUT_BLOCKSTATE_TYPE_REGISTRY);
        event.register(ModOngoingObjectiveTypes.ONGOING_OBJECTIVE_TYPE_REGISTRY);
    }

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
                ModModifierEffects.MODIFIER_EFFECT_KEY,
                AbstractModifierEffect.DIRECT_CODEC,
                AbstractModifierEffect.DIRECT_CODEC
        );
        event.dataPackRegistry(
                ModDatapackRegistries.MODIFIER_KEY,
                Modifier.DIRECT_CODEC,
                Modifier.DIRECT_CODEC
        );
        event.dataPackRegistry(
                ModRiftThemes.RIFT_THEME_KEY,
                RiftTheme.DIRECT_CODEC,
                RiftTheme.DIRECT_SYNC_CODEC
        );
        event.dataPackRegistry(
                ModDatapackRegistries.RUNEGEM_DATA_KEY,
                RunegemData.CODEC,
                RunegemData.CODEC
        );
        event.dataPackRegistry(
                Upgrade.UPGRADE_REGISTRY_KEY,
                Upgrade.CODEC,
                Upgrade.CODEC
        );
        event.dataPackRegistry(
                EFFECT_MARKER_REGISTRY,
                EffectMarker.CODEC,
                EffectMarker.CODEC
        );
    }
}
