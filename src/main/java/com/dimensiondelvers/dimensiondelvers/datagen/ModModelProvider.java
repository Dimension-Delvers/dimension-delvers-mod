package com.dimensiondelvers.dimensiondelvers.datagen;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.block.BlockFamilyHelper;
import com.dimensiondelvers.dimensiondelvers.client.render.item.properties.select.SelectRuneGemShape;
import com.dimensiondelvers.dimensiondelvers.init.ModBlocks;
import com.dimensiondelvers.dimensiondelvers.init.ModItems;
import com.dimensiondelvers.dimensiondelvers.item.runegem.RuneGemShape;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, DimensionDelvers.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, @NotNull ItemModelGenerators itemModels) {
        blockModels.createTrivialCube(ModBlocks.RUNE_ANVIL_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.DEV_BLOCK.get());

        itemModels.generateFlatItem(ModItems.EXAMPLE_ITEM.get(), ModelTemplates.FLAT_ITEM);

        this.generateRunegemItem(ModItems.RUNEGEM.get(), itemModels);

        ModBlocks.BUILD_BLOCK_HELPERS.forEach(helper -> createModelsForBuildBlock(helper, blockModels, itemModels));
    }

    private void createModelsForBuildBlock(BlockFamilyHelper helper, BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.family(helper.getBlock().get()).generateFor(helper.getFamily());
    }

    public void generateRunegemItem(Item item, ItemModelGenerators itemModels) {
        ResourceLocation modelLocation = ModelLocationUtils.getModelLocation(item);
        ResourceLocation shapeLocation = ResourceLocation.fromNamespaceAndPath(DimensionDelvers.MODID, "item/runegem/shape/");
        List<SelectItemModel.SwitchCase<RuneGemShape>> list = new ArrayList<>(RuneGemShape.values().length);
        for (RuneGemShape shape : RuneGemShape.values()) {
            ItemModel.Unbaked model = ItemModelUtils.plainModel(createRuneGemShapeModel(shapeLocation.withSuffix(shape.getName()), ModItems.RUNEGEM.get(), shape.getName(), ModelTemplates.FLAT_ITEM, itemModels));
            list.add(ItemModelUtils.when(shape, model));
        }
        itemModels.itemModelOutput.accept(item, ItemModelUtils.select(new SelectRuneGemShape(), ItemModelUtils.plainModel(modelLocation), list));
    }

    private static ResourceLocation createRuneGemShapeModel(ResourceLocation location, Item item, String suffix, ModelTemplate modelTemplate, ItemModelGenerators itemModels) {
        return modelTemplate.create(location, TextureMapping.layer0(location), itemModels.modelOutput);
    }

}
