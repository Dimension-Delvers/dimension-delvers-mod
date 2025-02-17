package com.dimensiondelvers.dimensiondelvers.init;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.world.level.PocRiftChunkGenerator;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModChunkGenerators {
    public static final DeferredRegister<MapCodec<? extends ChunkGenerator>>
        CHUNK_GENERATORS = DeferredRegister.create(Registries.CHUNK_GENERATOR, DimensionDelvers.MODID);

    public static final Supplier<MapCodec<PocRiftChunkGenerator>>
        POC_RIFT_GENERATOR = CHUNK_GENERATORS.register("poc_rift_generator", () -> PocRiftChunkGenerator.CODEC);


}
