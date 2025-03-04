package com.wanderersoftherift.wotr.datagen;

import com.wanderersoftherift.wotr.init.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    public ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.DEV_BLOCK.get());
        dropSelf(ModBlocks.RUNE_ANVIL_BLOCK.get());
        dropSelf(ModBlocks.SKILL_BENCH.get());
        ModBlocks.BLOCK_FAMILY_HELPERS.forEach(helper -> {
            dropSelf(helper.getBlock().get());
            helper.getVariants().forEach((variant, block) -> dropSelf(block.get()));
        });
        dropSelf(ModBlocks.RIFT_CHEST.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
