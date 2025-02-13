package com.dimensiondelvers.dimensiondelvers.init;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.world.level.levelgen.structure.templatesystem.AttachmentProcessor;
import com.dimensiondelvers.dimensiondelvers.world.level.levelgen.structure.templatesystem.GradientReplaceProcessor;
import com.dimensiondelvers.dimensiondelvers.world.level.levelgen.structure.templatesystem.MushroomProcessor;
import com.dimensiondelvers.dimensiondelvers.world.level.levelgen.structure.templatesystem.VineProcessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModProcessors {
    public static final DeferredRegister<StructureProcessorType<?>> PROCESSORS = DeferredRegister.create(Registries.STRUCTURE_PROCESSOR,DimensionDelvers.MODID);

    // Add VINES
    public static final Supplier<StructureProcessorType<GradientReplaceProcessor>> GRADIENT_SPOT_REPLACE = PROCESSORS.register("spot_gradient", () -> () -> GradientReplaceProcessor.CODEC);
    public static final Supplier<StructureProcessorType<AttachmentProcessor>> ATTACHMENT = PROCESSORS.register("attachment", () -> () -> AttachmentProcessor.CODEC);
    public static final Supplier<StructureProcessorType<VineProcessor>> VINES = PROCESSORS.register("vines", () -> () -> VineProcessor.CODEC);
    public static final Supplier<StructureProcessorType<MushroomProcessor>> MUSHROOMS = PROCESSORS.register("mushrooms", () -> () -> MushroomProcessor.CODEC);
}
