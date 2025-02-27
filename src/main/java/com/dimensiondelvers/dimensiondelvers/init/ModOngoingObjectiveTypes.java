package com.dimensiondelvers.dimensiondelvers.init;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.rift.objective.AbstractObjective;
import com.dimensiondelvers.dimensiondelvers.rift.objective.types.StealthObjective;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class ModOngoingObjectiveTypes {
    public static final ResourceKey<Registry<MapCodec<? extends AbstractObjective>>> ONGOING_OBJECTIVE_TYPE_KEY = ResourceKey.createRegistryKey(DimensionDelvers.id("ongoing_objective_type"));

    public static final Registry<MapCodec<? extends AbstractObjective>> ONGOING_OBJECTIVE_TYPE_REGISTRY = new RegistryBuilder<>(ONGOING_OBJECTIVE_TYPE_KEY).create();
    // Creating the Deferred Register
    public static final DeferredRegister<MapCodec<? extends AbstractObjective>> ONGOING_OBJECTIVE_TYPES = DeferredRegister.create(
            ONGOING_OBJECTIVE_TYPE_KEY, DimensionDelvers.MODID
    );

    public static final Supplier<MapCodec<? extends AbstractObjective>> STEALTH_OBJECTIVE = ONGOING_OBJECTIVE_TYPES.register(
            "stealth", () -> StealthObjective.CODEC
    );
}
