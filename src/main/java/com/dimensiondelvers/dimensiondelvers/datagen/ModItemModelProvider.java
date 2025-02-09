package com.dimensiondelvers.dimensiondelvers.datagen;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.client.ItemPropertyRegistry;
import com.dimensiondelvers.dimensiondelvers.init.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;


/* Handles Data Generation for Item models of the DimensionDelvers mod */
public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, DimensionDelvers.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.EXAMPLE_ITEM.get());
        basicItem(ModItems.DD_TWEAKER.get());


        ItemModelBuilder modelNormal = basicItem(ModItems.RUNEGEM.get());
        // Adding texture overrides via datagen
        modelNormal.override().predicate(ItemPropertyRegistry.RUNEGEM_PROPERTY, 0.0F).model(getGeneratedModel("circle")).end();
        modelNormal.override().predicate(ItemPropertyRegistry.RUNEGEM_PROPERTY, 0.1F).model(getGeneratedModel("square")).end();
        modelNormal.override().predicate(ItemPropertyRegistry.RUNEGEM_PROPERTY, 0.2F).model(getGeneratedModel("triangle")).end();
        modelNormal.override().predicate(ItemPropertyRegistry.RUNEGEM_PROPERTY, 0.3F).model(getGeneratedModel("diamond")).end();
        modelNormal.override().predicate(ItemPropertyRegistry.RUNEGEM_PROPERTY, 0.4F).model(getGeneratedModel("heart")).end();
        modelNormal.override().predicate(ItemPropertyRegistry.RUNEGEM_PROPERTY, 0.5F).model(getGeneratedModel("pentagon")).end();
    }

    /**
     * Generates the model file dynamically.
     */
    private ModelFile getGeneratedModel(String shape) {
        return withExistingParent("item/runegem/shape/" + shape, mcLoc("item/generated"))
                .texture("layer0", modLoc("item/runegem/shape/" + shape));
    }

}
