package com.dimensiondelvers.dimensiondelvers.datagen;

import com.dimensiondelvers.dimensiondelvers.init.ModBlocks;
import com.dimensiondelvers.dimensiondelvers.init.ModItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

/* Handles Data Generation for Recipes of the DimensionDelvers mod */
public class ModRecipeProvider extends RecipeProvider {

    // Construct the provider to run
    protected ModRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
    }

    @Override
    protected void buildRecipes() {
        HolderGetter<Item> getter = this.registries.lookupOrThrow(Registries.ITEM);
        ShapedRecipeBuilder.shaped(getter, RecipeCategory.MISC, ModBlocks.EXAMPLE_BLOCK.get(), 4)
                .pattern("BBB")
                .pattern("BBB")
                .pattern("BBB")
                .define('B', ModItems.EXAMPLE_ITEM.get())
                .unlockedBy("has_example_item", has(ModItems.EXAMPLE_ITEM))
                .save(this.output);

        ShapelessRecipeBuilder.shapeless(getter, RecipeCategory.MISC, ModItems.EXAMPLE_ITEM.get(), 9)
                .requires(ModBlocks.EXAMPLE_BLOCK)
                .unlockedBy("has_example_block", has(ModBlocks.EXAMPLE_BLOCK)).save(this.output);
    }

    // The runner to add to the data generator
    public static class Runner extends RecipeProvider.Runner {
        // Get the parameters from the `GatherDataEvent`s.
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
            return new ModRecipeProvider(provider, output);
        }

        @Override
        public String getName() {
            return "Dimension Delver's Recipes";
        }
    }
}