package com.wanderersoftherift.wotr.datagen;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.init.WotrBlocks;
import com.wanderersoftherift.wotr.init.WotrTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static net.minecraft.tags.BlockTags.FENCES;
import static net.minecraft.tags.BlockTags.FENCE_GATES;
import static net.minecraft.tags.BlockTags.SLABS;
import static net.minecraft.tags.BlockTags.STAIRS;
import static net.minecraft.tags.BlockTags.WALLS;

/**
 * Handles Data Generation for Block Tags of the Wotr mod
 */
public class WotrBlockTagProvider extends BlockTagsProvider {
    public WotrBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, WanderersOfTheRift.MODID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        // spotless:off
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(WotrBlocks.RUNE_ANVIL_ENTITY_BLOCK.get())
                .add(WotrBlocks.KEY_FORGE.get())
                .add(WotrBlocks.ABILITY_BENCH.get())
                .add(WotrBlocks.RIFT_SPAWNER.get())

                .add(WotrBlocks.WHITE_CONCRETE_SLAB.slab().get())
                .add(WotrBlocks.ORANGE_CONCRETE_SLAB.slab().get())
                .add(WotrBlocks.MAGENTA_CONCRETE_SLAB.slab().get())
                .add(WotrBlocks.LIGHT_BLUE_CONCRETE_SLAB.slab().get())
                .add(WotrBlocks.YELLOW_CONCRETE_SLAB.slab().get())
                .add(WotrBlocks.LIME_CONCRETE_SLAB.slab().get())
                .add(WotrBlocks.PINK_CONCRETE_SLAB.slab().get())
                .add(WotrBlocks.GRAY_CONCRETE_SLAB.slab().get())
                .add(WotrBlocks.LIGHT_GRAY_CONCRETE_SLAB.slab().get())
                .add(WotrBlocks.CYAN_CONCRETE_SLAB.slab().get())
                .add(WotrBlocks.PURPLE_CONCRETE_SLAB.slab().get())
                .add(WotrBlocks.BLUE_CONCRETE_SLAB.slab().get())
                .add(WotrBlocks.BROWN_CONCRETE_SLAB.slab().get())
                .add(WotrBlocks.GREEN_CONCRETE_SLAB.slab().get())
                .add(WotrBlocks.RED_CONCRETE_SLAB.slab().get())
                .add(WotrBlocks.BLACK_CONCRETE_SLAB.slab().get())

                .add(WotrBlocks.GLASS_SLAB.slab().get())
                .add(WotrBlocks.TINTED_GLASS_SLAB.slab().get())
                .add(WotrBlocks.WHITE_STAINED_GLASS_SLAB.slab().get())
                .add(WotrBlocks.ORANGE_STAINED_GLASS_SLAB.slab().get())
                .add(WotrBlocks.MAGENTA_STAINED_GLASS_SLAB.slab().get())
                .add(WotrBlocks.LIGHT_BLUE_STAINED_GLASS_SLAB.slab().get())
                .add(WotrBlocks.YELLOW_STAINED_GLASS_SLAB.slab().get())
                .add(WotrBlocks.LIME_STAINED_GLASS_SLAB.slab().get())
                .add(WotrBlocks.PINK_STAINED_GLASS_SLAB.slab().get())
                .add(WotrBlocks.GRAY_STAINED_GLASS_SLAB.slab().get())
                .add(WotrBlocks.LIGHT_GRAY_STAINED_GLASS_SLAB.slab().get())
                .add(WotrBlocks.CYAN_STAINED_GLASS_SLAB.slab().get())
                .add(WotrBlocks.PURPLE_STAINED_GLASS_SLAB.slab().get())
                .add(WotrBlocks.BLUE_STAINED_GLASS_SLAB.slab().get())
                .add(WotrBlocks.BROWN_STAINED_GLASS_SLAB.slab().get())
                .add(WotrBlocks.GREEN_STAINED_GLASS_SLAB.slab().get())
                .add(WotrBlocks.RED_STAINED_GLASS_SLAB.slab().get())
                .add(WotrBlocks.BLACK_STAINED_GLASS_SLAB.slab().get())

