package com.dimensiondelvers.dimensiondelvers.abilities.types;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.registries.DeferredHolder;

public interface DurationAbility {
    boolean isActive(Player p);
    void setDuration(Player p, DeferredHolder<Attribute, RangedAttribute> attribute);

    void Tick(Player p);
}
