package com.dimensiondelvers.dimensiondelvers.abilities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;


public class Heal extends AbstractAbility {
    public Heal(ResourceLocation abilityName) {
        super(abilityName);
    }

    @Override
    public void OnActivate(Player p) {

        //TODO implement cooldown
        int healAmount = (int) p.getAttributeValue(AbilityAttributes.HEAL_EFFECTIVENESS);
        p.heal(healAmount);

        //NOTE permanent modifiers are probably better for "locking" into stages of skills.
        p.getAttribute(AbilityAttributes.HEAL_EFFECTIVENESS).addOrReplacePermanentModifier(AbilityAttributes.HEAL_MODIFIER);
    }

    @Override
    public void OnDeactivate(Player p) {

    }
}
