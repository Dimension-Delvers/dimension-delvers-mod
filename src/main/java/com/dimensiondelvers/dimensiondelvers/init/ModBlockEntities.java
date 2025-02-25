package com.dimensiondelvers.dimensiondelvers.init;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.block.blockentity.EssenceExtractorBlockEntity;
import com.dimensiondelvers.dimensiondelvers.block.blockentity.RiftChestBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, DimensionDelvers.MODID);

    public static final Supplier<BlockEntityType<RiftChestBlockEntity>> RIFT_CHEST = BLOCK_ENTITIES.register(
            "rift_chest",
            () -> new BlockEntityType<>(
                    RiftChestBlockEntity::new,
                    ModBlocks.RIFT_CHEST.get()
            )
    );

    public static final Supplier<BlockEntityType<EssenceExtractorBlockEntity>> ESSENCE_EXTRACTOR = BLOCK_ENTITIES.register(
            "essence_extractor",
            () -> new BlockEntityType<>(
                    EssenceExtractorBlockEntity::new,
                    ModBlocks.ESSENCE_EXTRACTOR.get()
            )
    );
}
