package com.wanderersoftherift.wotr.world.level.levelgen.processor;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.world.level.levelgen.processor.util.ProcessorUtil;
import com.wanderersoftherift.wotr.world.level.levelgen.processor.util.StructureRandomType;
import com.wanderersoftherift.wotr.world.level.levelgen.theme.LevelRiftThemeData;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.wanderersoftherift.wotr.init.ModProcessors.RIFT_THEME;

public class ThemeProcessor extends StructureProcessor {
    public static final MapCodec<ThemeProcessor> CODEC = MapCodec.unit(ThemeProcessor::new);

    private static final long SEED = 268431L;

    public ThemeProcessor() {
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos piecePos, BlockPos structurePos, StructureTemplate.StructureBlockInfo rawBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        List<StructureProcessor> processors = getThemeProcessors(world, structurePos);
        Iterator<StructureProcessor> iterator = processors.iterator();

        while (blockInfo != null && iterator.hasNext()) {
            blockInfo = iterator.next()
                    .process(world, piecePos, structurePos, rawBlockInfo, blockInfo, settings, template);
        }
        return blockInfo;
    }

    @Override
    public List<StructureTemplate.StructureBlockInfo> finalizeProcessing(ServerLevelAccessor serverLevel, BlockPos piecePos, BlockPos structurePos, List<StructureTemplate.StructureBlockInfo> originalBlockInfos, List<StructureTemplate.StructureBlockInfo> processedBlockInfos, StructurePlaceSettings settings) {
        List<StructureTemplate.StructureBlockInfo> result = processedBlockInfos;

        for (StructureProcessor structureprocessor : getThemeProcessors(serverLevel, structurePos)) {
            result = structureprocessor.finalizeProcessing(serverLevel, piecePos, structurePos, originalBlockInfos, result, settings);
        }

        return result;
    }

    private List<StructureProcessor> getThemeProcessors(LevelReader world, BlockPos structurePos) {
        if(world instanceof ServerLevel serverLevel) {
            LevelRiftThemeData riftThemeData = LevelRiftThemeData.getFromLevel(serverLevel);
            if(riftThemeData.getTheme() != null) {
                return riftThemeData.getTheme().value().processors().value().list();
            }
            return defaultThemeProcessors(serverLevel, structurePos);
        }
        return new ArrayList<>();
    }

    private List<StructureProcessor> defaultThemeProcessors(ServerLevel world, BlockPos structurePos) {
        Optional<Registry<StructureProcessorList>> registryReference = world.registryAccess().lookup(Registries.PROCESSOR_LIST);
        return registryReference.get().get(ResourceLocation.fromNamespaceAndPath(WanderersOfTheRift.MODID, "cave")).get().value().list();
    }

    private static @NotNull List<StructureProcessor> getRandomThemeProcessor(ServerLevel world, BlockPos structurePos) {
        Optional<Registry<StructureProcessorList>> registryReference = world.registryAccess().lookup(Registries.PROCESSOR_LIST);
        if (registryReference.isPresent()) {
            RandomSource random = ProcessorUtil.getRandom(StructureRandomType.STRUCTURE, structurePos, structurePos, structurePos, world, SEED);
            List<StructureProcessorList> processorLists = new ArrayList<>();
            processorLists.add(registryReference.get().get(ResourceLocation.fromNamespaceAndPath(WanderersOfTheRift.MODID, "forest")).get().value());
            processorLists.add(registryReference.get().get(ResourceLocation.fromNamespaceAndPath(WanderersOfTheRift.MODID, "cave")).get().value());
            return processorLists.get(random.nextInt(processorLists.size())).list();
        }
        return new ArrayList<>();
    }

    protected StructureProcessorType<?> getType() {
        return RIFT_THEME.get();
    }

}