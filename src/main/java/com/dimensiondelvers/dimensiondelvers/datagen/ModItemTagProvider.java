package com.dimensiondelvers.dimensiondelvers.datagen;


import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

/* Handles Data Generation for Block Tags of the DimensionDelvers mod */
public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags, DimensionDelvers.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
    }
}