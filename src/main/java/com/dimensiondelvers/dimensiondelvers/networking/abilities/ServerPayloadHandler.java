package com.dimensiondelvers.dimensiondelvers.networking.abilities;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.Registries.UpgradeRegistry;
import com.dimensiondelvers.dimensiondelvers.abilities.AbstractAbility;
import com.dimensiondelvers.dimensiondelvers.abilities.types.ToggleAbility;
import com.dimensiondelvers.dimensiondelvers.client.gui.menu.TestMenu;
import com.dimensiondelvers.dimensiondelvers.networking.data.ClaimUpgrade;
import com.dimensiondelvers.dimensiondelvers.networking.data.OpenUpgradeMenu;
import com.dimensiondelvers.dimensiondelvers.networking.data.UseAbility;
import com.dimensiondelvers.dimensiondelvers.upgrades.AbstractUpgrade;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.dimensiondelvers.dimensiondelvers.Registries.AbilityRegistry.ABILITY_REGISTRY;

public class ServerPayloadHandler {
    public static void handleAbilityOnServer(final UseAbility ability, final IPayloadContext context)
    {
        AbstractAbility abilility = ABILITY_REGISTRY.get(ResourceLocation.parse(ability.ability_location()));
        if(abilility == null)
        {
            DimensionDelvers.LOGGER.error("Invalid Ability Activated: " + ResourceLocation.parse(ability.ability_location()));
        }
        else
        {
            if(abilility instanceof ToggleAbility)
            {
                if(!((ToggleAbility)abilility).IsToggled(context.player()))
                {
                    abilility.OnActivate(context.player());
                }
                else
                {
                    abilility.onDeactivate(context.player());
                }

                if(abilility.CanPlayerUse(context.player())) ((ToggleAbility)abilility).Toggle(context.player());
            }
            else
            {
                abilility.OnActivate(context.player());
            }
        }
//        DimensionDelvers.LOGGER.info(ability.ability_location());
        //TODO summon arrow here
    }

    public static void handleUpgradeMenuOnServer(final OpenUpgradeMenu menu, final IPayloadContext context)
    {
        context.player().openMenu(new SimpleMenuProvider(
                (containerId, playerInventory, player) -> new TestMenu(containerId, playerInventory),
                Component.translatable("menu.title.examplemod.mymenu") //TODO uh change this lmao
        ));

    }

    public static void handleUpgradeOnServer(final ClaimUpgrade upgrade, final IPayloadContext context)
    {

        AbstractUpgrade abstractUpgrade = UpgradeRegistry.UPGRADE_REGISTRY.get(ResourceLocation.parse(upgrade.upgrade_location()));
        if(!abstractUpgrade.isUnlocked(context.player()))
        {
            abstractUpgrade.unlock(context.player());

        }
        else
        {
            abstractUpgrade.remove(context.player());
        }

    }
}
