package com.dimensiondelvers.dimensiondelvers.world.level.levelgen.processor.util;

import com.dimensiondelvers.dimensiondelvers.mixin.InvokerBlockBehaviour;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.minecraft.tags.BlockTags.AIR;
import static net.minecraft.world.level.block.Blocks.JIGSAW;


public class ProcessorUtil {
    public static final String NBT_FINAL_STATE = "final_state";

    public static RandomSource getRandom(StructureRandomType type, BlockPos blockPos, BlockPos piecePos, BlockPos structurePos, LevelReader world, long processorSeed) {
        RandomSource randomSource = RandomSource.create(getRandomSeed(type, blockPos, piecePos, structurePos, world, processorSeed));
        randomSource.consumeCount(3);
        return randomSource;
    }

    public static long getRandomSeed(StructureRandomType type, BlockPos blockPos, BlockPos piecePos, BlockPos structurePos, LevelReader world, long processorSeed) {
        switch (type) {
            case BLOCK:
                return getRandomSeed(blockPos, processorSeed);
            case PIECE:
                return getRandomSeed(piecePos, processorSeed);
            case STRUCTURE:
                return getRandomSeed(structurePos, processorSeed);
            case WORLD:
                return ((WorldGenLevel) world).getSeed() + processorSeed;
            default:
                throw new RuntimeException("Unknown random type: " + type.toString());
        }
    }

    public static long getRandomSeed(BlockPos pos, long processorSeed) {
        return pos == null ? Util.getMillis() + processorSeed : Mth.getSeed(pos) + processorSeed;
    }

    public static Block getRandomBlockFromBlockTag(TagKey<Block> tagKey, RandomSource random, List<ResourceLocation> exclusionList) {
        Optional<HolderSet.Named<Block>> tagHolders = BuiltInRegistries.BLOCK.get(tagKey);
        if (tagHolders.isPresent()) {
            List<Block> collect = tagHolders.get().stream()
                    .map(Holder::value)
                    .filter(block -> !exclusionList.contains(BuiltInRegistries.BLOCK.getKey(block)))
                    .toList();
            if (!collect.isEmpty()) {
                return collect.get(random.nextInt(collect.size()));
            }
        }
        return Blocks.AIR;
    }

    public static Block getRandomBlockFromItemTag(TagKey<Item> tagKey, RandomSource random, List<Block> exclusionList) {
        Optional<HolderSet.Named<Item>> tagHolders = BuiltInRegistries.ITEM.get(tagKey);
        if (tagHolders.isPresent()) {
            List<Block> collect = tagHolders.get().stream()
                    .map(Holder::value)
                    .filter(item -> item instanceof BlockItem)
                    .map(item -> ((BlockItem) item).getBlock())
                    .filter(block -> !exclusionList.contains(block))
                    .toList();
            if (!collect.isEmpty()) {
                return collect.get(random.nextInt(collect.size()));
            }
        }
        return Blocks.AIR;
    }

    /*public static Item getRandomItemFromTag(TagKey<Item> tag, RandomSource random, List<ResourceLocation> exclusionList){
        List<Item> resultList = ITEMS.tags().getTag(tag).stream().filter(item -> !exclusionList.contains(ITEMS.getKey(item))).collect(Collectors.toList());
        return resultList.get(random.nextInt(resultList.size()));
    }*/

    public static BlockState copyStairsState(BlockState blockState, Block newBlock) {
        BlockState newBlockState = newBlock.defaultBlockState();
        return copyStairsState(blockState, newBlockState);
    }

    public static @NotNull BlockState copyStairsState(BlockState blockState, BlockState newBlockState) {
        newBlockState = updateProperty(blockState, newBlockState, StairBlock.FACING);
        newBlockState = updateProperty(blockState, newBlockState, StairBlock.SHAPE);
        newBlockState = updateProperty(blockState, newBlockState, StairBlock.HALF);
        newBlockState = updateProperty(blockState, newBlockState, StairBlock.WATERLOGGED);
        return newBlockState;
    }

    public static BlockState copySlabState(BlockState blockState, Block newBlock) {
        BlockState newBlockState = newBlock.defaultBlockState();
        return copySlabState(blockState, newBlockState);
    }

    public static @NotNull BlockState copySlabState(BlockState blockState, BlockState newBlockState) {
        newBlockState = updateProperty(blockState, newBlockState, SlabBlock.TYPE);
        newBlockState = updateProperty(blockState, newBlockState, SlabBlock.WATERLOGGED);
        return newBlockState;
    }

    public static BlockState copyWallState(BlockState blockState, Block newBlock) {
        BlockState newBlockState = newBlock.defaultBlockState();
        return copyWallState(blockState, newBlockState);
    }

