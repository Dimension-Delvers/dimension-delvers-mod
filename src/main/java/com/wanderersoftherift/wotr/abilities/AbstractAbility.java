package com.wanderersoftherift.wotr.abilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.wanderersoftherift.wotr.Registries.AbilityRegistry;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.abilities.Serializable.PlayerCooldownData;
import com.wanderersoftherift.wotr.abilities.Serializable.PlayerDurationData;
import com.wanderersoftherift.wotr.abilities.effects.AbstractEffect;
import com.wanderersoftherift.wotr.codec.DeferrableRegistryCodec;
import com.wanderersoftherift.wotr.init.ModAttachments;
import com.wanderersoftherift.wotr.init.ModAttributes;
import com.wanderersoftherift.wotr.item.skillgem.AbilitySlots;
import com.wanderersoftherift.wotr.networking.data.CooldownActivated;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.function.Function;

import static com.wanderersoftherift.wotr.Registries.AbilityRegistry.DATA_PACK_ABILITY_REG_KEY;

public abstract class AbstractAbility {

    private final List<AbstractEffect> effects;

    public abstract MapCodec<? extends AbstractAbility> getCodec();

    public static final Codec<AbstractAbility> DIRECT_CODEC = AbilityRegistry.ABILITY_TYPES_REGISTRY.byNameCodec().dispatch(AbstractAbility::getCodec, Function.identity());
    public static final Codec<Holder<AbstractAbility>> CODEC = DeferrableRegistryCodec.create(DATA_PACK_ABILITY_REG_KEY);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<AbstractAbility>> STREAM_CODEC = ByteBufCodecs.holderRegistry(DATA_PACK_ABILITY_REG_KEY);
    private final ResourceLocation name;
    private ResourceLocation icon = ResourceLocation.withDefaultNamespace("textures/misc/forcefield.png");
    public float baseCooldown = 0;
    public Holder<Attribute> durationAttribute = null;
    private boolean isToggle = false;

    public AbstractAbility(ResourceLocation abilityName, ResourceLocation icon, List<AbstractEffect> effects) {
        this.name = abilityName;
        this.effects = effects;
        this.icon = icon;
    }

    public void setIcon(ResourceLocation location) {
        icon = location;
    }

    public ResourceLocation getIcon() {
        return icon;
    }

    public List<AbstractEffect> getEffects() {
        return this.effects;
    }

    public abstract void OnActivate(Player player, int slot, ItemStack abilityItem);

    public abstract void onDeactivate(Player player, int slot);

    public boolean CanPlayerUse(Player player) {
//        return p.getData(ModAbilities.ABILITY_UNLOCKED_ATTACHMENTS.get(this.getName()));
        return true;
    }

    public ResourceLocation getName() {
        return name;
    }

    public String GetTranslationString() {
        return "ability." + getName().getNamespace() + "." + getName().getPath();
    }

    /*
    COOL DOWN RELATED STUFF HERE
    */

    public boolean IsOnCooldown(Player player, int slot) {
        //If we registered this ability as one that has a cooldown and the player has a cooldown active for this ability.
        return player.getData(ModAttachments.ABILITY_COOLDOWNS).isOnCooldown(slot);
//        return ModAbilities.COOL_DOWN_ATTACHMENTS.containsKey(this.getName()) && p.getData(ModAbilities.COOL_DOWN_ATTACHMENTS.get(this.getName())) > 0;
    }

    //TODO refactor this because I dont think we need to pass in this attribute anymore?
    public void setCooldown(Player player, int slot) {
        if (this.hasCooldown()) {
            AbilitySlots abilitySlots = player.getData(ModAttachments.ABILITY_SLOTS);
            ItemStack abilityItem = abilitySlots.getStackInSlot(slot);

            float cooldown = AbilityAttributeHelper.getAbilityAttribute(ModAttributes.COOLDOWN, this.getBaseCooldown(), player, abilityItem);

            WanderersOfTheRift.LOGGER.info("Setting cooldown for: " + this.getName() + " length: " + cooldown);
            PlayerCooldownData cooldowns = player.getData(ModAttachments.ABILITY_COOLDOWNS);
            cooldowns.setCooldown(slot, (int) cooldown);
            player.setData(ModAttachments.ABILITY_COOLDOWNS, cooldowns);

            PacketDistributor.sendToPlayer((ServerPlayer) player, new CooldownActivated(slot, (int) cooldown, (int) cooldown));
        }
    }

    public boolean hasCooldown() {
        return getBaseCooldown() > 0;
    }

    public int getActiveCooldown(Player player, int slot) {
        return player.getData(ModAttachments.ABILITY_COOLDOWNS).getCooldownRemaining(slot);
    }

    public float getBaseCooldown() {
        return baseCooldown;
    }

    /*
        DURATION RELATED STUFF BELOW
        */
    public boolean hasDuration() {
        return durationAttribute != null;
    }

    public boolean isActive(Player player) {
        return player.getData(ModAttachments.DURATIONS).isDurationRunning(this.getName());
    }

    public void setDuration(Player player, Holder<Attribute> attribute) {
        //TODO look into combining this and the cooldown
        if (this.hasDuration()) {
            WanderersOfTheRift.LOGGER.info("Setting duration for: " + this.getName());
            PlayerDurationData durations = player.getData(ModAttachments.DURATIONS);
            durations.beginDuration(this.getName(), (int) player.getAttributeValue(attribute) * 20);
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
    public boolean IsToggle() {
        return this.isToggle;
    }

    public void setIsToggle(boolean shouldToggle) {
        this.isToggle = shouldToggle;
    }

    public boolean IsToggled(Player player) {
//        return ModAbilities.TOGGLE_ATTACHMENTS.containsKey(this.getName()) && p.getData(ModAbilities.TOGGLE_ATTACHMENTS.get(this.getName()));
        return false;
    }

    public void Toggle(Player player) {
        //Change the toggle to opposite and then tell the player
//        if(TOGGLE_ATTACHMENTS.containsKey(this.getName())) p.setData(TOGGLE_ATTACHMENTS.get(this.getName()), !IsToggled(p));
//        PacketDistributor.sendToPlayer((ServerPlayer) p, new ToggleState(this.getName().toString(), IsToggled(p)));
    }

}
