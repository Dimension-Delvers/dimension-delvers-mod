package com.wanderersoftherift.wotr.datagen;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.init.WotrBlocks;
import com.wanderersoftherift.wotr.init.WotrDataComponentType;
import com.wanderersoftherift.wotr.init.WotrItems;
import com.wanderersoftherift.wotr.init.WotrRegistries;
import com.wanderersoftherift.wotr.item.crafting.EssencePredicate;
import com.wanderersoftherift.wotr.item.crafting.KeyForgeRecipe;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/* Handles Data Generation for Recipes of the Wotr mod */
public class WotrRecipeProvider extends RecipeProvider {

    // Construct the provider to run
    protected WotrRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
    }

    @Override
    protected void buildRecipes() {
        HolderGetter<Item> getter = this.registries.lookupOrThrow(Registries.ITEM);

        ShapedRecipeBuilder.shaped(getter, RecipeCategory.MISC, WotrBlocks.RIFT_SPAWNER.asItem())
                .pattern("sss")
                .pattern("sEs")
                .pattern("sss")
                .define('s', Items.STONE)
                .define('E', Items.ENDER_PEARL)
                .unlockedBy("has_ender_pearl", this.has(Items.ENDER_PEARL))
                .save(this.output);

        ShapedRecipeBuilder.shaped(getter, RecipeCategory.MISC, WotrBlocks.KEY_FORGE.asItem())
                .pattern("   ")
                .pattern("PI ")
                .pattern("BL ")
                .define('L', ItemTags.LOGS)
                .define('I', Items.IRON_BLOCK)
                .define('B', Items.BLAST_FURNACE)
                .define('P', Items.FLOWER_POT)
                .unlockedBy("has_blast_furnace", this.has(Items.BLAST_FURNACE))
                .save(this.output);

        ShapedRecipeBuilder.shaped(getter, RecipeCategory.MISC, WotrBlocks.ABILITY_BENCH.asItem())
                .pattern("ggg")
                .pattern("w w")
                .pattern("w w")
                .define('g', Items.GLASS)
                .define('w', ItemTags.PLANKS)
                .unlockedBy("has_glass", this.has(Items.GLASS))
                .unlockedBy("has_plank", this.has(ItemTags.PLANKS))
                .save(this.output);

        ShapedRecipeBuilder.shaped(getter, RecipeCategory.MISC, WotrBlocks.RUNE_ANVIL_ENTITY_BLOCK.asItem())
                .pattern(" e ")
                .pattern("eae")
                .pattern(" e ")
                .define('a', ItemTags.ANVIL)
                .define('e', Items.EMERALD)
                .unlockedBy("has_rune", this.has(WotrItems.RUNEGEM))
                .save(this.output);

        ItemStack dodgeSkillGem = WotrItems.ABILITY_HOLDER.toStack();
        dodgeSkillGem.applyComponents(DataComponentPatch.builder()
                .set(WotrDataComponentType.ABILITY.get(),
                        DeferredHolder.create(WotrRegistries.Keys.ABILITIES, WanderersOfTheRift.id("dash")))
                .build());
        ShapedRecipeBuilder.shaped(getter, RecipeCategory.MISC, dodgeSkillGem)
                .pattern("ggg")
                .pattern("gIg")
                .pattern("ggg")
                .define('g', Blocks.GLASS_PANE.asItem())
                .define('I', ItemTags.FOOT_ARMOR)
                .unlockedBy("has_glass_pane", this.has(Blocks.GLASS_PANE.asItem()))
                .save(this.output, "wotr:ability_dash");

        ItemStack fireballSkillGem = WotrItems.ABILITY_HOLDER.toStack();
        fireballSkillGem.applyComponents(DataComponentPatch.builder()
                .set(WotrDataComponentType.ABILITY.get(),
                        DeferredHolder.create(WotrRegistries.Keys.ABILITIES, WanderersOfTheRift.id("fireball")))
                .build());
        ShapedRecipeBuilder.shaped(getter, RecipeCategory.MISC, fireballSkillGem)
                .pattern("ggg")
                .pattern("gIg")
                .pattern("ggg")
                .define('g', Blocks.GLASS_PANE.asItem())
                .define('I', Items.FLINT_AND_STEEL)
                .unlockedBy("has_glass_pane", this.has(Blocks.GLASS_PANE.asItem()))
                .save(this.output, "wotr:ability_fireball");

        ItemStack healSkillGem = WotrItems.ABILITY_HOLDER.toStack();
        healSkillGem.applyComponents(DataComponentPatch.builder()
                .set(WotrDataComponentType.ABILITY.get(),
                        DeferredHolder.create(WotrRegistries.Keys.ABILITIES, WanderersOfTheRift.id("heal")))
                .build());
        ShapedRecipeBuilder.shaped(getter, RecipeCategory.MISC, healSkillGem)
                .pattern("ggg")
                .pattern("gIg")
                .pattern("ggg")
                .define('g', Blocks.GLASS_PANE.asItem())
                .define('I', Items.APPLE)
                .unlockedBy("has_glass_pane", this.has(Blocks.GLASS_PANE.asItem()))
                .save(this.output, "wotr:ability_heal");

        KeyForgeRecipe
                .create(WotrDataComponentType.RIFT_THEME.get(),
                        DeferredHolder.create(WotrRegistries.Keys.RIFT_THEMES, WanderersOfTheRift.id("cave")))
                .setPriority(-1)
                .save(output, WanderersOfTheRift.id("rift_theme_cave"));

        KeyForgeRecipe
                .create(WotrDataComponentType.RIFT_THEME.get(),
                        DeferredHolder.create(WotrRegistries.Keys.RIFT_THEMES, WanderersOfTheRift.id("forest")))
                .withEssenceReq(new EssencePredicate.Builder(WanderersOfTheRift.id("plant")).setMinPercent(50f).build())
                .save(output, WanderersOfTheRift.id("rift_theme_forest"));

        KeyForgeRecipe
                .create(WotrDataComponentType.RIFT_THEME.get(),
                        DeferredHolder.create(WotrRegistries.Keys.RIFT_THEMES, WanderersOfTheRift.id("processor")))
                .withEssenceReq(
                        new EssencePredicate.Builder(WanderersOfTheRift.id("processor")).setMinPercent(1f).build())
                .save(output, WanderersOfTheRift.id("rift_theme_processor"));

        KeyForgeRecipe
                .create(WotrDataComponentType.RIFT_THEME.get(),
                        DeferredHolder.create(WotrRegistries.Keys.RIFT_THEMES, WanderersOfTheRift.id("mushroom")))
                .withEssenceReq(
                        new EssencePredicate.Builder(WanderersOfTheRift.id("mushroom")).setMinPercent(50f).build())
                .setPriority(10)
                .save(output, WanderersOfTheRift.id("rift_theme_mushroom"));

        KeyForgeRecipe
                .create(WotrDataComponentType.RIFT_THEME.get(),
                        DeferredHolder.create(WotrRegistries.Keys.RIFT_THEMES, WanderersOfTheRift.id("nether")))
                .withEssenceReq(
                        new EssencePredicate.Builder(WanderersOfTheRift.id("nether")).setMinPercent(50f).build())
                .setPriority(10)
                .save(output, WanderersOfTheRift.id("rift_theme_nether"));

        KeyForgeRecipe
                .create(WotrDataComponentType.RIFT_THEME.get(),
                        DeferredHolder.create(WotrRegistries.Keys.RIFT_THEMES, WanderersOfTheRift.id("noir")))
                .withEssenceReq(new EssencePredicate.Builder(WanderersOfTheRift.id("light")).setMinPercent(50f).build())
                .setPriority(10)
                .save(output, WanderersOfTheRift.id("rift_theme_noir"));

        KeyForgeRecipe
                .create(WotrDataComponentType.RIFT_THEME.get(),
                        DeferredHolder.create(WotrRegistries.Keys.RIFT_THEMES, WanderersOfTheRift.id("buzzy_bees")))
                .withEssenceReq(
                        new EssencePredicate.Builder(WanderersOfTheRift.id("life")).setMinPercent(25F).build())
                .withEssenceReq(
                        new EssencePredicate.Builder(WanderersOfTheRift.id("water")).setMinPercent(5F).build())
                .withEssenceReq(
                        new EssencePredicate.Builder(WanderersOfTheRift.id("order")).setMinPercent(5F).build())
                .withEssenceReq(
                        new EssencePredicate.Builder(WanderersOfTheRift.id("plant")).setMinPercent(15F).build())
                .setPriority(10)
                .save(output, WanderersOfTheRift.id("rift_theme_buzzy_bees"));

        KeyForgeRecipe
                .create(WotrDataComponentType.RIFT_OBJECTIVE.get(),
                        DeferredHolder.create(WotrRegistries.Keys.OBJECTIVES, WanderersOfTheRift.id("kill")))
                .setPriority(-1)
                .save(output, WanderersOfTheRift.id("rift_objective_kill"));

        KeyForgeRecipe
                .create(WotrDataComponentType.RIFT_OBJECTIVE.get(),
                        DeferredHolder.create(WotrRegistries.Keys.OBJECTIVES, WanderersOfTheRift.id("stealth")))
                .setPriority(1)
                .withEssenceReq(new EssencePredicate.Builder(WanderersOfTheRift.id("dark")).setMinPercent(5f).build())
                .save(output, WanderersOfTheRift.id("rift_objective_stealth"));

    }

    // The runner to add to the data generator
    public static class Runner extends RecipeProvider.Runner {
        // Get the parameters from the `GatherDataEvent`s.
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(
                HolderLookup.@NotNull Provider provider,
                @NotNull RecipeOutput output) {
            return new WotrRecipeProvider(provider, output);
        }

        @Override
        public @NotNull String getName() {
            return "Wanderers of the Rift's Recipes";
        }
    }
}
