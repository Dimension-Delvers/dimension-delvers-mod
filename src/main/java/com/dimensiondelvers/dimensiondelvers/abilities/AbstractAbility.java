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
    private boolean hasCooldown = false;
    private boolean isToggleAbility = false;

    public AbstractAbility(ResourceLocation abilityName)
    {
        this.name = abilityName;
    }
    public AbstractAbility(ResourceLocation abilityName, boolean hasCooldown, boolean shouldToggle)
    {
        this.name = abilityName;
        this.hasCooldown = hasCooldown;
        this.isToggleAbility = shouldToggle;
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

    public boolean HasCooldown()
    {
        return this.hasCooldown;
    }

    public boolean IsToggleAbility() { return this.isToggleAbility; }

    public boolean IsOnCooldown(Player p) {
        //If we registered this ability as one that has a cooldown and the player has a cooldown active for this ability.
        return COOL_DOWN_ATTACHMENTS.containsKey(this.GetName()) && p.getData(COOL_DOWN_ATTACHMENTS.get(this.GetName())) > 0;
    }

    public boolean IsToggled(Player p) {
        return TOGGLE_ATTACHMENTS.containsKey(this.GetName()) && p.getData(TOGGLE_ATTACHMENTS.get(this.GetName()));
    }

    public void Toggle(Player p)
    {
        //Change the toggle to opposite and then tell the player
        if(TOGGLE_ATTACHMENTS.containsKey(this.GetName())) p.setData(TOGGLE_ATTACHMENTS.get(this.GetName()), !IsToggled(p));
        PacketDistributor.sendToPlayer((ServerPlayer) p, new ToggleState(this.GetName().toString(), IsToggled(p)));
    }

    public String GetTranslationString()
    {
        return "ability." + GetName().getNamespace() + "." + GetName().getPath();
    }

}