    public static @NotNull BlockState copyWallState(BlockState blockState, BlockState newBlockState) {
        newBlockState = updateProperty(blockState, newBlockState, WallBlock.UP);
        newBlockState = updateProperty(blockState, newBlockState, WallBlock.EAST_WALL);
        newBlockState = updateProperty(blockState, newBlockState, WallBlock.NORTH_WALL);
        newBlockState = updateProperty(blockState, newBlockState, WallBlock.SOUTH_WALL);
        newBlockState = updateProperty(blockState, newBlockState, WallBlock.WEST_WALL);
        newBlockState = updateProperty(blockState, newBlockState, WallBlock.WATERLOGGED);
        return newBlockState;
    }

    private static BlockState updateProperty(BlockState state, BlockState newState, Property property) {
        if (newState.hasProperty(property)) {
            return newState.setValue(property, state.getValue(property));
        }
        return newState;
    }

    public static Map<BlockPos, StructureTemplate.StructureBlockInfo> mapByPos(List<StructureTemplate.StructureBlockInfo> pieceBlocks) {
        return pieceBlocks.stream().collect(Collectors.toMap(StructureTemplate.StructureBlockInfo::pos, blockInfo -> blockInfo));
    }

    public static StructureTemplate.StructureBlockInfo getBlock(List<StructureTemplate.StructureBlockInfo> pieceBlocks, BlockPos pos) {
        return pieceBlocks.stream().filter(blockInfo -> blockInfo.pos().equals(pos)).findFirst().orElse(null);
    }

    /*public static boolean isAir(StructureTemplate.StructureBlockInfo blockinfo){
        if(blockinfo != null && blockinfo.state().is(JIGSAW)){
            Block block = BuiltInRegistries.BLOCKS.getValue(new ResourceLocation(blockinfo.nbt.getString(NBT_FINAL_STATE)));
            return block == null || block.defaultBlockState().isAir() ;
        }else {
            return blockinfo == null || blockinfo.state.is(AIR) || blockinfo.state.is(CAVE_AIR);
        }
    }*/

    public static boolean isSolid(StructureTemplate.StructureBlockInfo blockinfo){
        if(blockinfo != null && blockinfo.state().is(JIGSAW)){
            Block block = BuiltInRegistries.BLOCK.getValue(ResourceLocation.parse(blockinfo.nbt().getString(NBT_FINAL_STATE)));
            return block != null && !block.defaultBlockState().is(AIR) && !(block instanceof LiquidBlock);
        }else {
            return blockinfo != null && !blockinfo.state().is(AIR) && !(blockinfo.state().getBlock() instanceof LiquidBlock);
        }
    }

    public static boolean isFaceFull(StructureTemplate.StructureBlockInfo blockinfo, Direction direction) {
        if(blockinfo == null) return false;
        if (blockinfo.state().is(JIGSAW)) {
            Block block = BuiltInRegistries.BLOCK.getValue(ResourceLocation.parse(blockinfo.nbt().getString(NBT_FINAL_STATE)));
            return block != null && !blockinfo.state().is(AIR) && !(blockinfo.state().getBlock() instanceof LiquidBlock) &&
                    Block.isFaceFull(((InvokerBlockBehaviour) block).invokeGetShape(block.defaultBlockState(), null, blockinfo.pos(), CollisionContext.empty()), direction);
        } else {
            return !blockinfo.state().is(AIR) && !(blockinfo.state().getBlock() instanceof LiquidBlock) && Block.isFaceFull(((InvokerBlockBehaviour) blockinfo.state().getBlock()).invokeGetCollisionShape(blockinfo.state(), null, blockinfo.pos(), CollisionContext.empty()), direction);
        }
    }

    public static StructureTemplate.Palette getCurrentPalette(List<StructureTemplate.Palette> palettes) {
        int i = palettes.size();
        if (i == 0) {
            throw new IllegalStateException("No palettes");
        } else {
            return palettes.get(i - 1);
        }
    }

    public static StructureTemplate.StructureBlockInfo getBlockInfo(List<StructureTemplate.StructureBlockInfo> mapByPos, BlockPos pos) {
        BlockPos firstPos = mapByPos.getFirst().pos();
        BlockPos lastPos = mapByPos.getLast().pos();
        if(isPosBetween(pos, firstPos, lastPos)){
            return null;
        }
        int width = lastPos.getX() + 1 - firstPos.getX();
        int height = lastPos.getY() + 1 - firstPos.getY();
        int index = (pos.getX() - firstPos.getX()) + (pos.getY()- firstPos.getY()) * width + (pos.getZ()-firstPos.getZ()) * width * height;
        if(index < 0 || index >= mapByPos.size()){
            return null;
        }
        return mapByPos.get(index);
    }

    private static boolean isPosBetween(BlockPos pos, BlockPos firstPos, BlockPos lastPos) {
        return pos.getX() < firstPos.getX() || pos.getX() > lastPos.getX() ||
                pos.getY() < firstPos.getY() || pos.getY() > lastPos.getY() ||
                pos.getZ() < firstPos.getZ() || pos.getZ() > lastPos.getZ();
    }
}