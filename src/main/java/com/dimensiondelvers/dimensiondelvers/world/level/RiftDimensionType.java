package com.dimensiondelvers.dimensiondelvers.world.level;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.DimensionType;

public class RiftDimensionType {
    public static final ResourceLocation RIFT_DIMENSION_RENDERER_KEY = DimensionDelvers.id("rift_dimension_renderer");
    public static final ResourceKey<DimensionType> RIFT_DIMENSION_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, DimensionDelvers.id("rift_dimension"));
}