                .add(WotrBlocks.TUBE_CORAL_BLOCK_SLAB.slab().get())
                .add(WotrBlocks.BRAIN_CORAL_BLOCK_SLAB.slab().get())
                .add(WotrBlocks.BUBBLE_CORAL_BLOCK_SLAB.slab().get())
                .add(WotrBlocks.FIRE_CORAL_BLOCK_SLAB.slab().get())
                .add(WotrBlocks.HORN_CORAL_BLOCK_SLAB.slab().get())
                .add(WotrBlocks.GOLD_BLOCK_SLAB.slab().get())
                .add(WotrBlocks.IRON_BLOCK_SLAB.slab().get())
                .add(WotrBlocks.RAW_IRON_BLOCK_SLAB.slab().get())
                .add(WotrBlocks.RAW_GOLD_BLOCK_SLAB.slab().get())
                .add(WotrBlocks.PACKED_ICE_SLAB.slab().get())
                .add(WotrBlocks.ICE_SLAB.slab().get())
                .add(WotrBlocks.BLUE_ICE_SLAB.slab().get())
                .add(WotrBlocks.GILDED_BLACKSTONE_SLAB.slab().get())
                .add(WotrBlocks.BONE_BLOCK_SLAB.slab().get())
                .add(WotrBlocks.CLAY_SLAB.slab().get())
                .add(WotrBlocks.RESIN_BLOCK_SLAB.slab().get());

        tag(WotrTags.Blocks.BANNED_IN_RIFT)
                .add(WotrBlocks.RIFT_SPAWNER.get())
                .add(WotrBlocks.ABILITY_BENCH.get());

        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(WotrBlocks.NOGRAVGRAVEL.get())
                .add(WotrBlocks.NOGRAVSAND.get())
                .add(WotrBlocks.NOGRAVREDSAND.get())
                .add(WotrBlocks.NOGRAVWHITECONCRETEPOWDER.get())
                .add(WotrBlocks.NOGRAVORANGECONCRETEPOWDER.get())
                .add(WotrBlocks.NOGRAVLIGHTBLUECONCRETEPOWDER.get())
                .add(WotrBlocks.NOGRAVYELLOWCONCRETEPOWDER.get())
                .add(WotrBlocks.NOGRAVLIMECONCRETEPOWDER.get())
                .add(WotrBlocks.NOGRAVPINKCONCRETEPOWDER.get())
                .add(WotrBlocks.NOGRAVGRAYCONCRETEPOWDER.get())
                .add(WotrBlocks.NOGRAVLIGHTGRAYCONCRETEPOWDER.get())
                .add(WotrBlocks.NOGRAVCYANCONCRETEPOWDER.get())
                .add(WotrBlocks.NOGRAVPURPLECONCRETEPOWDER.get())
                .add(WotrBlocks.NOGRAVBLUECONCRETEPOWDER.get())
                .add(WotrBlocks.NOGRAVBROWNCONCRETEPOWDER.get())
                .add(WotrBlocks.NOGRAVGREENCONCRETEPOWDER.get())
                .add(WotrBlocks.NOGRAVREDCONCRETEPOWDER.get())
                .add(WotrBlocks.NOGRAVBLACKCONCRETEPOWDER.get())

                .add(WotrBlocks.DIRT_SLAB.slab().get())
                .add(WotrBlocks.COARSE_DIRT_SLAB.slab().get())
                .add(WotrBlocks.ROOTED_DIRT_SLAB.slab().get())
                .add(WotrBlocks.PODZOL_SLAB.slab().get())
                .add(WotrBlocks.MYCELIUM_SLAB.slab().get())
                .add(WotrBlocks.DIRT_PATH_SLAB.slab().get())
                .add(WotrBlocks.SOUL_SAND_SLAB.slab().get())
                .add(WotrBlocks.SOUL_SOIL_SLAB.slab().get())
                .add(WotrBlocks.MUD_SLAB.slab().get())
                .add(WotrBlocks.MUDDY_MANGROVE_ROOTS_SLAB.slab().get())

