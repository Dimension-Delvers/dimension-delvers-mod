package com.wanderersoftherift.wotr.abilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.wanderersoftherift.wotr.Registries.AbilityRegistry;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.abilities.Serializable.PlayerCooldownData;
import com.wanderersoftherift.wotr.abilities.Serializable.PlayerDurationData;
import com.wanderersoftherift.wotr.abilities.effects.AbstractEffect;
import com.wanderersoftherift.wotr.init.ModAttachments;
import com.wanderersoftherift.wotr.networking.data.CooldownActivated;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.function.Function;

import static com.wanderersoftherift.wotr.Registries.AbilityRegistry.DATA_PACK_ABILITY_REG_KEY;

public abstract class AbstractAbility {

    private List<AbstractEffect> effects;

    public abstract MapCodec<? extends AbstractAbility> getCodec();
    public static final Codec<AbstractAbility> DIRECT_CODEC = AbilityRegistry.ABILITY_TYPES_REGISTRY.byNameCodec().dispatch(AbstractAbility::getCodec, Function.identity());
    public static final Codec<Holder<AbstractAbility>> CODEC = RegistryFixedCodec.create(DATA_PACK_ABILITY_REG_KEY);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<AbstractAbility>> STREAM_CODEC = ByteBufCodecs.holderRegistry(DATA_PACK_ABILITY_REG_KEY);
    private ResourceLocation name;
    private ResourceLocation icon = ResourceLocation.withDefaultNamespace("textures/misc/forcefield.png");
    public float baseCooldown = 0;
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

    public abstract void OnActivate(Player player);
    public abstract void onDeactivate(Player player);

    public boolean CanPlayerUse(Player player)
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

    public boolean IsOnCooldown(Player player) {
        //If we registered this ability as one that has a cooldown and the player has a cooldown active for this ability.
        return player.getData(ModAttachments.COOL_DOWNS).isOnCooldown(this.getName());
//        return ModAbilities.COOL_DOWN_ATTACHMENTS.containsKey(this.getName()) && p.getData(ModAbilities.COOL_DOWN_ATTACHMENTS.get(this.getName())) > 0;
    }

    //TODO refactor this because I dont think we need to pass in this attribute anymore?
    public void setCooldown(Player player) {
        float cooldown = AbilityAttributeHelper.getAbilityAttribute(AbilityAttributes.COOLDOWN, this.getBaseCooldown(), player);
        //We only need to set a cooldown for ones that have cooldowns
        if(this.hasCooldown())
        {
            WanderersOfTheRift.LOGGER.info("Setting cooldown for: " + this.getName() + " length: " + cooldown);
            PlayerCooldownData cooldowns = player.getData(ModAttachments.COOL_DOWNS);
            cooldowns.setCooldown(this.getName(), (int) cooldown);
            player.setData(ModAttachments.COOL_DOWNS, cooldowns);
        }
         //TODO maybe make helper to calculate time based on ticks for find a different method (maybe include in the attribute???)
        PacketDistributor.sendToPlayer((ServerPlayer) player, new CooldownActivated(this.getName().toString(),(int) cooldown ));
    }
    public boolean hasCooldown() { return getBaseCooldown() > 0; }

    public int getActiveCooldown(Player player) {
        return player.getData(ModAttachments.COOL_DOWNS).getCooldown(this.getName());
    }

    public float getBaseCooldown() {
        return baseCooldown;
    }

    /*
        DURATION RELATED STUFF BELOW
        */
    public boolean hasDuration() {return durationAttribute != null;}
    public boolean isActive(Player player) {
        return player.getData(ModAttachments.DURATIONS).isDurationRunning(this.getName());
    }

    public void setDuration(Player player, Holder<Attribute> attribute) {
        //TODO look into combining this and the cooldown
        if(this.hasDuration())
        {
            WanderersOfTheRift.LOGGER.info("Setting duration for: " + this.getName());
            PlayerDurationData durations = player.getData(ModAttachments.DURATIONS);
            durations.beginDuration(this.getName(), (int)player.getAttributeValue(attribute) * 20);
            player.setData(ModAttachments.DURATIONS, durations);
        }
    }

    public Holder<Attribute> getDurationLength() {
        return durationAttribute;
    }

    public abstract void tick(Player player);

    /*
    TOGGLE STUFF BELOW
     */
    public boolean IsToggle() {return this.isToggle;}
    public void setIsToggle(boolean shouldToggle)
    {
        this.isToggle = shouldToggle;
    }
    public boolean IsToggled(Player player) {
//        return ModAbilities.TOGGLE_ATTACHMENTS.containsKey(this.getName()) && p.getData(ModAbilities.TOGGLE_ATTACHMENTS.get(this.getName()));
        return false;
    }

    public void Toggle(Player player)
    {
        //Change the toggle to opposite and then tell the player
//        if(TOGGLE_ATTACHMENTS.containsKey(this.getName())) p.setData(TOGGLE_ATTACHMENTS.get(this.getName()), !IsToggled(p));
//        PacketDistributor.sendToPlayer((ServerPlayer) p, new ToggleState(this.getName().toString(), IsToggled(p)));
    }

}
