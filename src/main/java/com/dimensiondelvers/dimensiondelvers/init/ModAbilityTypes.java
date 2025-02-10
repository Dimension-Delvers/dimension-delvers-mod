package com.dimensiondelvers.dimensiondelvers.init;


import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.ability.AbstractAbility;
import com.dimensiondelvers.dimensiondelvers.ability.JumpAbility;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class ModAbilityTypes {
    //Resource key for the registry for ability types
    public static final ResourceKey<Registry<MapCodec<? extends AbstractAbility>>> ABILITY_TYPES_KEY = ResourceKey.createRegistryKey(DimensionDelvers.id("ability_types"));

    public static final ResourceKey<Registry<AbstractAbility>> ABILITY_KEY = ResourceKey.createRegistryKey(DimensionDelvers.id("ability"));
    //Registry for the ability types
    public static final Registry<MapCodec<? extends AbstractAbility>> ABILITY_TYPES_REGISTRY = new RegistryBuilder<>(ABILITY_TYPES_KEY).create();

    public static final DeferredRegister<MapCodec<? extends AbstractAbility>> ABILITY_TYPES = DeferredRegister.create(
            ABILITY_TYPES_KEY, DimensionDelvers.MODID
    );
    public static final Supplier<MapCodec<? extends AbstractAbility>> JUMP_ABILITY_TYPE = ABILITY_TYPES.register(
            "jump", ()-> JumpAbility.JUMP_CODEC);
}
