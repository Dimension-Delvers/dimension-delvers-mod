package com.dimensiondelvers.dimensiondelvers.datagen;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.init.ModAbilities;
import com.dimensiondelvers.dimensiondelvers.init.ModBlocks;
import com.dimensiondelvers.dimensiondelvers.init.ModItems;
import com.dimensiondelvers.dimensiondelvers.init.ModUpgrades;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

/* Handles Data Generation for I18n of the locale 'en_us' of the DimensionDelvers mod */
public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output) {
        super(output, DimensionDelvers.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        // Helpers are available for various common object types. Every helper has two variants: an add() variant
        // for the object itself, and an addTypeHere() variant that accepts a supplier for the object.
        // The different names for the supplier variants are required due to generic type erasure.
        // All following examples assume the existence of the values as suppliers of the needed type.
        // See https://docs.neoforged.net/docs/1.21.1/resources/client/i18n/ for translation of other types.

        // Adds a block translation.
        addBlock(ModBlocks.EXAMPLE_BLOCK, "Example Block");
        addBlock(ModBlocks.DEV_BLOCK, "Dev Block");

        // Adds an item translation.
        addItem(ModItems.EXAMPLE_ITEM, "Example Item");

        // Adds a generic translation
        add("itemGroup.dimensiondelvers", "Dimension Delvers");

        add(ModAbilities.BOOST_ABILITY.get().GetTranslationString(), "Boost Ability");
        add(ModAbilities.SUMMON_ARROW_ABILITY.get().GetTranslationString(), "Arrow Ability");
        add(ModAbilities.HEAL_ABILITY.get().GetTranslationString(), "Heal Ability");
        add(ModAbilities.ARMOR_STAND_ABILITY.get().GetTranslationString(), "Armorstand Ability");
        add(ModAbilities.BE_PRETTY.get().GetTranslationString(), "Pretty Ability");


        add(ModUpgrades.UNLOCK_BOOST.get().GetTranslationString(), "Unlock Boost");
        add("tooltip." + ModUpgrades.UNLOCK_BOOST.get().GetTranslationString(), "Press I to boost!");
        add(ModUpgrades.UNLOCK_ARROW.get().GetTranslationString(), "Unlock Arrow");
        add("tooltip." + ModUpgrades.UNLOCK_ARROW.get().GetTranslationString(), "Press O to shoot!");
        add(ModUpgrades.UNLOCK_HEAL.get().GetTranslationString(), "Unlock Heal");
        add("tooltip." + ModUpgrades.UNLOCK_HEAL.get().GetTranslationString(), "Press U to heal!");
        add(ModUpgrades.UNLOCK_ARMOR_STAND.get().GetTranslationString(), "Unlock Armorstand");
        add("tooltip." + ModUpgrades.UNLOCK_ARMOR_STAND.get().GetTranslationString(), "Press Y to toggle Armorstand!");
        add(ModUpgrades.UNLOCK_PRETTY.get().GetTranslationString(), "Unlock Beauty");
        add("tooltip." + ModUpgrades.UNLOCK_PRETTY.get().GetTranslationString(), "Press H to be temporarily pretty (Check F5)");
    }
}
