package com.dimensiondelvers.dimensiondelvers.networking.abilities;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.Registries.AbilityRegistry;
import com.dimensiondelvers.dimensiondelvers.Registries.UpgradeRegistry;
import com.dimensiondelvers.dimensiondelvers.abilities.AbstractAbility;
import com.dimensiondelvers.dimensiondelvers.client.gui.menu.TestMenu;
import com.dimensiondelvers.dimensiondelvers.networking.data.ClaimUpgrade;
import com.dimensiondelvers.dimensiondelvers.networking.data.OpenUpgradeMenu;
import com.dimensiondelvers.dimensiondelvers.networking.data.UseAbility;
import com.dimensiondelvers.dimensiondelvers.upgrades.AbstractUpgrade;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

//import static com.dimensiondelvers.dimensiondelvers.Registries.AbilityRegistry.ABILITY_REGISTRY;

public class ServerPayloadHandler {
    public static void handleAbilityOnServer(final UseAbility ability, final IPayloadContext context)
    {
        Optional<Registry<AbstractAbility>> reg = context.player().getServer().registryAccess().lookup(AbilityRegistry.DATA_PACK_ABILITY_REG_KEY);
        if(reg.isPresent())
        {
            if(!reg.get().get(ResourceLocation.parse(ability.ability_location())).isPresent())
            {
                DimensionDelvers.LOGGER.error("Invalid Ability Activated: " + ResourceLocation.parse(ability.ability_location()));
            }
            else
            {

                AbstractAbility abilility = reg.get().get(ResourceLocation.parse(ability.ability_location())).get().value();
                if(abilility.IsToggle())
                {
                    if(!abilility.IsToggled(context.player()))
                    {
                        abilility.OnActivate(context.player());
                    }
                    else
                    {
                        abilility.onDeactivate(context.player());
                    }

                    if(abilility.CanPlayerUse(context.player())) abilility.Toggle(context.player());
                }
                else
                {
                    abilility.OnActivate(context.player());
                }
            }
        }
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
        AbstractUpgrade abstractUpgrade = UpgradeRegistry.UPGRADE_REGISTRY.get(ResourceLocation.parse(upgrade.upgrade_location())).get().value();
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
