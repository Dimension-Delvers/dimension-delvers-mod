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
import java.util.Set;
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
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(WanderersOfTheRift.MODID);
    public static final Map<RiftChestType, DeferredBlock<Block>> CHEST_TYPES = new HashMap<>();
    public static final List<BlockFamilyHelper> BLOCK_FAMILY_HELPERS = new ArrayList<>();
    public static final Map<String, DeferredBlock<? extends Block>> REGISTERED_SLABS = new HashMap<>();
    public static final Map<String, DeferredBlock<? extends Block>> REGISTERED_DIRECTTONAL_SLABS = new HashMap<>();
    public static final Map<String, DeferredBlock<? extends Block>> REGISTERED_GLASS_SLABS = new HashMap<>();


    public static final Map<String, Block> SLAB_BASE_BLOCKS_STANDARD = Map.ofEntries(
            // Concrete
            Map.entry("white_concrete", Blocks.WHITE_CONCRETE),
            Map.entry("orange_concrete", Blocks.ORANGE_CONCRETE),
            Map.entry("magenta_concrete", Blocks.MAGENTA_CONCRETE),
            Map.entry("light_blue_concrete", Blocks.LIGHT_BLUE_CONCRETE),
            Map.entry("yellow_concrete", Blocks.YELLOW_CONCRETE),
            Map.entry("lime_concrete", Blocks.LIME_CONCRETE),
            Map.entry("pink_concrete", Blocks.PINK_CONCRETE),
            Map.entry("gray_concrete", Blocks.GRAY_CONCRETE),
            Map.entry("light_gray_concrete", Blocks.LIGHT_GRAY_CONCRETE),
            Map.entry("cyan_concrete", Blocks.CYAN_CONCRETE),
            Map.entry("purple_concrete", Blocks.PURPLE_CONCRETE),
            Map.entry("blue_concrete", Blocks.BLUE_CONCRETE),
            Map.entry("brown_concrete", Blocks.BROWN_CONCRETE),
            Map.entry("green_concrete", Blocks.GREEN_CONCRETE),
            Map.entry("red_concrete", Blocks.RED_CONCRETE),
            Map.entry("black_concrete", Blocks.BLACK_CONCRETE),

            // Concrete powder
            Map.entry("white_concrete_powder", Blocks.WHITE_CONCRETE_POWDER),
            Map.entry("orange_concrete_powder", Blocks.ORANGE_CONCRETE_POWDER),
            Map.entry("magenta_concrete_powder", Blocks.MAGENTA_CONCRETE_POWDER),
            Map.entry("light_blue_concrete_powder", Blocks.LIGHT_BLUE_CONCRETE_POWDER),
            Map.entry("yellow_concrete_powder", Blocks.YELLOW_CONCRETE_POWDER),
            Map.entry("lime_concrete_powder", Blocks.LIME_CONCRETE_POWDER),
            Map.entry("pink_concrete_powder", Blocks.PINK_CONCRETE_POWDER),
            Map.entry("gray_concrete_powder", Blocks.GRAY_CONCRETE_POWDER),
            Map.entry("light_gray_concrete_powder", Blocks.LIGHT_GRAY_CONCRETE_POWDER),
            Map.entry("cyan_concrete_powder", Blocks.CYAN_CONCRETE_POWDER),
            Map.entry("purple_concrete_powder", Blocks.PURPLE_CONCRETE_POWDER),
            Map.entry("blue_concrete_powder", Blocks.BLUE_CONCRETE_POWDER),
            Map.entry("brown_concrete_powder", Blocks.BROWN_CONCRETE_POWDER),
            Map.entry("green_concrete_powder", Blocks.GREEN_CONCRETE_POWDER),
            Map.entry("red_concrete_powder", Blocks.RED_CONCRETE_POWDER),
            Map.entry("black_concrete_powder", Blocks.BLACK_CONCRETE_POWDER),

            // Coral
            Map.entry("tube_coral_block", Blocks.TUBE_CORAL_BLOCK),
            Map.entry("brain_coral_block", Blocks.BRAIN_CORAL_BLOCK),
            Map.entry("bubble_coral_block", Blocks.BUBBLE_CORAL_BLOCK),
            Map.entry("fire_coral_block", Blocks.FIRE_CORAL_BLOCK),
            Map.entry("horn_coral_block", Blocks.HORN_CORAL_BLOCK),

            // Misc
            Map.entry("warped_wart_block", Blocks.WARPED_WART_BLOCK),
            Map.entry("snow", Blocks.SNOW),
            Map.entry("soul_sand", Blocks.SOUL_SAND),
            Map.entry("soul_soil", Blocks.SOUL_SOIL),
            Map.entry("rooted_dirt", Blocks.ROOTED_DIRT),
            Map.entry("raw_iron_block", Blocks.RAW_IRON_BLOCK),
            Map.entry("raw_gold_block", Blocks.RAW_GOLD_BLOCK),
            Map.entry("pale_moss_block", Blocks.PALE_MOSS_BLOCK),
            Map.entry("packed_ice", Blocks.PACKED_ICE),
            Map.entry("nether_wart_block", Blocks.NETHER_WART_BLOCK),
            Map.entry("mud", Blocks.MUD),
            Map.entry("moss_block", Blocks.MOSS_BLOCK),
            Map.entry("ice", Blocks.ICE),
            Map.entry("gold_block", Blocks.GOLD_BLOCK),
            Map.entry("iron_block", Blocks.IRON_BLOCK),
            Map.entry("gilded_blackstone", Blocks.GILDED_BLACKSTONE),
            Map.entry("dirt", Blocks.DIRT),
            Map.entry("clay", Blocks.CLAY),
            Map.entry("coarse_dirt", Blocks.COARSE_DIRT),
            Map.entry("blue_ice", Blocks.BLUE_ICE),
            Map.entry("resin_block", Blocks.RESIN_BLOCK)
    );

    public static final Map<String, Block> SLAB_BASE_BLOCKS_DIRECTIONAL = Map.ofEntries(
            Map.entry("podzol", Blocks.PODZOL),
            Map.entry("muddy_mangrove_roots", Blocks.MUDDY_MANGROVE_ROOTS),
            Map.entry("mycelium", Blocks.MYCELIUM),
            Map.entry("dirt_path", Blocks.DIRT_PATH),
            Map.entry("dried_kelp_block", Blocks.DRIED_KELP_BLOCK),
            Map.entry("bone_block", Blocks.BONE_BLOCK)
    );

    public static final Map<String, Block> SLAB_BASE_BLOCKS_GLASS = Map.ofEntries(
            Map.entry("glass", Blocks.GLASS),
            Map.entry("white_stained_glass", Blocks.WHITE_STAINED_GLASS),
            Map.entry("orange_stained_glass", Blocks.ORANGE_STAINED_GLASS),
            Map.entry("magenta_stained_glass", Blocks.MAGENTA_STAINED_GLASS),
            Map.entry("light_blue_stained_glass", Blocks.LIGHT_BLUE_STAINED_GLASS),
            Map.entry("yellow_stained_glass", Blocks.YELLOW_STAINED_GLASS),
            Map.entry("lime_stained_glass", Blocks.LIME_STAINED_GLASS),
            Map.entry("pink_stained_glass", Blocks.PINK_STAINED_GLASS),
            Map.entry("gray_stained_glass", Blocks.GRAY_STAINED_GLASS),
            Map.entry("light_gray_stained_glass", Blocks.LIGHT_GRAY_STAINED_GLASS),
            Map.entry("cyan_stained_glass", Blocks.CYAN_STAINED_GLASS),
            Map.entry("purple_stained_glass", Blocks.PURPLE_STAINED_GLASS),
            Map.entry("blue_stained_glass", Blocks.BLUE_STAINED_GLASS),
            Map.entry("brown_stained_glass", Blocks.BROWN_STAINED_GLASS),
            Map.entry("green_stained_glass", Blocks.GREEN_STAINED_GLASS),
            Map.entry("red_stained_glass", Blocks.RED_STAINED_GLASS),
            Map.entry("black_stained_glass", Blocks.BLACK_STAINED_GLASS),
            Map.entry("tinted_glass", Blocks.TINTED_GLASS)
    );

    static {
        SLAB_BASE_BLOCKS_STANDARD.forEach((id, base) -> {
            registerSlab(id + "_slab", base);
        });

        SLAB_BASE_BLOCKS_DIRECTIONAL.forEach((id, base) -> {
            registerDirectionalSlab(id + "_slab", base);
        });

        SLAB_BASE_BLOCKS_GLASS.forEach((id, base) -> {
            registerGlassSlab(id + "_slab", base);
        });
    }

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

    public static final DeferredBlock<CarpetBlock> HAY_CARPET =
            registerCarpet("hay_carpet", Blocks.HAY_BLOCK);

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

    //same texture on all sides
    private static DeferredBlock<SlabBlock> registerSlab(String id, Block baseBlock) {
        DeferredBlock<SlabBlock> slab = registerDevBlock(id, () -> new SlabBlock(
                BlockBehaviour.Properties.ofFullCopy(baseBlock).setId(blockId(id))
        ));
        String key = id.replace("_slab", "");
        REGISTERED_SLABS.put(key, slab);
        return slab;
    }

    //different textures on side top and bot
    private static DeferredBlock<SlabBlock> registerDirectionalSlab(String id, Block baseBlock) {
        DeferredBlock<SlabBlock> slab = registerDevBlock(id, () -> new SlabBlock(
                BlockBehaviour.Properties.ofFullCopy(baseBlock).setId(blockId(id))
        ));
        String key = id.replace("_slab", "");
        REGISTERED_DIRECTTONAL_SLABS.put(key, slab);
        return slab;
    }

    //different textures on side top and bot
    private static DeferredBlock<SlabBlock> registerGlassSlab(String id, Block baseBlock) {
        DeferredBlock<SlabBlock> slab = registerDevBlock(id, () -> new SlabBlock(
                BlockBehaviour.Properties.ofFullCopy(baseBlock).setId(blockId(id))
        ));
        String key = id.replace("_slab", "");
        REGISTERED_GLASS_SLABS.put(key, slab);
        return slab;
    }

    private static DeferredBlock<StairBlock> registerStairs(String id, Block baseBlock) {
        return registerDevBlock(id, () -> new StairBlock(
                baseBlock.defaultBlockState(),
                BlockBehaviour.Properties.ofFullCopy(baseBlock).setId(blockId(id))
        ));
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
