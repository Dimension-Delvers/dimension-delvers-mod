package com.dimensiondelvers.dimensiondelvers.init;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.ability.AbstractAbility;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber(modid = DimensionDelvers.MODID, bus = EventBusSubscriber.Bus.MOD)
public class RegistryEvents {
    @SubscribeEvent
    static void registerRegistries(NewRegistryEvent event) {
        event.register(ModAbilityTypes.ABILITY_TYPES_REGISTRY);
    }
    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
                // The registry key.
                ModAbilityTypes.ABILITY_KEY,
                // The codec of the registry contents.
                AbstractAbility.DIRECT_CODEC,
                // The network codec of the registry contents. Often identical to the normal codec.
                // May be a reduced variant of the normal codec that omits data that is not needed on the client.
                // May be null. If null, registry entries will not be synced to the client at all.
                // May be omitted, which is functionally identical to passing null (a method overload
                // with two parameters is called that passes null to the normal three parameter method).
                AbstractAbility.DIRECT_CODEC
                // A consumer which configures the constructed registry via the RegistryBuilder.
                // May be omitted, which is functionally identical to passing builder -> {}.
        );
    }
}
