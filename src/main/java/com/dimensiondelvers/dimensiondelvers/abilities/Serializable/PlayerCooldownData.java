package com.dimensiondelvers.dimensiondelvers.abilities.Serializable;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.abilities.AbstractAbility;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerCooldownData extends SerializableMap {


    public void setCooldown(ResourceLocation loc, int amount) {
        this.insert(loc, amount);
    }

    public int getCooldown(ResourceLocation loc)
    {
        return this.get(loc);
    }

    public boolean isOnCooldown(ResourceLocation loc)
    {
        return this.containsKey(loc) && this.get(loc) > 0;
    }

    public void reduceCooldowns()
    {
        reduceAll(1);
    }

    public List<AbstractAbility> getActiveCooldowns(Registry<AbstractAbility> abilityRegistry)
    {
        List<AbstractAbility> onCoolDown = new ArrayList<>();
        for(ResourceLocation loc: this.getKeys())
        {
            if(abilityRegistry.get(loc).isPresent())
            {
                onCoolDown.add(abilityRegistry.get(loc).get().value());
            }
        }

        return onCoolDown;
    }
}
