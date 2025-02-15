package com.dimensiondelvers.dimensiondelvers.world.level.levelgen.structure.templatesystem;

import com.dimensiondelvers.dimensiondelvers.util.OpenSimplex2F;
import com.dimensiondelvers.dimensiondelvers.world.level.levelgen.structure.templatesystem.util.ProcessorUtil;
import com.dimensiondelvers.dimensiondelvers.world.level.levelgen.structure.templatesystem.util.WeightedBlockstateEntry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dimensiondelvers.dimensiondelvers.init.ModProcessors.WEIGHTED_REPLACE;

public class WeightedReplaceProcessor extends StructureProcessor {
    public static final MapCodec<WeightedReplaceProcessor> CODEC = RecordCodecBuilder.mapCodec(builder ->
            builder.group(
                    WeightedBlockstateEntry.CODEC.listOf().fieldOf("gradient_list").forGetter(WeightedReplaceProcessor::getWeightList),
                    Codec.INT.optionalFieldOf("seed_adjustment", 0).forGetter(WeightedReplaceProcessor::getSeedAdjustment),
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("to_replace").forGetter(WeightedReplaceProcessor::getToReplace)
            ).apply(builder, WeightedReplaceProcessor::new));

    private final List<WeightedBlockstateEntry> weightList;
    private final int seedAdjustment;
    private final Block toReplace;

    protected static Map<Long, OpenSimplex2F> noiseGenSeeds = new HashMap<>();

    public WeightedReplaceProcessor(List<WeightedBlockstateEntry> weightList, int seedAdjustment, Block toReplace) {
        this.weightList = weightList;
        this.seedAdjustment = seedAdjustment;
        this.toReplace = toReplace;
    }

    public OpenSimplex2F getNoiseGen(long seed) {
        return noiseGenSeeds.computeIfAbsent(seed, OpenSimplex2F::new);
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos piecePos, BlockPos structurePos, StructureTemplate.StructureBlockInfo rawBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        BlockState blockstate = blockInfo.state();
        BlockPos blockPos = blockInfo.pos();
        if (!blockstate.is(toReplace)) {
            return blockInfo;
        }

        BlockState newBlockState = getReplacementBlock(blockPos, world);
        if (newBlockState == null) {
            return blockInfo;
        }

        if (blockstate.is(BlockTags.STAIRS)) {
            return new StructureTemplate.StructureBlockInfo(blockPos, ProcessorUtil.copyStairsState(blockstate, newBlockState), blockInfo.nbt());
        } else if (blockstate.is(BlockTags.SLABS)) {
            return new StructureTemplate.StructureBlockInfo(blockPos, ProcessorUtil.copySlabState(blockstate, newBlockState), blockInfo.nbt());
        } else if (blockstate.is(BlockTags.WALLS)) {
            return new StructureTemplate.StructureBlockInfo(blockPos, ProcessorUtil.copyWallState(blockstate, newBlockState), blockInfo.nbt());
        } else {
            return new StructureTemplate.StructureBlockInfo(blockPos, newBlockState, blockInfo.nbt());
        }
    }

    private BlockState getReplacementBlock(BlockPos blockPos, LevelReader world) {
        if (world instanceof WorldGenLevel worldGenLevel) {
            return WeightedRandom.getRandomItem(
                    ((WorldGenLevel) world).getRandom(),
                    weightList
            ).orElse(new WeightedBlockstateEntry(null, Weight.of(1))).getBlockState();
        }
        return null;
    }


    protected StructureProcessorType<?> getType() {
        return WEIGHTED_REPLACE.get();
    }

    public List<WeightedBlockstateEntry> getWeightList() {
        return weightList;
    }

    public int getSeedAdjustment() {
        return seedAdjustment;
    }

    public Block getToReplace() {
        return toReplace;
    }
}