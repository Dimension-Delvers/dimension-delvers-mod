package com.dimensiondelvers.dimensiondelvers.networking.abilities;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.abilities.AbstractAbility;
import com.dimensiondelvers.dimensiondelvers.networking.data.UseAbility;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.dimensiondelvers.dimensiondelvers.Registries.AbilityRegistry.ABILITY_REGISTRY;

public class ServerPayloadHandler {
    public static void handleAbilityOnServer(final UseAbility ability, final IPayloadContext context)
    {
        AbstractAbility abil = ABILITY_REGISTRY.get(ResourceLocation.parse(ability.ability_location()));
        if(abil == null)
        {
            DimensionDelvers.LOGGER.info("NULL! " + ResourceLocation.parse(ability.ability_location()));
        }
        else
        {
            abil.OnActivate(context.player());
        }
        DimensionDelvers.LOGGER.info(ability.ability_location());
        //TODO summon arrow here
    }
}
