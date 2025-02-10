package com.dimensiondelvers.dimensiondelvers.abilities;

import com.dimensiondelvers.dimensiondelvers.init.ModAbilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public abstract class AbstractAbility {

    //TODO possibly look into abstracting this more into different types of abstract abilities such as cooldown, vs toggle, vs casting, etc that way we can just check the instance of the ability being used to perform some of these checks
    private ResourceLocation name;
    private ResourceLocation icon = ResourceLocation.withDefaultNamespace("textures/misc/forcefield.png");

    public AbstractAbility(ResourceLocation abilityName)
    {
        this.name = abilityName;
    }
    public void setIcon(ResourceLocation location)
    {
        icon = location;
    }

    public ResourceLocation getIcon() {
        return icon;
    }

    public abstract void OnActivate(Player p);
    public abstract void onDeactivate(Player p);

    public boolean CanPlayerUse(Player p)
    {
        return p.getData(ModAbilities.ABILITY_UNLOCKED_ATTACHMENTS.get(this.getName()));
    }

    public ResourceLocation getName()
    {
        return name;
    }

    public String GetTranslationString()
    {
        return "ability." + getName().getNamespace() + "." + getName().getPath();
    }

}
