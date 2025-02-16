package com.dimensiondelvers.dimensiondelvers.abilities.old.types;

import net.minecraft.world.entity.player.Player;

public interface ToggleAbility {
    boolean IsToggled(Player p);
    void Toggle(Player p);
}
