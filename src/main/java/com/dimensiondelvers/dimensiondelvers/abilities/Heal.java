package com.dimensiondelvers.dimensiondelvers.abilities;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.registries.DeferredHolder;


public class Heal extends AbstractAbility {
    public Heal(ResourceLocation abilityName) {
        super(abilityName, false, false, false);
    }

    @Override
    public MapCodec<? extends AbstractAbility> getCodec() {
        return null;
    }

    @Override
    public void OnActivate(Player p) {
        if(!this.CanPlayerUse(p))
        {
            ((ServerPlayer)p).sendSystemMessage(Component.literal("You cannot use heal!"));
            return;
        }

        //TODO implement cooldown
        int healAmount = (int) p.getAttributeValue(AbilityAttributes.HEAL_EFFECTIVENESS);
        p.heal(healAmount);

        //NOTE permanent modifiers are probably better for "locking" into stages of skills.
        //p.getAttribute(AbilityAttributes.HEAL_EFFECTIVENESS).addOrReplacePermanentModifier(AbilityAttributes.HEAL_MODIFIER);


    }

    @Override
    public void onDeactivate(Player p) {

    }

    @Override
    public DeferredHolder<Attribute, RangedAttribute> GetCooldownLength() {
        return null;
    }

    @Override
    public void tick(Player p) {

    }

}
