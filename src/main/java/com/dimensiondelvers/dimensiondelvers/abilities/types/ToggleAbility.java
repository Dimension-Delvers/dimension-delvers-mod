package com.dimensiondelvers.dimensiondelvers.abilities.types;

import net.minecraft.world.entity.player.Player;

import static com.dimensiondelvers.dimensiondelvers.init.ModAbilities.TOGGLE_ATTACHMENTS;

public interface ToggleAbility {
    boolean IsToggled(Player p);
    void Toggle(Player p);
}
