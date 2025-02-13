package com.dimensiondelvers.dimensiondelvers.world.level.levelgen.structure.templatesystem;

import com.dimensiondelvers.dimensiondelvers.init.ModProcessors;
import com.dimensiondelvers.dimensiondelvers.world.level.levelgen.structure.templatesystem.util.ProcessorUtil;
import com.dimensiondelvers.dimensiondelvers.world.level.levelgen.structure.templatesystem.util.StructureRandomType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.util.RandomSource;

import java.util.Map;
import java.util.stream.Collectors;

import static com.dimensiondelvers.dimensiondelvers.world.level.levelgen.structure.templatesystem.util.ProcessorUtil.mapByPos;
import static com.dimensiondelvers.dimensiondelvers.world.level.levelgen.structure.templatesystem.util.StructureRandomType.RANDOM_TYPE_CODEC;
import static net.minecraft.core.Direction.*;
import static net.minecraft.world.level.block.Blocks.VINE;
import static net.minecraft.world.level.block.VineBlock.PROPERTY_BY_DIRECTION;

public class VineProcessor extends StructureProcessor {
    public static final MapCodec<VineProcessor> CODEC = RecordCodecBuilder.mapCodec(builder ->
            builder.group(
                    Codec.BOOL.optionalFieldOf("attach_to_wall", true).forGetter(VineProcessor::isAttachToWall),
                    Codec.BOOL.optionalFieldOf("attach_to_ceiling", true).forGetter(VineProcessor::isAttachToCeiling),
                    Codec.FLOAT.fieldOf("rarity").forGetter(VineProcessor::getRarity),
                    RANDOM_TYPE_CODEC.optionalFieldOf("random_type", StructureRandomType.BLOCK).forGetter(VineProcessor::getStructureRandomType)
            ).apply(builder, VineProcessor::new));
    private static final long SEED = 8514174L;

    private final boolean attachToWall;
    private final boolean attachToCeiling;
    private final float rarity;
    private final StructureRandomType structureRandomType;

    public VineProcessor(boolean attachToWall, boolean attachToCeiling, float rarity, StructureRandomType structureRandomType) {
        this.attachToWall = attachToWall;
        this.attachToCeiling = attachToCeiling;
        this.rarity = rarity;
        this.structureRandomType = structureRandomType;
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos piecePos, BlockPos structurePos, StructureTemplate.StructureBlockInfo rawBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, StructureTemplate template) {
        return blockInfo;
    }

    @Override
    public List<StructureTemplate.StructureBlockInfo> finalizeProcessing(ServerLevelAccessor serverLevel, BlockPos offset, BlockPos pos, List<StructureTemplate.StructureBlockInfo> originalBlockInfos, List<StructureTemplate.StructureBlockInfo> processedBlockInfos, StructurePlaceSettings settings) {
        Map<BlockPos, StructureTemplate.StructureBlockInfo> mapByPos = mapByPos(processedBlockInfos);
        List<StructureTemplate.StructureBlockInfo> newBlockInfos = new ArrayList<>();
        for (StructureTemplate.StructureBlockInfo blockInfo : mapByPos.values()) {
            StructureTemplate.StructureBlockInfo newBlockInfo = processFinal(serverLevel, offset, pos, blockInfo, blockInfo, settings, mapByPos);
            newBlockInfos.add(newBlockInfo);
        }
        return newBlockInfos;
    }

    public StructureTemplate.StructureBlockInfo processFinal(LevelReader world, BlockPos piecePos, BlockPos structurePos, StructureTemplate.StructureBlockInfo rawBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, Map<BlockPos, StructureTemplate.StructureBlockInfo> mapByPos) {
        RandomSource random = ProcessorUtil.getRandom(structureRandomType, blockInfo.pos(), piecePos, structurePos, world, SEED);
        BlockState blockstate = blockInfo.state();
        BlockPos blockpos = blockInfo.pos();
        List<Direction> possibleDirections = new ArrayList<>();
        if (blockstate.isAir() && random.nextFloat() <= rarity) {
            if (attachToWall) {
                possibleDirections.addAll(Arrays.asList(NORTH, EAST, SOUTH, WEST));
            }
            if (attachToCeiling) {
                possibleDirections.add(UP);
            }
            possibleDirections = possibleDirections.stream().filter(direction -> isDirectionPossible(mapByPos, rawBlockInfo.pos(), direction)).collect(Collectors.toList());
        }
        if (possibleDirections.isEmpty()) {
            return blockInfo;
        } else {
            Direction direction = possibleDirections.get(random.nextInt(possibleDirections.size()));
            BooleanProperty property = PROPERTY_BY_DIRECTION.get(direction);
            return new StructureTemplate.StructureBlockInfo(blockpos, VINE.defaultBlockState().setValue(property, true), null);
        }
    }

    private boolean isDirectionPossible(Map<BlockPos, StructureTemplate.StructureBlockInfo> pieceBlocks, BlockPos pos, Direction direction) {
        StructureTemplate.StructureBlockInfo tempBlock = pieceBlocks.get(pos.relative(direction));
        return ProcessorUtil.isFaceFull(tempBlock, direction.getOpposite());
    }

    protected StructureProcessorType<?> getType() {
        return ModProcessors.VINES.get();
    }

    public boolean isAttachToWall() {
        return attachToWall;
    }

    public boolean isAttachToCeiling() {
        return attachToCeiling;
    }

    public float getRarity() {
        return rarity;
    }

    public StructureRandomType getStructureRandomType() {
        return structureRandomType;
    }
}