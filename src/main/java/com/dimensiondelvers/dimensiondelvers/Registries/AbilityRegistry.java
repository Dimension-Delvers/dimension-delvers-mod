package com.dimensiondelvers.dimensiondelvers.Registries;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.abilities.AbstractAbility;
import com.dimensiondelvers.dimensiondelvers.abilities.ApplyModifierAbility;
import com.dimensiondelvers.dimensiondelvers.abilities.BoostAbility;
import com.dimensiondelvers.dimensiondelvers.abilities.ProjectileAbility;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class AbilityRegistry {
    /*
     * This is the key for the registry that contains the actual datapacks/abilities
     */
    public static final ResourceKey<Registry<AbstractAbility>> DATA_PACK_ABILITY_REG_KEY = ResourceKey.createRegistryKey(DimensionDelvers.id("abilities"));

    /*
     * This sets up the registries for registering the ability types
     */
    public static final ResourceKey<Registry<MapCodec<? extends AbstractAbility>>> ABILITY_TYPES_KEY = ResourceKey.createRegistryKey(DimensionDelvers.id("ability_types"));

    public static final Registry<MapCodec<? extends AbstractAbility>> ABILITY_TYPES_REGISTRY = new RegistryBuilder<>(ABILITY_TYPES_KEY).create();

    public static final DeferredRegister<MapCodec<? extends AbstractAbility>> ABILITY_TYPES = DeferredRegister.create(
            ABILITY_TYPES_KEY, DimensionDelvers.MODID
    );

    /*
     * This is where we register the different "types" of abilities that can be created and configured using datapacks
     */
    public static final Supplier<MapCodec<? extends AbstractAbility>> BOOST_ABILITY_TYPE = ABILITY_TYPES.register(
            "boost", ()-> BoostAbility.CODEC);
    public static final Supplier<MapCodec<? extends AbstractAbility>> PROJECTILE_ABILITY_TYPE = ABILITY_TYPES.register(
            "projectile", ()-> ProjectileAbility.CODEC);
    public static final Supplier<MapCodec<? extends AbstractAbility>> MODIFIER_ABILITY_TYPE = ABILITY_TYPES.register(
            "apply_modifier", ()-> ApplyModifierAbility.CODEC);
}
