package com.wanderersoftherift.wotr.networking.abilities;

import com.wanderersoftherift.wotr.Registries.UpgradeRegistry;
import com.wanderersoftherift.wotr.abilities.AbstractAbility;
import com.wanderersoftherift.wotr.client.gui.menu.TestMenu;
import com.wanderersoftherift.wotr.init.ModAttachments;
import com.wanderersoftherift.wotr.init.ModDataComponentType;
import com.wanderersoftherift.wotr.item.skillgem.AbilitySlots;
import com.wanderersoftherift.wotr.networking.data.ClaimUpgrade;
import com.wanderersoftherift.wotr.networking.data.OpenUpgradeMenu;
import com.wanderersoftherift.wotr.networking.data.UseAbility;
import com.wanderersoftherift.wotr.upgrades.AbstractUpgrade;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleMenuProvider;
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
                ability.OnActivate(context.player(), useAbilityPacket.slot());
            } else {
                ability.onDeactivate(context.player(), useAbilityPacket.slot());
            }

            if (ability.CanPlayerUse(context.player())) {
                ability.Toggle(context.player());
            }
        } else {
            ability.OnActivate(context.player(), useAbilityPacket.slot());
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
        UpgradeRegistry.UPGRADE_REGISTRY.get(ResourceLocation.parse(upgrade.upgrade_location())).ifPresent((Holder<AbstractUpgrade> upgradeHolder) -> {
            AbstractUpgrade abstractUpgrade = upgradeHolder.value();
            if(!abstractUpgrade.isUnlocked(context.player()))
            {
                abstractUpgrade.unlock(context.player());
            }
            else
            {
                abstractUpgrade.remove(context.player());
            }
        });
    }
}
