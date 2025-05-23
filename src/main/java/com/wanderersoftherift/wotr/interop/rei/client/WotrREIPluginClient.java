package com.wanderersoftherift.wotr.interop.rei.client;

import com.wanderersoftherift.wotr.init.WotrBlocks;
import com.wanderersoftherift.wotr.init.WotrDataComponentType;
import com.wanderersoftherift.wotr.init.WotrItems;
import com.wanderersoftherift.wotr.init.WotrRegistries;
import com.wanderersoftherift.wotr.item.runegem.RunegemData;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;

@REIPluginClient
public class WotrREIPluginClient implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new KeyForgeDisplayCategory());
        registry.addWorkstations(WotrDisplayCategories.KEY_FORGE, EntryStacks.of(WotrBlocks.KEY_FORGE.toStack()));
        registry.add(new RuneAnvilDisplayCategory());
        registry.addWorkstations(WotrDisplayCategories.RUNE_ANVIL,
                EntryStacks.of(WotrBlocks.RUNE_ANVIL_ENTITY_BLOCK.toStack()));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        Registry<RunegemData> runegemDataRegistry = Minecraft.getInstance().level.registryAccess()
                .lookupOrThrow(WotrRegistries.Keys.RUNEGEM_DATA);
        for (RunegemData runegemData : runegemDataRegistry) {
            ItemStack runegem = WotrItems.RUNEGEM.toStack();
            runegem.set(WotrDataComponentType.RUNEGEM_DATA, runegemData);
            for (RunegemData.ModifierGroup modifierGroup : runegemData.modifierLists()) {
                registry.add(new RuneAnvilDisplay(runegem, modifierGroup));
            }
        }
    }

}