                .add(WotrBlocks.WHITE_CONCRETE_POWDER_SLAB.slab().get())
                .add(WotrBlocks.ORANGE_CONCRETE_POWDER_SLAB.slab().get())
                .add(WotrBlocks.MAGENTA_CONCRETE_POWDER_SLAB.slab().get())
                .add(WotrBlocks.LIGHT_BLUE_CONCRETE_POWDER_SLAB.slab().get())
                .add(WotrBlocks.YELLOW_CONCRETE_POWDER_SLAB.slab().get())
                .add(WotrBlocks.LIME_CONCRETE_POWDER_SLAB.slab().get())
                .add(WotrBlocks.PINK_CONCRETE_POWDER_SLAB.slab().get())
                .add(WotrBlocks.GRAY_CONCRETE_POWDER_SLAB.slab().get())
                .add(WotrBlocks.LIGHT_GRAY_CONCRETE_POWDER_SLAB.slab().get())
                .add(WotrBlocks.CYAN_CONCRETE_POWDER_SLAB.slab().get())
                .add(WotrBlocks.PURPLE_CONCRETE_POWDER_SLAB.slab().get())
                .add(WotrBlocks.BLUE_CONCRETE_POWDER_SLAB.slab().get())
                .add(WotrBlocks.BROWN_CONCRETE_POWDER_SLAB.slab().get())
                .add(WotrBlocks.GREEN_CONCRETE_POWDER_SLAB.slab().get())
                .add(WotrBlocks.RED_CONCRETE_POWDER_SLAB.slab().get())
                .add(WotrBlocks.BLACK_CONCRETE_POWDER_SLAB.slab().get());

        tag(BlockTags.DIRT)
                .add(WotrBlocks.DIRT_SLAB.slab().get())
                .add(WotrBlocks.DIRT_PATH_SLAB.slab().get())
                .add(WotrBlocks.COARSE_DIRT_SLAB.slab().get())
                .add(WotrBlocks.ROOTED_DIRT_SLAB.slab().get())
                .add(WotrBlocks.PODZOL_SLAB.slab().get())
                .add(WotrBlocks.MYCELIUM_SLAB.slab().get());

        tag(BlockTags.SAND)
                .add(WotrBlocks.NOGRAVSAND.get())
                .add(WotrBlocks.NOGRAVREDSAND.get());

        tag(BlockTags.BAMBOO_PLANTABLE_ON)
                .add(WotrBlocks.NOGRAVGRAVEL.get());

        tag(BlockTags.SOUL_SPEED_BLOCKS)
                .add(WotrBlocks.SOUL_SAND_SLAB.slab().get())
                .add(WotrBlocks.SOUL_SOIL_SLAB.slab().get());

        tag(BlockTags.SOUL_FIRE_BASE_BLOCKS)
                .add(WotrBlocks.SOUL_SAND_SLAB.slab().get())
                .add(WotrBlocks.SOUL_SOIL_SLAB.slab().get());

        tag(BlockTags.ICE)
                .add(WotrBlocks.ICE_SLAB.slab().get())
                .add(WotrBlocks.PACKED_ICE_SLAB.slab().get())
                .add(WotrBlocks.BLUE_ICE_SLAB.slab().get());

        tag(BlockTags.SLABS)
                .add(getAllSlabBlocks());

        // spotless:on

        WotrBlocks.BLOCK_FAMILY_HELPERS.forEach(family -> {
            if (family.getVariant(BlockFamily.Variant.STAIRS) != null) {
                tag(STAIRS).add(family.getVariant(BlockFamily.Variant.STAIRS).get());
            }
            if (family.getVariant(BlockFamily.Variant.SLAB) != null) {
                tag(SLABS).add(family.getVariant(BlockFamily.Variant.SLAB).get());
            }
            if (family.getVariant(BlockFamily.Variant.WALL) != null) {
                tag(WALLS).add(family.getVariant(BlockFamily.Variant.WALL).get());
            }
            if (family.getVariant(BlockFamily.Variant.FENCE) != null) {
                tag(FENCES).add(family.getVariant(BlockFamily.Variant.FENCE).get());
            }
            if (family.getVariant(BlockFamily.Variant.FENCE_GATE) != null) {
                tag(FENCE_GATES).add(family.getVariant(BlockFamily.Variant.FENCE_GATE).get());
            }
        });
    }

    private Block[] getAllSlabBlocks() {
        return Stream.of(
                WotrBlocks.REGISTERED_STANDARD_SLABS.values().stream(),
                WotrBlocks.REGISTERED_GLASS_SLABS.values().stream(),
                WotrBlocks.REGISTERED_DIRECTIONAL_SLABS.values().stream(),
                WotrBlocks.REGISTERED_TRIMM_SLABS.values().stream()
        ).flatMap(stream -> stream).map(slabInfo -> slabInfo.slab().get()).toArray(Block[]::new);
    }

}
