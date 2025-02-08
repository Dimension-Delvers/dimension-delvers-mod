package com.dimensiondelvers.dimensiondelvers.datagen;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.init.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

/* Handles Data Generation for Blockstates of the DimensionDelvers mod */
public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, DimensionDelvers.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.DEV_BLOCK);
        blockWithItem(ModBlocks.EXAMPLE_BLOCK);
        blockWithItem(ModBlocks.RUNE_ANVIL_BLOCK);

        Block block = ModBlocks.SPRING_BLOCK.get();
        ModelFile model = models().cube(
                BuiltInRegistries.BLOCK.getKey(block).getPath(),
                modLoc("block/spring_block_bottom"),
                modLoc("block/spring_block_top"),
                modLoc("block/spring_block_side"),
                modLoc("block/spring_block_side"),
                modLoc("block/spring_block_side"),
                modLoc("block/spring_block_side")
        ).texture(
                "particle",
                modLoc("block/spring_block_bottom").getPath()
        );

        simpleBlockWithItem(block, model);
    }


    private void blockWithItem(DeferredBlock<?> block) {
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }
}
