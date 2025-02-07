package com.dimensiondelvers.dimensiondelvers.upgrades;

import com.dimensiondelvers.dimensiondelvers.Registries.AbilityRegistry;
import com.dimensiondelvers.dimensiondelvers.abilities.AbilityAttributes;
import com.dimensiondelvers.dimensiondelvers.init.ModAbilities;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class ReduceBoostCoolDownUpgrade extends AbstractUpgrade{
    public ReduceBoostCoolDownUpgrade(ResourceLocation upgradeName) {
        super(upgradeName);
    }

    @Override
    public void Unlock(Player p) {

        p.getAttribute(AbilityAttributes.BOOST_COOLDOWN).addOrReplacePermanentModifier(AbilityAttributes.BOOST_COOLDOWN_MODIFIER);
        //Use translateable string when you can! This is purely for showing the system, and im too lazy for datagen stuff
        p.sendSystemMessage(Component.literal("You upgraded ").append(Component.translatable(ModAbilities.BOOST_ABILITY.get().GetTranslationString())));
        super.Unlock(p);
    }

    @Override
    public void Remove(Player p) {
        p.getAttribute(AbilityAttributes.BOOST_COOLDOWN).removeModifier(AbilityAttributes.BOOST_COOLDOWN_MODIFIER);
        p.sendSystemMessage(Component.literal("You degraded ").append(Component.translatable(ModAbilities.BOOST_ABILITY.get().GetTranslationString())));
        super.Remove(p);
    }
}
