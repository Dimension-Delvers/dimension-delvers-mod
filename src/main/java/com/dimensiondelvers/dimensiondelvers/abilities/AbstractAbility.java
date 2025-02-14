package com.dimensiondelvers.dimensiondelvers.abilities;

import com.dimensiondelvers.dimensiondelvers.Registries.AbilityRegistry;
import com.dimensiondelvers.dimensiondelvers.init.ModAbilities;
import com.dimensiondelvers.dimensiondelvers.networking.data.CooldownActivated;
import com.dimensiondelvers.dimensiondelvers.networking.data.ToggleState;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Function;

import static com.dimensiondelvers.dimensiondelvers.init.ModAbilities.COOL_DOWN_ATTACHMENTS;
import static com.dimensiondelvers.dimensiondelvers.init.ModAbilities.TOGGLE_ATTACHMENTS;

public abstract class AbstractAbility {

    public abstract MapCodec<? extends AbstractAbility> getCodec();
    public static final Codec<AbstractAbility> DIRECT_CODEC = AbilityRegistry.ABILITY_TYPES_REGISTRY.byNameCodec().dispatch(AbstractAbility::getCodec, Function.identity());
    private ResourceLocation name;
    private ResourceLocation icon = ResourceLocation.withDefaultNamespace("textures/misc/forcefield.png");

    private boolean hasCooldown = false;
    private boolean hasDuration = false;
    private boolean isToggle = false;
    public AbstractAbility(ResourceLocation abilityName, boolean hasCooldown, boolean hasDuration, boolean isToggle)
    {
        this.name = abilityName;
        this.hasCooldown = hasCooldown;
        this.hasDuration = hasDuration;
        this.isToggle = isToggle;
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

    /*
    COOL DOWN RELATED STUFF HERE
    */

    public boolean IsOnCooldown(Player p) {
        //If we registered this ability as one that has a cooldown and the player has a cooldown active for this ability.
        return ModAbilities.COOL_DOWN_ATTACHMENTS.containsKey(this.getName()) && p.getData(ModAbilities.COOL_DOWN_ATTACHMENTS.get(this.getName())) > 0;
    }

    public void SetCooldown(Player p, DeferredHolder<Attribute, RangedAttribute> attribute) {
        p.setData(COOL_DOWN_ATTACHMENTS.get(this.getName()), (int)p.getAttributeValue(attribute) * 20); //TODO maybe make helper to calculate time based on ticks for find a different method (maybe include in the attribute???)
        PacketDistributor.sendToPlayer((ServerPlayer) p, new CooldownActivated(this.getName().toString(),(int)p.getAttributeValue(GetCooldownLength()) * 20 ));
    }
    public boolean HasCooldown() { return this.hasCooldown; }
    public abstract DeferredHolder<Attribute, RangedAttribute> GetCooldownLength();

    /*
    DURATION RELATED STUFF BELOW
    */
    public boolean HasDuration() {return this.hasDuration;}
    public boolean isActive(Player p) {
        return ModAbilities.DURATION_ATTACHMENTS.containsKey(this.getName()) && p.getData(ModAbilities.DURATION_ATTACHMENTS.get(this.getName())) > 0;
    }

    public void setDuration(Player p, DeferredHolder<Attribute, RangedAttribute> attribute) {
        p.setData(ModAbilities.DURATION_ATTACHMENTS.get(this.getName()), (int)p.getAttributeValue(attribute) * 20); //TODO maybe make helper to calculate time based on ticks for find a different method (maybe include in the attribute???)
    }

    public abstract void tick(Player p);

    /*
    TOGGLE STUFF BELOW
     */
    public boolean IsToggle() {return this.isToggle;}
    public boolean IsToggled(Player p) {
        return ModAbilities.TOGGLE_ATTACHMENTS.containsKey(this.getName()) && p.getData(ModAbilities.TOGGLE_ATTACHMENTS.get(this.getName()));
    }
    public void Toggle(Player p)
    {
        //Change the toggle to opposite and then tell the player
        if(TOGGLE_ATTACHMENTS.containsKey(this.getName())) p.setData(TOGGLE_ATTACHMENTS.get(this.getName()), !IsToggled(p));
        PacketDistributor.sendToPlayer((ServerPlayer) p, new ToggleState(this.getName().toString(), IsToggled(p)));
    }

}
