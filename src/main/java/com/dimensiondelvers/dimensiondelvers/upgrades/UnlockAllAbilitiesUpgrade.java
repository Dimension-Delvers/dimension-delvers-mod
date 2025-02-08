package com.dimensiondelvers.dimensiondelvers.upgrades;

import com.dimensiondelvers.dimensiondelvers.Registries.AbilityRegistry;
import com.dimensiondelvers.dimensiondelvers.abilities.AbstractAbility;
import com.dimensiondelvers.dimensiondelvers.init.ModAbilities;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class UnlockAllAbilitiesUpgrade extends AbstractUpgrade{
    public UnlockAllAbilitiesUpgrade(ResourceLocation upgradeName) {
        super(upgradeName);
    }

    @Override
    public void Unlock(Player p) {
        for(AbstractAbility abstractAbility: AbilityRegistry.ABILITY_REGISTRY.stream().toList())
        {
            p.setData(ModAbilities.ABILITY_UNLOCKED_ATTACHMENTS.get(abstractAbility.getName()), true);
        }
        //Use translateable string when you can! This is purely for showing the system, and im too lazy for datagen stuff
        p.sendSystemMessage(Component.literal("You unlocked all abilities!"));
        super.Unlock(p);
    }

    @Override
    public void Remove(Player p) {

        for(AbstractAbility abstractAbility: AbilityRegistry.ABILITY_REGISTRY.stream().toList())
        {
            p.setData(ModAbilities.ABILITY_UNLOCKED_ATTACHMENTS.get(abstractAbility.getName()), false);
        }
        p.sendSystemMessage(Component.literal("You removed all abilities!"));
        super.Remove(p);
    }
}
