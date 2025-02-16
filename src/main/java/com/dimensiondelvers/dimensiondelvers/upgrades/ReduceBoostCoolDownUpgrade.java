package com.dimensiondelvers.dimensiondelvers.upgrades;

import com.dimensiondelvers.dimensiondelvers.abilities.AbilityAttributes;
import com.dimensiondelvers.dimensiondelvers.init.ModAbilities;
import com.dimensiondelvers.dimensiondelvers.init.ModUpgrades;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class ReduceBoostCoolDownUpgrade extends AbstractUpgrade{
    public ReduceBoostCoolDownUpgrade(ResourceLocation upgradeName) {
        super(upgradeName);
//        addRequirement(ModUpgrades.UNLOCK_BOOST.get());
    }

    @Override
    public void unlock(Player p) {
        super.unlock(p); //TODO cleanup the check for the requirements
        if(isUnlocked(p)) {
            p.getAttribute(AbilityAttributes.BOOST_COOLDOWN).addOrReplacePermanentModifier(AbilityAttributes.BOOST_COOLDOWN_MODIFIER);
            //Use translateable string when you can! This is purely for showing the system, and im too lazy for datagen stuff
//            ((ServerPlayer)p).sendSystemMessage(Component.literal("You upgraded ").append(Component.translatable(ModAbilities.BOOST_ABILITY.get().GetTranslationString())));

        }

    }

    @Override
    public void remove(Player p) {
        p.getAttribute(AbilityAttributes.BOOST_COOLDOWN).removeModifier(AbilityAttributes.BOOST_COOLDOWN_MODIFIER);
//        ((ServerPlayer)p).sendSystemMessage(Component.literal("You degraded ").append(Component.translatable(ModAbilities.BOOST_ABILITY.get().GetTranslationString())));
        super.remove(p);
    }
}
