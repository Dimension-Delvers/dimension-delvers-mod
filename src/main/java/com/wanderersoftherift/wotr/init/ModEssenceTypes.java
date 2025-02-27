package com.wanderersoftherift.wotr.init;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.item.essence.EssenceType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/**
 * Registers all essence types defined by the mod
 */
public class ModEssenceTypes {
    public static final ResourceLocation NONE_ID = WanderersOfTheRift.id("none");
    public static final ResourceKey<Registry<EssenceType>> ESSENCE_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(WanderersOfTheRift.id("essence_type"));
    public static final Registry<EssenceType> ESSENCE_TYPE_REGISTRY = new RegistryBuilder<>(ESSENCE_TYPE_REGISTRY_KEY)
            .sync(true)
            .defaultKey(NONE_ID)
            .create();

    public static final DeferredRegister<EssenceType> ESSENCE_TYPES = DeferredRegister.create(ESSENCE_TYPE_REGISTRY, WanderersOfTheRift.MODID);

    // Creating the Deferred Register
    public static final Supplier<EssenceType> NONE = ESSENCE_TYPES.register("none", () -> new EssenceType(WanderersOfTheRift.id("none")));
    public static final Supplier<EssenceType> MEAT = ESSENCE_TYPES.register("meat", () -> new EssenceType(WanderersOfTheRift.id("meat")));
    public static final Supplier<EssenceType> EARTH = ESSENCE_TYPES.register("earth", () -> new EssenceType(WanderersOfTheRift.id("earth")));
    public static final Supplier<EssenceType> LIFE = ESSENCE_TYPES.register("life", () -> new EssenceType(WanderersOfTheRift.id("life")));
    public static final Supplier<EssenceType> WATER = ESSENCE_TYPES.register("water", () -> new EssenceType(WanderersOfTheRift.id("water")));
}
