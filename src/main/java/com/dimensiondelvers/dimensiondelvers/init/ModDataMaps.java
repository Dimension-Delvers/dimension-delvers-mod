package com.dimensiondelvers.dimensiondelvers.init;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.item.essence.EssenceValue;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

/**
 * Registers any Data Maps defined by the mod
 */
@EventBusSubscriber(modid = DimensionDelvers.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModDataMaps {
    public static final DataMapType<Item, EssenceValue> ESSENCE_VALUE_DATA = DataMapType.builder(
                    DimensionDelvers.id("essence_value"),
                    Registries.ITEM,
                    EssenceValue.CODEC
            ).synced(EssenceValue.CODEC, true)
            .build();

    @SubscribeEvent
    public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(ESSENCE_VALUE_DATA);
    }
}
