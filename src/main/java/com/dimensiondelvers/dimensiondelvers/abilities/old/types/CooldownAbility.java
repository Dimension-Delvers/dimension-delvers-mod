package com.dimensiondelvers.dimensiondelvers.abilities.old.types;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.registries.DeferredHolder;

public interface CooldownAbility {

    boolean IsOnCooldown(Player p);

    void SetCooldown(Player p, DeferredHolder<Attribute, RangedAttribute> attribute);
    default boolean HasCooldown() {return true;}
    DeferredHolder<Attribute, RangedAttribute> GetCooldownLength();

}
