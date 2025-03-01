package com.dimensiondelvers.dimensiondelvers.init;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.item.essence.EssenceType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Registers all essence types defined by the mod
 */
public class ModEssenceTypes {
    public static final ResourceKey<Registry<EssenceType>> ESSENCE_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(DimensionDelvers.id("essence_type"));
    public static final Registry<EssenceType> ESSENCE_TYPE_REGISTRY = new RegistryBuilder<>(ESSENCE_TYPE_REGISTRY_KEY)
            .sync(true)
            .create();

    public static final DeferredRegister<EssenceType> ESSENCE_TYPES = DeferredRegister.create(ESSENCE_TYPE_REGISTRY, DimensionDelvers.MODID);

    // Creating the Deferred Register
    public static final Supplier<EssenceType> MEAT = ESSENCE_TYPES.register("meat", () -> new EssenceType(DimensionDelvers.id("meat")));
    public static final Supplier<EssenceType> EARTH = ESSENCE_TYPES.register("earth", () -> new EssenceType(DimensionDelvers.id("earth")));
    public static final Supplier<EssenceType> LIFE = ESSENCE_TYPES.register("life", () -> new EssenceType(DimensionDelvers.id("life")));
    public static final Supplier<EssenceType> WATER = ESSENCE_TYPES.register("water", () -> new EssenceType(DimensionDelvers.id("water")));
}
