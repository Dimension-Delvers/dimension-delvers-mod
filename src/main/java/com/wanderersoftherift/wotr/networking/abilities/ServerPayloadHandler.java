package com.wanderersoftherift.wotr.networking.abilities;

import com.wanderersoftherift.wotr.abilities.AbstractAbility;
import com.wanderersoftherift.wotr.abilities.attachment.AbilitySlots;
import com.wanderersoftherift.wotr.init.ModAttachments;
import com.wanderersoftherift.wotr.init.ModDataComponentType;
import com.wanderersoftherift.wotr.networking.data.UseAbility;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {
    public static void handleAbilityOnServer(final UseAbility useAbilityPacket, final IPayloadContext context)
    {
        AbilitySlots abilitySlots = context.player().getData(ModAttachments.ABILITY_SLOTS);
        ItemStack abilityItem = abilitySlots.getStackInSlot(useAbilityPacket.slot());
        if (abilityItem.isEmpty() || !abilityItem.has(ModDataComponentType.ABILITY)) {
            return;
        }
        AbstractAbility ability = abilityItem.get(ModDataComponentType.ABILITY).value();
        abilitySlots.setSelectedSlot(useAbilityPacket.slot());

        if (ability.IsToggle()) // Should check last toggle, because pressing a button can send multiple packets
        {
            if (!ability.IsToggled(context.player())) {
                ability.onActivate(context.player(), useAbilityPacket.slot(), abilityItem);
            } else {
                ability.onDeactivate(context.player(), useAbilityPacket.slot());
            }

            if (ability.canPlayerUse(context.player())) {
                ability.Toggle(context.player());
            }
        } else {
            ability.onActivate(context.player(), useAbilityPacket.slot(), abilityItem);
        }
    }
}
