package com.wanderersoftherift.wotr.init;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.block.AbilityBenchBlock;
import com.wanderersoftherift.wotr.block.BlockFamilyHelper;
import com.wanderersoftherift.wotr.block.DittoBlock;
import com.wanderersoftherift.wotr.block.KeyForgeBlock;
import com.wanderersoftherift.wotr.block.MobTrapBlock;
import com.wanderersoftherift.wotr.block.PlayerTrapBlock;
import com.wanderersoftherift.wotr.block.RiftChestEntityBlock;
import com.wanderersoftherift.wotr.block.RiftMobSpawnerBlock;
import com.wanderersoftherift.wotr.block.RiftSpawnerBlock;
import com.wanderersoftherift.wotr.block.RuneAnvilEntityBlock;
import com.wanderersoftherift.wotr.block.SpringBlock;
import com.wanderersoftherift.wotr.block.TrapBlock;
import com.wanderersoftherift.wotr.item.RiftChestType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.wanderersoftherift.wotr.block.BlockFamilyHelper.BUTTON_SUFFIX;
import static com.wanderersoftherift.wotr.block.BlockFamilyHelper.DIRECTIONAL_PILLAR_SUFFIX;
import static com.wanderersoftherift.wotr.block.BlockFamilyHelper.FENCE_GATE_SUFFIX;
import static com.wanderersoftherift.wotr.block.BlockFamilyHelper.FENCE_SUFFIX;
import static com.wanderersoftherift.wotr.block.BlockFamilyHelper.GLASS_BLOCK_SUFFIX;
import static com.wanderersoftherift.wotr.block.BlockFamilyHelper.PANE_SUFFIX;
import static com.wanderersoftherift.wotr.block.BlockFamilyHelper.PLATE_SUFFIX;
import static com.wanderersoftherift.wotr.block.BlockFamilyHelper.SLAB_SUFFIX;
import static com.wanderersoftherift.wotr.block.BlockFamilyHelper.STAIRS_SUFFIX;
import static com.wanderersoftherift.wotr.block.BlockFamilyHelper.TRAPDOOR_SUFFIX;
import static com.wanderersoftherift.wotr.block.BlockFamilyHelper.WALL_SUFFIX;
import static net.minecraft.world.level.block.state.properties.WoodType.OAK;

public class WotrBlocks {

    public static class SlabInfo {
        private final DeferredBlock<SlabBlock> slab;
        private final Block baseBlock;

        public SlabInfo(DeferredBlock<SlabBlock> slab, Block baseBlock) {
            this.slab = slab;
            this.baseBlock = baseBlock;
        }

        public DeferredBlock<SlabBlock> slab() {
            return slab;
        }

        public Block baseBlock() {
            return baseBlock;
        }

        public Item getBaseItem() {
            return baseBlock.asItem();
        }
    }

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(WanderersOfTheRift.MODID);
    public static final Map<RiftChestType, DeferredBlock<Block>> CHEST_TYPES = new HashMap<>();
    public static final List<BlockFamilyHelper> BLOCK_FAMILY_HELPERS = new ArrayList<>();

    public static final DeferredBlock<RuneAnvilEntityBlock> RUNE_ANVIL_ENTITY_BLOCK = registerBlock("rune_anvil",
            () -> new RuneAnvilEntityBlock(
                    BlockBehaviour.Properties.of().setId(blockId("rune_anvil")).strength(2.5F).sound(SoundType.METAL)));

    public static final DeferredBlock<RiftChestEntityBlock> RIFT_CHEST = registerChestBlock("rift_chest",
            () -> new RiftChestEntityBlock(WotrBlockEntities.RIFT_CHEST::get,
                    BlockBehaviour.Properties.of().setId(blockId("rift_chest")).strength(1.5F).sound(SoundType.WOOD)),
            RiftChestType.WOODEN);

