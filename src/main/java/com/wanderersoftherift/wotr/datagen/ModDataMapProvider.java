package com.wanderersoftherift.wotr.datagen;

import com.wanderersoftherift.wotr.init.ModDataMaps;
import com.wanderersoftherift.wotr.init.ModEssenceTypes;
import com.wanderersoftherift.wotr.item.essence.EssenceValue;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.DataMapProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * Data gen for Data Maps - these are additional information that can be attached to registries, similar to tags but
 * with more content
 */
public class ModDataMapProvider extends DataMapProvider {
    public ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.@NotNull Provider provider) {
        // Core
        this.builder(ModDataMaps.ESSENCE_VALUE_DATA)
                .add(Tags.Items.FOODS_RAW_MEAT, new EssenceValue(ModEssenceTypes.MEAT.get(), 1), false)
                .add(Tags.Items.FOODS_COOKED_MEAT, new EssenceValue(ModEssenceTypes.MEAT.get(), 2), false)
                .add(Tags.Items.COBBLESTONES, new EssenceValue(ModEssenceTypes.EARTH.get(), 1), false)
                .add(Tags.Items.STONES, new EssenceValue(ModEssenceTypes.EARTH.get(), 2), false)
                .add(Tags.Items.STRIPPED_LOGS, new EssenceValue(ModEssenceTypes.LIFE.get(), 8), false)
                .add(Tags.Items.CROPS, new EssenceValue(ModEssenceTypes.LIFE.get(), 1), false)
                .build();


    }
}
