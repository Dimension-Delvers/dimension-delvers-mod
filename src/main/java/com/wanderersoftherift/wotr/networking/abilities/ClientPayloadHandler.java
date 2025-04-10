package com.wanderersoftherift.wotr.networking.abilities;

import com.wanderersoftherift.wotr.abilities.attachment.PlayerCooldownData;
import com.wanderersoftherift.wotr.init.ModAttachments;
import com.wanderersoftherift.wotr.networking.data.CooldownActivated;
import com.wanderersoftherift.wotr.networking.data.ToggleState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {
    public static void handleCooldownOnClient(final CooldownActivated cooldownActivated, final IPayloadContext context)
    {
        PlayerCooldownData cooldowns = context.player().getData(ModAttachments.ABILITY_COOLDOWNS);
        cooldowns.setCooldown(cooldownActivated.slot(), cooldownActivated.cooldownLength(), cooldownActivated.cooldownRemaining());
        context.player().setData(ModAttachments.ABILITY_COOLDOWNS, cooldowns);
    }

    public static void handleToggleOnClient(final ToggleState toggle, final IPayloadContext context)
    {
//        context.player().setData(TOGGLE_ATTACHMENTS.get(ResourceLocation.parse(toggle.ability_location())), toggle.toggle());
    }
}
