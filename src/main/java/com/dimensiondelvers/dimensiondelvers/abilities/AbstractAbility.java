package com.dimensiondelvers.dimensiondelvers.abilities;

import com.dimensiondelvers.dimensiondelvers.init.ModAbilities;
import com.dimensiondelvers.dimensiondelvers.networking.data.ToggleState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import static com.dimensiondelvers.dimensiondelvers.init.ModAbilities.COOL_DOWN_ATTACHMENTS;
import static com.dimensiondelvers.dimensiondelvers.init.ModAbilities.TOGGLE_ATTACHMENTS;

public abstract class AbstractAbility {

    //TODO possibly look into abstracting this more into different types of abstract abilities such as cooldown, vs toggle, vs casting, etc that way we can just check the instance of the ability being used to perform some of these checks
    private ResourceLocation name;

    public AbstractAbility(ResourceLocation abilityName)
    {
        this.name = abilityName;
    }

    public abstract void OnActivate(Player p);
    public abstract void OnDeactivate(Player p);

    public boolean CanPlayerUse(Player p)
    {
        return p.getData(ModAbilities.ABILITY_UNLOCKED_ATTACHMENTS.get(this.GetName()));
    }

    public ResourceLocation GetName()
    {
        return name;
    }

    public String GetTranslationString()
    {
        return "ability." + GetName().getNamespace() + "." + GetName().getPath();
    }

}
