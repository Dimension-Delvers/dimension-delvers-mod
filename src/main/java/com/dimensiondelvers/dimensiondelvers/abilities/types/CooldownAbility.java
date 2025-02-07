package com.dimensiondelvers.dimensiondelvers.abilities.types;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.dimensiondelvers.dimensiondelvers.init.ModAbilities.COOL_DOWN_ATTACHMENTS;

public interface CooldownAbility {

    boolean IsOnCooldown(Player p);

    void SetCooldown(Player p, DeferredHolder<Attribute, RangedAttribute> attribute);
    default boolean HasCooldown() {return true;}
}
