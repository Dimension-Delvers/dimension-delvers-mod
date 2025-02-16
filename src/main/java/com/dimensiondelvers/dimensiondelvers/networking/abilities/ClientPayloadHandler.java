package com.dimensiondelvers.dimensiondelvers.networking.abilities;

import com.dimensiondelvers.dimensiondelvers.abilities.Serializable.PlayerCooldownData;
import com.dimensiondelvers.dimensiondelvers.init.ModAbilities;
import com.dimensiondelvers.dimensiondelvers.networking.data.CooldownActivated;
import com.dimensiondelvers.dimensiondelvers.networking.data.ToggleState;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {
    public static void handleCooldownOnClient(final CooldownActivated cooldownActivated, final IPayloadContext context)
    {
        PlayerCooldownData cooldowns = context.player().getData(ModAbilities.COOL_DOWNS);
        cooldowns.setCooldown(ResourceLocation.parse(cooldownActivated.ability_location()), cooldownActivated.cooldownLength());
        context.player().setData(ModAbilities.COOL_DOWNS, cooldowns);
    }

    public static void handleToggleOnClient(final ToggleState toggle, final IPayloadContext context)
    {
//        context.player().setData(TOGGLE_ATTACHMENTS.get(ResourceLocation.parse(toggle.ability_location())), toggle.toggle());
    }
}
