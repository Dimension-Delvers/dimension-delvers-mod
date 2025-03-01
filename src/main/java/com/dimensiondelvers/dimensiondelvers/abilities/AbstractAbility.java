package com.dimensiondelvers.dimensiondelvers.abilities;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.Registries.AbilityRegistry;
import com.dimensiondelvers.dimensiondelvers.abilities.Serializable.PlayerCooldownData;
import com.dimensiondelvers.dimensiondelvers.abilities.Serializable.PlayerDurationData;
import com.dimensiondelvers.dimensiondelvers.abilities.effects.AbstractEffect;
import com.dimensiondelvers.dimensiondelvers.init.ModAbilities;
import com.dimensiondelvers.dimensiondelvers.networking.data.CooldownActivated;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.function.Function;

import static com.dimensiondelvers.dimensiondelvers.Registries.AbilityRegistry.DATA_PACK_ABILITY_REG_KEY;

public abstract class AbstractAbility {

    private List<AbstractEffect> effects;

    public abstract MapCodec<? extends AbstractAbility> getCodec();
    public static final Codec<AbstractAbility> DIRECT_CODEC = AbilityRegistry.ABILITY_TYPES_REGISTRY.byNameCodec().dispatch(AbstractAbility::getCodec, Function.identity());
    public static final Codec<Holder<AbstractAbility>> CODEC = RegistryFixedCodec.create(DATA_PACK_ABILITY_REG_KEY);
    private ResourceLocation name;
    private ResourceLocation icon = ResourceLocation.withDefaultNamespace("textures/misc/forcefield.png");
    public Holder<Attribute> cooldownAttribute = null;
    public Holder<Attribute> durationAttribute = null;
    private boolean isToggle = false;
    public AbstractAbility(ResourceLocation abilityName, List<AbstractEffect> effects)
    {
        this.name = abilityName;
        this.effects = effects;
    }
    public void setIcon(ResourceLocation location)
    {
        icon = location;
    }

    public ResourceLocation getIcon() {
        return icon;
    }
    public List<AbstractEffect> getEffects() {
        return this.effects;
    }

    public abstract void OnActivate(Player p);
    public abstract void onDeactivate(Player p);

    public boolean CanPlayerUse(Player p)
    {
//        return p.getData(ModAbilities.ABILITY_UNLOCKED_ATTACHMENTS.get(this.getName()));
        return true;
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
        return p.getData(ModAbilities.COOL_DOWNS).isOnCooldown(this.getName());
//        return ModAbilities.COOL_DOWN_ATTACHMENTS.containsKey(this.getName()) && p.getData(ModAbilities.COOL_DOWN_ATTACHMENTS.get(this.getName())) > 0;
    }

    //TODO refactor this because I dont think we need to pass in this attribute anymore?
    public void setCooldown(Player p, Holder<Attribute> attribute) {

        //We only need to set a cooldown for ones that have cooldowns
        if(this.hasCooldown())
        {
            DimensionDelvers.LOGGER.info("Setting cooldown for: " + this.getName() + " length: " + (int)p.getAttributeValue(attribute) * 20);
            PlayerCooldownData cooldowns = p.getData(ModAbilities.COOL_DOWNS);
            cooldowns.setCooldown(this.getName(), (int)p.getAttributeValue(attribute) * 20);
            p.setData(ModAbilities.COOL_DOWNS, cooldowns);
        }
         //TODO maybe make helper to calculate time based on ticks for find a different method (maybe include in the attribute???)
        PacketDistributor.sendToPlayer((ServerPlayer) p, new CooldownActivated(this.getName().toString(),(int)p.getAttributeValue(attribute) * 20 ));
    }
    public boolean hasCooldown() { return cooldownAttribute != null; }
    public Holder<Attribute> getCooldownLength() {
        return cooldownAttribute;
    }

    public int getCooldown(Player p) {
        return p.getData(ModAbilities.COOL_DOWNS).getCooldown(this.getName());
    }


    /*
    DURATION RELATED STUFF BELOW
    */
    public boolean hasDuration() {return durationAttribute != null;}
    public boolean isActive(Player p) {
        return p.getData(ModAbilities.DURATIONS).isDurationRunning(this.getName());
    }

    public void setDuration(Player p, Holder<Attribute> attribute) {
        //TODO look into combining this and the cooldown
        if(this.hasDuration())
        {
            DimensionDelvers.LOGGER.info("Setting duration for: " + this.getName());
            PlayerDurationData durations = p.getData(ModAbilities.DURATIONS);
            durations.beginDuration(this.getName(), (int)p.getAttributeValue(attribute) * 20);
            p.setData(ModAbilities.DURATIONS, durations);
        }
    }

    public Holder<Attribute> getDurationLength() {
        return durationAttribute;
    }

    public abstract void tick(Player p);

    /*
    TOGGLE STUFF BELOW
     */
    public boolean IsToggle() {return this.isToggle;}
    public void setIsToggle(boolean shouldToggle)
    {
        this.isToggle = shouldToggle;
    }
    public boolean IsToggled(Player p) {
//        return ModAbilities.TOGGLE_ATTACHMENTS.containsKey(this.getName()) && p.getData(ModAbilities.TOGGLE_ATTACHMENTS.get(this.getName()));
        return false;
    }

    public void Toggle(Player p)
    {
        //Change the toggle to opposite and then tell the player
//        if(TOGGLE_ATTACHMENTS.containsKey(this.getName())) p.setData(TOGGLE_ATTACHMENTS.get(this.getName()), !IsToggled(p));
//        PacketDistributor.sendToPlayer((ServerPlayer) p, new ToggleState(this.getName().toString(), IsToggled(p)));
    }

}
