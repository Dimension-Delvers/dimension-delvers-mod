package com.dimensiondelvers.dimensiondelvers.init;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.block.RuneAnvilBlock;
import com.dimensiondelvers.dimensiondelvers.block.SpringBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(DimensionDelvers.MODID);

    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock(
            "example_block", // Name
            BlockBehaviour.Properties.of() // Properties to use.
    );

    public static final DeferredBlock<Block> DEV_BLOCK = BLOCKS.register(
            "dev_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .destroyTime(-1F)
                    .explosionResistance(3600000F)
                    .sound(SoundType.STONE)
                    .lightLevel(state -> 7)
            )
    );

    public static final DeferredBlock<RuneAnvilBlock> RUNE_ANVIL_BLOCK = BLOCKS.register(
            "rune_anvil",
            () -> new RuneAnvilBlock(BlockBehaviour.Properties.of()
                    .strength(2.5F)
                    .sound(SoundType.METAL)
            )
    );

    public static final DeferredBlock<SpringBlock> SPRING_BLOCK = BLOCKS.register(
            "spring_block",
            () -> new SpringBlock(BlockBehaviour.Properties.of()
                    .strength(2.0F)
                    .sound(SoundType.WOOD)
            )
    );
}
