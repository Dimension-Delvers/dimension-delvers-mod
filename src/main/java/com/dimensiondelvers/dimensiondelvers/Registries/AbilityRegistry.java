package com.dimensiondelvers.dimensiondelvers.Registries;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.abilities.AbstractAbility;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class AbilityRegistry {

    public static final ResourceKey<Registry<AbstractAbility>> ABILITY_REGISTRY_KEY = ResourceKey.createRegistryKey(DimensionDelvers.id("abilities"));
    public static final Registry<AbstractAbility> ABILITY_REGISTRY = new RegistryBuilder<>(ABILITY_REGISTRY_KEY).create();
    public static final DeferredRegister<AbstractAbility> ABILITY_REGISTRY_DEF = DeferredRegister.create(ABILITY_REGISTRY, DimensionDelvers.MODID);
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, DimensionDelvers.MODID);


    public static final ResourceKey<Registry<MapCodec<? extends AbstractAbility>>> ABILITY_TYPES_KEY = ResourceKey.createRegistryKey(DimensionDelvers.id("ability_types"));
    public static final Registry<MapCodec<? extends AbstractAbility>> ABILITY_TYPES_REGISTRY = new RegistryBuilder<>(ABILITY_TYPES_KEY).create();

    public static final DeferredRegister<MapCodec<? extends AbstractAbility>> ABILITY_TYPES = DeferredRegister.create(
            ABILITY_TYPES_KEY, DimensionDelvers.MODID
    );
}
