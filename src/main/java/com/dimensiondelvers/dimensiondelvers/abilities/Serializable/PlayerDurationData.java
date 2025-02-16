package com.dimensiondelvers.dimensiondelvers.abilities.Serializable;

import com.dimensiondelvers.dimensiondelvers.abilities.AbstractAbility;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class PlayerDurationData extends SerializableMap {


    public void beginDuration(ResourceLocation loc, int amount) {
        this.insert(loc, amount);
    }

    public int getDuration(ResourceLocation loc)
    {
        return this.get(loc);
    }

    public boolean isDurationRunning(ResourceLocation loc)
    {
        return this.containsKey(loc) && this.get(loc) > 0;
    }

    public void reduceDurations()
    {
        reduceAll(1);
    }

    public List<AbstractAbility> getRunningDurations(Registry<AbstractAbility> abilityRegistry)
    {
        List<AbstractAbility> durationActive = new ArrayList<>();
        for(ResourceLocation loc: this.getKeys())
        {
            if(abilityRegistry.get(loc).isPresent())
            {
                durationActive.add(abilityRegistry.get(loc).get().value());
            }
        }

        return durationActive;
    }
}
