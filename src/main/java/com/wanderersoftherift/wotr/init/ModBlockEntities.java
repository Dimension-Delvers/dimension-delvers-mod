package com.wanderersoftherift.wotr.init;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.block.blockentity.RiftChestBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, WanderersOfTheRift.MODID);

    public static final Supplier<BlockEntityType<RiftChestBlockEntity>> RIFT_CHEST = BLOCK_ENTITIES.register(
            "rift_chest",
            () -> new BlockEntityType<>(
                    RiftChestBlockEntity::new,
                    ModBlocks.RIFT_CHEST.get()
            )
    );
}