    public static final DeferredBlock<RiftMobSpawnerBlock> RIFT_MOB_SPAWNER = registerBlock("rift_mob_spawner",
            () -> new RiftMobSpawnerBlock(BlockBehaviour.Properties.of()
                    .setId(blockId("rift_mob_spawner"))
                    .mapColor(MapColor.STONE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .lightLevel(blockState -> blockState.getValue(RiftMobSpawnerBlock.STATE).lightLevel())
                    .strength(50.0F)
                    .sound(SoundType.TRIAL_SPAWNER)
                    .isViewBlocking((blockState, blockGetter, blockPos) -> false)
                    .noOcclusion()));

    public static final DeferredBlock<RiftSpawnerBlock> RIFT_SPAWNER = registerBlock("rift_spawner",
            () -> new RiftSpawnerBlock(BlockBehaviour.Properties.of()
                    .setId(blockId("rift_spawner"))
                    .strength(2.0f)
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<KeyForgeBlock> KEY_FORGE = registerBlock("key_forge", () -> new KeyForgeBlock(
            BlockBehaviour.Properties.of().setId(blockId("key_forge")).strength(2.0f).sound(SoundType.STONE)));

    public static final DeferredBlock<AbilityBenchBlock> ABILITY_BENCH = registerBlock("ability_bench",
            () -> new AbilityBenchBlock(BlockBehaviour.Properties.of()
                    .setId(blockId("ability_bench"))
                    .strength(2.0f)
                    .sound(SoundType.WOOD)));

    public static final DeferredBlock<DittoBlock> DITTO_BLOCK = registerBlock("ditto_block",
            () -> new DittoBlock(BlockBehaviour.Properties.of()
                    .setId(blockId("ditto_block"))
                    .strength(1.5F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<TrapBlock> TRAP_BLOCK = registerBlock("trap_block",
            () -> new TrapBlock(BlockBehaviour.Properties.of()
                    .setId(blockId("trap_block"))
                    .strength(1.5F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<PlayerTrapBlock> PLAYER_TRAP_BLOCK = registerBlock("player_trap_block",
            () -> new PlayerTrapBlock(BlockBehaviour.Properties.of()
                    .setId(blockId("player_trap_block"))
                    .strength(1.5F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<MobTrapBlock> MOB_TRAP_BLOCK = registerBlock("mob_trap_block",
            () -> new MobTrapBlock(BlockBehaviour.Properties.of()
                    .setId(blockId("mob_trap_block"))
                    .strength(1.5F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<SpringBlock> SPRING_BLOCK = registerBlock("spring_block",
            () -> new SpringBlock(BlockBehaviour.Properties.of()
                    .setId(blockId("spring_block"))
                    .strength(1.5F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final SlabInfo WHITE_CONCRETE_SLAB = registerSlabBlock("white_concrete_slab", Blocks.WHITE_CONCRETE);
    public static final SlabInfo ORANGE_CONCRETE_SLAB = registerSlabBlock("orange_concrete_slab",
            Blocks.ORANGE_CONCRETE);
    public static final SlabInfo MAGENTA_CONCRETE_SLAB = registerSlabBlock("magenta_concrete_slab",
            Blocks.MAGENTA_CONCRETE);
    public static final SlabInfo LIGHT_BLUE_CONCRETE_SLAB = registerSlabBlock("light_blue_concrete_slab",
            Blocks.LIGHT_BLUE_CONCRETE);
    public static final SlabInfo YELLOW_CONCRETE_SLAB = registerSlabBlock("yellow_concrete_slab",
            Blocks.YELLOW_CONCRETE);
    public static final SlabInfo LIME_CONCRETE_SLAB = registerSlabBlock("lime_concrete_slab", Blocks.LIME_CONCRETE);
    public static final SlabInfo PINK_CONCRETE_SLAB = registerSlabBlock("pink_concrete_slab", Blocks.PINK_CONCRETE);
    public static final SlabInfo GRAY_CONCRETE_SLAB = registerSlabBlock("gray_concrete_slab", Blocks.GRAY_CONCRETE);
    public static final SlabInfo LIGHT_GRAY_CONCRETE_SLAB = registerSlabBlock("light_gray_concrete_slab",
            Blocks.LIGHT_GRAY_CONCRETE);
    public static final SlabInfo CYAN_CONCRETE_SLAB = registerSlabBlock("cyan_concrete_slab", Blocks.CYAN_CONCRETE);
    public static final SlabInfo PURPLE_CONCRETE_SLAB = registerSlabBlock("purple_concrete_slab",
            Blocks.PURPLE_CONCRETE);
    public static final SlabInfo BLUE_CONCRETE_SLAB = registerSlabBlock("blue_concrete_slab", Blocks.BLUE_CONCRETE);
    public static final SlabInfo BROWN_CONCRETE_SLAB = registerSlabBlock("brown_concrete_slab", Blocks.BROWN_CONCRETE);
    public static final SlabInfo GREEN_CONCRETE_SLAB = registerSlabBlock("green_concrete_slab", Blocks.GREEN_CONCRETE);
    public static final SlabInfo RED_CONCRETE_SLAB = registerSlabBlock("red_concrete_slab", Blocks.RED_CONCRETE);
    public static final SlabInfo BLACK_CONCRETE_SLAB = registerSlabBlock("black_concrete_slab", Blocks.BLACK_CONCRETE);

    public static final SlabInfo WHITE_CONCRETE_POWDER_SLAB = registerSlabBlock("white_concrete_powder_slab",
            Blocks.WHITE_CONCRETE_POWDER);
    public static final SlabInfo ORANGE_CONCRETE_POWDER_SLAB = registerSlabBlock("orange_concrete_powder_slab",
            Blocks.ORANGE_CONCRETE_POWDER);
    public static final SlabInfo MAGENTA_CONCRETE_POWDER_SLAB = registerSlabBlock("magenta_concrete_powder_slab",
            Blocks.MAGENTA_CONCRETE_POWDER);
    public static final SlabInfo LIGHT_BLUE_CONCRETE_POWDER_SLAB = registerSlabBlock("light_blue_concrete_powder_slab",
            Blocks.LIGHT_BLUE_CONCRETE_POWDER);
    public static final SlabInfo YELLOW_CONCRETE_POWDER_SLAB = registerSlabBlock("yellow_concrete_powder_slab",
            Blocks.YELLOW_CONCRETE_POWDER);
    public static final SlabInfo LIME_CONCRETE_POWDER_SLAB = registerSlabBlock("lime_concrete_powder_slab",
            Blocks.LIME_CONCRETE_POWDER);
    public static final SlabInfo PINK_CONCRETE_POWDER_SLAB = registerSlabBlock("pink_concrete_powder_slab",
            Blocks.PINK_CONCRETE_POWDER);
    public static final SlabInfo GRAY_CONCRETE_POWDER_SLAB = registerSlabBlock("gray_concrete_powder_slab",
            Blocks.GRAY_CONCRETE_POWDER);
    public static final SlabInfo LIGHT_GRAY_CONCRETE_POWDER_SLAB = registerSlabBlock("light_gray_concrete_powder_slab",
            Blocks.LIGHT_GRAY_CONCRETE_POWDER);
    public static final SlabInfo CYAN_CONCRETE_POWDER_SLAB = registerSlabBlock("cyan_concrete_powder_slab",
            Blocks.CYAN_CONCRETE_POWDER);
    public static final SlabInfo PURPLE_CONCRETE_POWDER_SLAB = registerSlabBlock("purple_concrete_powder_slab",
            Blocks.PURPLE_CONCRETE_POWDER);
    public static final SlabInfo BLUE_CONCRETE_POWDER_SLAB = registerSlabBlock("blue_concrete_powder_slab",
            Blocks.BLUE_CONCRETE_POWDER);
    public static final SlabInfo BROWN_CONCRETE_POWDER_SLAB = registerSlabBlock("brown_concrete_powder_slab",
            Blocks.BROWN_CONCRETE_POWDER);
    public static final SlabInfo GREEN_CONCRETE_POWDER_SLAB = registerSlabBlock("green_concrete_powder_slab",
            Blocks.GREEN_CONCRETE_POWDER);
    public static final SlabInfo RED_CONCRETE_POWDER_SLAB = registerSlabBlock("red_concrete_powder_slab",
            Blocks.RED_CONCRETE_POWDER);
    public static final SlabInfo BLACK_CONCRETE_POWDER_SLAB = registerSlabBlock("black_concrete_powder_slab",
            Blocks.BLACK_CONCRETE_POWDER);

    public static final SlabInfo TUBE_CORAL_BLOCK_SLAB = registerSlabBlock("tube_coral_block_slab",
            Blocks.TUBE_CORAL_BLOCK);
    public static final SlabInfo BRAIN_CORAL_BLOCK_SLAB = registerSlabBlock("brain_coral_block_slab",
            Blocks.BRAIN_CORAL_BLOCK);
    public static final SlabInfo BUBBLE_CORAL_BLOCK_SLAB = registerSlabBlock("bubble_coral_block_slab",
            Blocks.BUBBLE_CORAL_BLOCK);
    public static final SlabInfo FIRE_CORAL_BLOCK_SLAB = registerSlabBlock("fire_coral_block_slab",
            Blocks.FIRE_CORAL_BLOCK);
    public static final SlabInfo HORN_CORAL_BLOCK_SLAB = registerSlabBlock("horn_coral_block_slab",
            Blocks.HORN_CORAL_BLOCK);

    public static final SlabInfo WARPED_WART_BLOCK_SLAB = registerSlabBlock("warped_wart_block_slab",
            Blocks.WARPED_WART_BLOCK);
    public static final SlabInfo SOUL_SAND_SLAB = registerSlabBlock("soul_sand_slab", Blocks.SOUL_SAND);
    public static final SlabInfo SOUL_SOIL_SLAB = registerSlabBlock("soul_soil_slab", Blocks.SOUL_SOIL);
    public static final SlabInfo ROOTED_DIRT_SLAB = registerSlabBlock("rooted_dirt_slab", Blocks.ROOTED_DIRT);
    public static final SlabInfo RAW_IRON_BLOCK_SLAB = registerSlabBlock("raw_iron_block_slab", Blocks.RAW_IRON_BLOCK);
    public static final SlabInfo RAW_GOLD_BLOCK_SLAB = registerSlabBlock("raw_gold_block_slab", Blocks.RAW_GOLD_BLOCK);
    public static final SlabInfo PALE_MOSS_BLOCK_SLAB = registerSlabBlock("pale_moss_block_slab",
            Blocks.PALE_MOSS_BLOCK);
    public static final SlabInfo PACKED_ICE_SLAB = registerSlabBlock("packed_ice_slab", Blocks.PACKED_ICE);
    public static final SlabInfo NETHER_WART_BLOCK_SLAB = registerSlabBlock("nether_wart_block_slab",
            Blocks.NETHER_WART_BLOCK);
    public static final SlabInfo MUD_SLAB = registerSlabBlock("mud_slab", Blocks.MUD);
    public static final SlabInfo MOSS_BLOCK_SLAB = registerSlabBlock("moss_block_slab", Blocks.MOSS_BLOCK);
    public static final SlabInfo ICE_SLAB = registerSlabBlock("ice_slab", Blocks.ICE);
    public static final SlabInfo GILDED_BLACKSTONE_SLAB = registerSlabBlock("gilded_blackstone_slab",
            Blocks.GILDED_BLACKSTONE);
    public static final SlabInfo DIRT_SLAB = registerSlabBlock("dirt_slab", Blocks.DIRT);
    public static final SlabInfo CLAY_SLAB = registerSlabBlock("clay_slab", Blocks.CLAY);
    public static final SlabInfo COARSE_DIRT_SLAB = registerSlabBlock("coarse_dirt_slab", Blocks.COARSE_DIRT);
    public static final SlabInfo BLUE_ICE_SLAB = registerSlabBlock("blue_ice_slab", Blocks.BLUE_ICE);
    public static final SlabInfo RESIN_BLOCK_SLAB = registerSlabBlock("resin_block_slab", Blocks.RESIN_BLOCK);

    public static final SlabInfo GLASS_SLAB = registerSlabBlock("glass_slab", Blocks.GLASS);
    public static final SlabInfo TINTED_GLASS_SLAB = registerSlabBlock("tinted_glass_slab", Blocks.TINTED_GLASS);
    public static final SlabInfo WHITE_STAINED_GLASS_SLAB = registerSlabBlock("white_stained_glass_slab",
            Blocks.WHITE_STAINED_GLASS);
    public static final SlabInfo ORANGE_STAINED_GLASS_SLAB = registerSlabBlock("orange_stained_glass_slab",
            Blocks.ORANGE_STAINED_GLASS);
    public static final SlabInfo MAGENTA_STAINED_GLASS_SLAB = registerSlabBlock("magenta_stained_glass_slab",
            Blocks.MAGENTA_STAINED_GLASS);
    public static final SlabInfo LIGHT_BLUE_STAINED_GLASS_SLAB = registerSlabBlock("light_blue_stained_glass_slab",
            Blocks.LIGHT_BLUE_STAINED_GLASS);
    public static final SlabInfo YELLOW_STAINED_GLASS_SLAB = registerSlabBlock("yellow_stained_glass_slab",
            Blocks.YELLOW_STAINED_GLASS);
    public static final SlabInfo LIME_STAINED_GLASS_SLAB = registerSlabBlock("lime_stained_glass_slab",
            Blocks.LIME_STAINED_GLASS);
    public static final SlabInfo PINK_STAINED_GLASS_SLAB = registerSlabBlock("pink_stained_glass_slab",
            Blocks.PINK_STAINED_GLASS);
    public static final SlabInfo GRAY_STAINED_GLASS_SLAB = registerSlabBlock("gray_stained_glass_slab",
            Blocks.GRAY_STAINED_GLASS);
    public static final SlabInfo LIGHT_GRAY_STAINED_GLASS_SLAB = registerSlabBlock("light_gray_stained_glass_slab",
            Blocks.LIGHT_GRAY_STAINED_GLASS);
    public static final SlabInfo CYAN_STAINED_GLASS_SLAB = registerSlabBlock("cyan_stained_glass_slab",
            Blocks.CYAN_STAINED_GLASS);
    public static final SlabInfo PURPLE_STAINED_GLASS_SLAB = registerSlabBlock("purple_stained_glass_slab",
            Blocks.PURPLE_STAINED_GLASS);
    public static final SlabInfo BLUE_STAINED_GLASS_SLAB = registerSlabBlock("blue_stained_glass_slab",
            Blocks.BLUE_STAINED_GLASS);
    public static final SlabInfo BROWN_STAINED_GLASS_SLAB = registerSlabBlock("brown_stained_glass_slab",
            Blocks.BROWN_STAINED_GLASS);
    public static final SlabInfo GREEN_STAINED_GLASS_SLAB = registerSlabBlock("green_stained_glass_slab",
            Blocks.GREEN_STAINED_GLASS);
    public static final SlabInfo RED_STAINED_GLASS_SLAB = registerSlabBlock("red_stained_glass_slab",
            Blocks.RED_STAINED_GLASS);
    public static final SlabInfo BLACK_STAINED_GLASS_SLAB = registerSlabBlock("black_stained_glass_slab",
            Blocks.BLACK_STAINED_GLASS);

    public static final SlabInfo PODZOL_SLAB = registerSlabBlock("podzol_slab", Blocks.PODZOL);
    public static final SlabInfo MUDDY_MANGROVE_ROOTS_SLAB = registerSlabBlock("muddy_mangrove_roots_slab",
            Blocks.MUDDY_MANGROVE_ROOTS);
    public static final SlabInfo MYCELIUM_SLAB = registerSlabBlock("mycelium_slab", Blocks.MYCELIUM);
    public static final SlabInfo DIRT_PATH_SLAB = registerSlabBlock("dirt_path_slab", Blocks.DIRT_PATH);
    public static final SlabInfo BONE_BLOCK_SLAB = registerSlabBlock("bone_block_slab", Blocks.BONE_BLOCK);

    public static final SlabInfo GOLD_BLOCK_SLAB = registerSlabBlock("gold_block_slab", Blocks.GOLD_BLOCK);
    public static final SlabInfo IRON_BLOCK_SLAB = registerSlabBlock("iron_block_slab", Blocks.IRON_BLOCK);

    public static final Map<String, SlabInfo> REGISTERED_STANDARD_SLABS = Map.ofEntries(
            Map.entry("white_concrete", WHITE_CONCRETE_SLAB), Map.entry("orange_concrete", ORANGE_CONCRETE_SLAB),
            Map.entry("magenta_concrete", MAGENTA_CONCRETE_SLAB),
            Map.entry("light_blue_concrete", LIGHT_BLUE_CONCRETE_SLAB),
            Map.entry("yellow_concrete", YELLOW_CONCRETE_SLAB), Map.entry("lime_concrete", LIME_CONCRETE_SLAB),
            Map.entry("pink_concrete", PINK_CONCRETE_SLAB), Map.entry("gray_concrete", GRAY_CONCRETE_SLAB),
            Map.entry("light_gray_concrete", LIGHT_GRAY_CONCRETE_SLAB), Map.entry("cyan_concrete", CYAN_CONCRETE_SLAB),
            Map.entry("purple_concrete", PURPLE_CONCRETE_SLAB), Map.entry("blue_concrete", BLUE_CONCRETE_SLAB),
            Map.entry("brown_concrete", BROWN_CONCRETE_SLAB), Map.entry("green_concrete", GREEN_CONCRETE_SLAB),
            Map.entry("red_concrete", RED_CONCRETE_SLAB), Map.entry("black_concrete", BLACK_CONCRETE_SLAB),
            Map.entry("white_concrete_powder", WHITE_CONCRETE_POWDER_SLAB),
            Map.entry("orange_concrete_powder", ORANGE_CONCRETE_POWDER_SLAB),
            Map.entry("magenta_concrete_powder", MAGENTA_CONCRETE_POWDER_SLAB),
            Map.entry("light_blue_concrete_powder", LIGHT_BLUE_CONCRETE_POWDER_SLAB),
            Map.entry("yellow_concrete_powder", YELLOW_CONCRETE_POWDER_SLAB),
            Map.entry("lime_concrete_powder", LIME_CONCRETE_POWDER_SLAB),
            Map.entry("pink_concrete_powder", PINK_CONCRETE_POWDER_SLAB),
            Map.entry("gray_concrete_powder", GRAY_CONCRETE_POWDER_SLAB),
            Map.entry("light_gray_concrete_powder", LIGHT_GRAY_CONCRETE_POWDER_SLAB),
            Map.entry("cyan_concrete_powder", CYAN_CONCRETE_POWDER_SLAB),
            Map.entry("purple_concrete_powder", PURPLE_CONCRETE_POWDER_SLAB),
            Map.entry("blue_concrete_powder", BLUE_CONCRETE_POWDER_SLAB),
            Map.entry("brown_concrete_powder", BROWN_CONCRETE_POWDER_SLAB),
            Map.entry("green_concrete_powder", GREEN_CONCRETE_POWDER_SLAB),
            Map.entry("red_concrete_powder", RED_CONCRETE_POWDER_SLAB),
            Map.entry("black_concrete_powder", BLACK_CONCRETE_POWDER_SLAB),
            Map.entry("tube_coral_block", TUBE_CORAL_BLOCK_SLAB),
            Map.entry("brain_coral_block", BRAIN_CORAL_BLOCK_SLAB),
            Map.entry("bubble_coral_block", BUBBLE_CORAL_BLOCK_SLAB),
            Map.entry("fire_coral_block", FIRE_CORAL_BLOCK_SLAB), Map.entry("horn_coral_block", HORN_CORAL_BLOCK_SLAB),
            Map.entry("warped_wart_block", WARPED_WART_BLOCK_SLAB), Map.entry("soul_sand", SOUL_SAND_SLAB),
            Map.entry("soul_soil", SOUL_SOIL_SLAB), Map.entry("rooted_dirt", ROOTED_DIRT_SLAB),
            Map.entry("raw_iron_block", RAW_IRON_BLOCK_SLAB), Map.entry("raw_gold_block", RAW_GOLD_BLOCK_SLAB),
            Map.entry("pale_moss_block", PALE_MOSS_BLOCK_SLAB), Map.entry("packed_ice", PACKED_ICE_SLAB),
            Map.entry("nether_wart_block", NETHER_WART_BLOCK_SLAB), Map.entry("mud", MUD_SLAB),
            Map.entry("moss_block", MOSS_BLOCK_SLAB), Map.entry("ice", ICE_SLAB),
            Map.entry("gilded_blackstone", GILDED_BLACKSTONE_SLAB), Map.entry("dirt", DIRT_SLAB),
            Map.entry("clay", CLAY_SLAB), Map.entry("coarse_dirt", COARSE_DIRT_SLAB),
            Map.entry("blue_ice", BLUE_ICE_SLAB), Map.entry("resin_block", RESIN_BLOCK_SLAB)
    );

    public static final Map<String, SlabInfo> REGISTERED_GLASS_SLABS = Map.ofEntries(
            Map.entry("glass", GLASS_SLAB), Map.entry("tinted_glass", TINTED_GLASS_SLAB),
            Map.entry("white_stained_glass", WHITE_STAINED_GLASS_SLAB),
            Map.entry("orange_stained_glass", ORANGE_STAINED_GLASS_SLAB),
            Map.entry("magenta_stained_glass", MAGENTA_STAINED_GLASS_SLAB),
            Map.entry("light_blue_stained_glass", LIGHT_BLUE_STAINED_GLASS_SLAB),
            Map.entry("yellow_stained_glass", YELLOW_STAINED_GLASS_SLAB),
            Map.entry("lime_stained_glass", LIME_STAINED_GLASS_SLAB),
            Map.entry("pink_stained_glass", PINK_STAINED_GLASS_SLAB),
            Map.entry("gray_stained_glass", GRAY_STAINED_GLASS_SLAB),
            Map.entry("light_gray_stained_glass", LIGHT_GRAY_STAINED_GLASS_SLAB),
            Map.entry("cyan_stained_glass", CYAN_STAINED_GLASS_SLAB),
            Map.entry("purple_stained_glass", PURPLE_STAINED_GLASS_SLAB),
            Map.entry("blue_stained_glass", BLUE_STAINED_GLASS_SLAB),
            Map.entry("brown_stained_glass", BROWN_STAINED_GLASS_SLAB),
            Map.entry("green_stained_glass", GREEN_STAINED_GLASS_SLAB),
            Map.entry("red_stained_glass", RED_STAINED_GLASS_SLAB),
            Map.entry("black_stained_glass", BLACK_STAINED_GLASS_SLAB)
    );

    public static final Map<String, SlabInfo> REGISTERED_DIRECTIONAL_SLABS = Map.ofEntries(
            Map.entry("podzol", PODZOL_SLAB), Map.entry("muddy_mangrove_roots", MUDDY_MANGROVE_ROOTS_SLAB),
            Map.entry("mycelium", MYCELIUM_SLAB), Map.entry("dirt_path", DIRT_PATH_SLAB),
            Map.entry("bone_block", BONE_BLOCK_SLAB)
    );

    public static final Map<String, SlabInfo> REGISTERED_TRIMM_SLABS = Map.ofEntries(
            Map.entry("gold_block", GOLD_BLOCK_SLAB), Map.entry("iron_block", IRON_BLOCK_SLAB)
    );

    public static final DeferredBlock<Block> NOGRAVGRAVEL = registerNoGrav("nograv_gravel", Blocks.GRAVEL);
    public static final DeferredBlock<Block> NOGRAVSAND = registerNoGrav("nograv_sand", Blocks.SAND);
    public static final DeferredBlock<Block> NOGRAVREDSAND = registerNoGrav("nograv_red_sand", Blocks.RED_SAND);
    public static final DeferredBlock<Block> NOGRAVWHITECONCRETEPOWDER = registerNoGrav("nograv_white_concrete_powder",
            Blocks.WHITE_CONCRETE_POWDER);
    public static final DeferredBlock<Block> NOGRAVORANGECONCRETEPOWDER = registerNoGrav(
            "nograv_orange_concrete_powder", Blocks.ORANGE_CONCRETE_POWDER);
    public static final DeferredBlock<Block> NOGRAVMAGENTACONCRETEPOWDER = registerNoGrav(
            "nograv_magenta_concrete_powder", Blocks.MAGENTA_CONCRETE_POWDER);
    public static final DeferredBlock<Block> NOGRAVLIGHTBLUECONCRETEPOWDER = registerNoGrav(
            "nograv_light_blue_concrete_powder", Blocks.LIGHT_BLUE_CONCRETE_POWDER);
    public static final DeferredBlock<Block> NOGRAVYELLOWCONCRETEPOWDER = registerNoGrav(
            "nograv_yellow_concrete_powder", Blocks.YELLOW_CONCRETE_POWDER);
    public static final DeferredBlock<Block> NOGRAVLIMECONCRETEPOWDER = registerNoGrav("nograv_lime_concrete_powder",
            Blocks.LIME_CONCRETE_POWDER);
    public static final DeferredBlock<Block> NOGRAVPINKCONCRETEPOWDER = registerNoGrav("nograv_pink_concrete_powder",
            Blocks.PINK_CONCRETE_POWDER);
    public static final DeferredBlock<Block> NOGRAVGRAYCONCRETEPOWDER = registerNoGrav("nograv_gray_concrete_powder",
            Blocks.GRAY_CONCRETE_POWDER);
    public static final DeferredBlock<Block> NOGRAVLIGHTGRAYCONCRETEPOWDER = registerNoGrav(
            "nograv_light_gray_concrete_powder", Blocks.LIGHT_GRAY_CONCRETE_POWDER);
    public static final DeferredBlock<Block> NOGRAVCYANCONCRETEPOWDER = registerNoGrav("nograv_cyan_concrete_powder",
            Blocks.CYAN_CONCRETE_POWDER);
    public static final DeferredBlock<Block> NOGRAVPURPLECONCRETEPOWDER = registerNoGrav(
            "nograv_purple_concrete_powder", Blocks.PURPLE_CONCRETE_POWDER);
    public static final DeferredBlock<Block> NOGRAVBLUECONCRETEPOWDER = registerNoGrav("nograv_blue_concrete_powder",
            Blocks.BLUE_CONCRETE_POWDER);
    public static final DeferredBlock<Block> NOGRAVBROWNCONCRETEPOWDER = registerNoGrav("nograv_brown_concrete_powder",
            Blocks.BROWN_CONCRETE_POWDER);
    public static final DeferredBlock<Block> NOGRAVGREENCONCRETEPOWDER = registerNoGrav("nograv_green_concrete_powder",
            Blocks.GREEN_CONCRETE_POWDER);
    public static final DeferredBlock<Block> NOGRAVREDCONCRETEPOWDER = registerNoGrav("nograv_red_concrete_powder",
            Blocks.RED_CONCRETE_POWDER);
    public static final DeferredBlock<Block> NOGRAVBLACKCONCRETEPOWDER = registerNoGrav("nograv_black_concrete_powder",
            Blocks.BLACK_CONCRETE_POWDER);

    public static final DeferredBlock<CarpetBlock> HAY_CARPET = registerCarpet("hay_carpet", Blocks.HAY_BLOCK);

    public static final BlockFamilyHelper PROCESSOR_BLOCK_1 = registerBuildingBlock("processor_block_1",
            () -> new Block(BlockBehaviour.Properties.of().setId(blockId("processor_block_1"))));
    public static final BlockFamilyHelper PROCESSOR_BLOCK_2 = registerBuildingBlock("processor_block_2",
            () -> new Block(BlockBehaviour.Properties.of().setId(blockId("processor_block_2"))));
    public static final BlockFamilyHelper PROCESSOR_BLOCK_3 = registerBuildingBlock("processor_block_3",
            () -> new Block(BlockBehaviour.Properties.of().setId(blockId("processor_block_3"))));
    public static final BlockFamilyHelper PROCESSOR_BLOCK_4 = registerBuildingBlock("processor_block_4",
            () -> new Block(BlockBehaviour.Properties.of().setId(blockId("processor_block_4"))));
    public static final BlockFamilyHelper PROCESSOR_BLOCK_5 = registerBuildingBlock("processor_block_5",
            () -> new Block(BlockBehaviour.Properties.of().setId(blockId("processor_block_5"))));
    public static final BlockFamilyHelper PROCESSOR_BLOCK_6 = registerBuildingBlock("processor_block_6",
            () -> new Block(BlockBehaviour.Properties.of().setId(blockId("processor_block_6"))));
    public static final BlockFamilyHelper PROCESSOR_BLOCK_7 = registerBuildingBlock("processor_block_7",
            () -> new Block(BlockBehaviour.Properties.of().setId(blockId("processor_block_7"))));
    public static final BlockFamilyHelper PROCESSOR_BLOCK_8 = registerBuildingBlock("processor_block_8",
            () -> new Block(BlockBehaviour.Properties.of().setId(blockId("processor_block_8"))));
    public static final BlockFamilyHelper PROCESSOR_BLOCK_9 = registerBuildingBlock("processor_block_9",
            () -> new Block(BlockBehaviour.Properties.of().setId(blockId("processor_block_9"))));
    public static final BlockFamilyHelper PROCESSOR_BLOCK_10 = registerBuildingBlock("processor_block_10",
            () -> new Block(BlockBehaviour.Properties.of().setId(blockId("processor_block_10"))));
    public static final BlockFamilyHelper PROCESSOR_BLOCK_11 = registerBuildingBlock("processor_block_11",
            () -> new Block(BlockBehaviour.Properties.of().setId(blockId("processor_block_11"))));
    public static final BlockFamilyHelper PROCESSOR_BLOCK_12 = registerBuildingBlock("processor_block_12",
            () -> new Block(BlockBehaviour.Properties.of().setId(blockId("processor_block_12"))));
    public static final BlockFamilyHelper PROCESSOR_BLOCK_13 = registerBuildingBlock("processor_block_13",
            () -> new Block(BlockBehaviour.Properties.of().setId(blockId("processor_block_13"))));
    public static final BlockFamilyHelper PROCESSOR_BLOCK_14 = registerBuildingBlock("processor_block_14",
            () -> new Block(BlockBehaviour.Properties.of().setId(blockId("processor_block_14"))));
    public static final BlockFamilyHelper PROCESSOR_BLOCK_15 = registerBuildingBlock("processor_block_15",
            () -> new Block(BlockBehaviour.Properties.of().setId(blockId("processor_block_15"))));

    private static BlockFamilyHelper registerBuildingBlock(String id, Supplier<Block> sup) {
        DeferredBlock<Block> block = registerDevBlock(id, sup);
        BlockFamilyHelper buildingBlockHelper = new BlockFamilyHelper.Builder().withBlockId(id)
                .withBlock(block)
                .withSlab(registerDevBlock(id + SLAB_SUFFIX, () -> new SlabBlock(
                        BlockBehaviour.Properties.ofFullCopy(block.get()).setId(blockId(id + SLAB_SUFFIX)))))
                .withStairs(registerDevBlock(id + STAIRS_SUFFIX,
                        () -> new StairBlock(block.get().defaultBlockState(),
                                BlockBehaviour.Properties.ofFullCopy(block.get()).setId(blockId(id + STAIRS_SUFFIX)))))
                .withButton(registerDevBlock(id + BUTTON_SUFFIX,
                        () -> new ButtonBlock(BlockSetType.STONE, 30,
                                BlockBehaviour.Properties.ofFullCopy(block.get())
                                        .noCollission()
                                        .strength(0.5F)
                                        .setId(blockId(id + BUTTON_SUFFIX)))))
                .withPressurePlate(registerDevBlock(id + PLATE_SUFFIX,
                        () -> new PressurePlateBlock(BlockSetType.STONE,
                                BlockBehaviour.Properties.ofFullCopy(block.get())
                                        .noCollission()
                                        .strength(0.5F)
                                        .setId(blockId(id + PLATE_SUFFIX)))))
                .withWall(registerDevBlock(id + WALL_SUFFIX, () -> new WallBlock(
                        BlockBehaviour.Properties.ofFullCopy(block.get()).setId(blockId(id + WALL_SUFFIX)))))
                .withFence(registerDevBlock(id + FENCE_SUFFIX, () -> new FenceBlock(
                        BlockBehaviour.Properties.ofFullCopy(block.get()).setId(blockId(id + FENCE_SUFFIX)))))
                .withFenceGate(registerDevBlock(id + FENCE_GATE_SUFFIX,
                        () -> new FenceGateBlock(OAK,
                                BlockBehaviour.Properties.ofFullCopy(block.get())
                                        .setId(blockId(id + FENCE_GATE_SUFFIX)))))
                .withTrapdoor(registerDevBlock(id + TRAPDOOR_SUFFIX,
                        () -> new TrapDoorBlock(BlockSetType.STONE,
                                BlockBehaviour.Properties.ofFullCopy(block.get())
                                        .setId(blockId(id + TRAPDOOR_SUFFIX)))))
                .withPane(
                        registerDevBlock(id + GLASS_BLOCK_SUFFIX,
                                () -> new StainedGlassBlock(DyeColor.WHITE,
                                        BlockBehaviour.Properties.ofFullCopy(block.get())
                                                .noOcclusion()
                                                .lightLevel((state) -> 0)
                                                .sound(SoundType.GLASS)
                                                .setId(blockId(id + GLASS_BLOCK_SUFFIX)))),
                        registerDevBlock(id + PANE_SUFFIX,
                                () -> new StainedGlassPaneBlock(DyeColor.WHITE,
                                        BlockBehaviour.Properties.ofFullCopy(block.get())
                                                .noOcclusion()
                                                .lightLevel((state) -> 0)
                                                .sound(SoundType.GLASS)
                                                .setId(blockId(id + PANE_SUFFIX)))))
                .withDirectionalPillar(registerDevBlock(id + DIRECTIONAL_PILLAR_SUFFIX,
                        () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(block.get())
                                .setId(blockId(id + DIRECTIONAL_PILLAR_SUFFIX))) {
                        }))
                .createBuildBlockHelper();
        BLOCK_FAMILY_HELPERS.add(buildingBlockHelper);
        return buildingBlockHelper;
    }

    private static <T extends Block> DeferredBlock<T> registerChestBlock(
            String riftChest,
            Supplier<T> supplier,
            RiftChestType riftChestType) {
        DeferredBlock<T> register = registerBlock(riftChest, supplier);
        CHEST_TYPES.put(riftChestType, (DeferredBlock<Block>) register);
        return register;
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String key, Supplier<T> sup) {
        DeferredBlock<T> register = BLOCKS.register(key, sup);
        WotrItems.registerSimpleBlockItem(key, register);
        return register;
    }

    private static <T extends Block> DeferredBlock<T> registerDevBlock(String key, Supplier<T> sup) {
        DeferredBlock<T> register = BLOCKS.register(key, sup);
        WotrItems.registerSimpleDevBlockItem(key, register);
        return register;
    }

    private static DeferredBlock<Block> registerNoGrav(String idString, Block original) {
        return registerBlock(idString, () -> new Block(
                BlockBehaviour.Properties.ofFullCopy(original).setId(blockId(idString))
        ));
    }

    private static SlabInfo registerSlabBlock(String id, Block baseBlock) {
        DeferredBlock<SlabBlock> slab = registerDevBlock(id,
                () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(baseBlock).setId(blockId(id))));
        return new SlabInfo(slab, baseBlock);
    }

    private static DeferredBlock<CarpetBlock> registerCarpet(String id, Block baseBlock) {
        return registerDevBlock(id, () -> new CarpetBlock(
                BlockBehaviour.Properties.ofFullCopy(baseBlock).setId(blockId(id))
        ));
    }

    private static ResourceKey<Block> blockId(String name) {
        return ResourceKey.create(Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(WanderersOfTheRift.MODID, name));
    }
}
