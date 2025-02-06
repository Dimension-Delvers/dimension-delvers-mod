package com.dimensiondelvers.dimensiondelvers.abilities;

import com.dimensiondelvers.dimensiondelvers.init.ModAbilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public abstract class AbstractAbility {
    private ResourceLocation name;
    private boolean hasCooldown = false;

    public AbstractAbility(ResourceLocation abilityName)
    {
        this.name = abilityName;
    }
    public AbstractAbility(ResourceLocation abilityName, boolean hasCooldown)
    {
        this.name = abilityName;
        this.hasCooldown = hasCooldown;
        if(this.HasCooldown()) {
            ModAbilities.COOLDOWN_MAP.put(this.name, true);
        }
    }
    public abstract void OnActivate(Player p);
    public abstract void OnDeactivate(Player p);

    public ResourceLocation GetName()
    {
        return name;
    }

    //Leaving for now, might need to come back to this, but seems like per player having cooldown is a better solution than a global one
//    public int GetCoolDownLength()
//    {
//        return cooldownTotal;
//    }

    public boolean HasCooldown()
    {
        return this.hasCooldown;
    }



}
