package com.dimensiondelvers.dimensiondelvers.init;


import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.modifier.AbstractModifier;
import com.dimensiondelvers.dimensiondelvers.modifier.Modifier;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import java.util.function.Supplier;


public class ModAbstractModifiers {

    // Registry and key for Abstract Modifiers
    public static final ResourceKey<Registry<MapCodec<? extends AbstractModifier>>> MODIFIER_TYPE_KEY = ResourceKey.createRegistryKey(DimensionDelvers.id("modifier_type"));

    public static final ResourceKey<Registry<AbstractModifier>> MODIFIER_KEY = ResourceKey.createRegistryKey(DimensionDelvers.id("modifier"));

    public static final Registry<MapCodec<? extends AbstractModifier>> MODIFIER_TYPE_REGISTRY = new RegistryBuilder<>(MODIFIER_TYPE_KEY).create();

    // Creating the Deferred Register
    public static final DeferredRegister<MapCodec<? extends AbstractModifier>> MODIFIER_TYPES = DeferredRegister.create(
            MODIFIER_TYPE_KEY, DimensionDelvers.MODID
    );

    public static final Supplier<MapCodec<? extends AbstractModifier>> GEAR_MODIFIER = MODIFIER_TYPES.register(
            "gear_modifier", () -> Modifier.MODIFIER_CODEC
    );
}
