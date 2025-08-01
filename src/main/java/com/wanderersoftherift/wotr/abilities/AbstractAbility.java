package com.wanderersoftherift.wotr.abilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.abilities.attachment.PlayerCooldownData;
import com.wanderersoftherift.wotr.abilities.attachment.PlayerDurationData;
import com.wanderersoftherift.wotr.abilities.effects.AbstractEffect;
import com.wanderersoftherift.wotr.init.WotrAttachments;
import com.wanderersoftherift.wotr.init.WotrAttributes;
import com.wanderersoftherift.wotr.init.WotrRegistries;
import com.wanderersoftherift.wotr.modifier.effect.AbstractModifierEffect;
import com.wanderersoftherift.wotr.modifier.effect.AttributeModifierEffect;
import com.wanderersoftherift.wotr.network.ability.AbilityCooldownUpdatePayload;
import com.wanderersoftherift.wotr.serialization.LaxRegistryCodec;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.wanderersoftherift.wotr.init.WotrRegistries.Keys.ABILITIES;

public abstract class AbstractAbility {

    public static final Codec<AbstractAbility> DIRECT_CODEC = WotrRegistries.ABILITY_TYPES.byNameCodec()
            .dispatch(AbstractAbility::getCodec, Function.identity());
    public static final Codec<Holder<AbstractAbility>> CODEC = LaxRegistryCodec.create(ABILITIES);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<AbstractAbility>> STREAM_CODEC = ByteBufCodecs
            .holderRegistry(ABILITIES);

    private final ResourceLocation name;
    private ResourceLocation icon = ResourceLocation.withDefaultNamespace("textures/misc/forcefield.png");
    private Optional<ResourceLocation> smallIcon = Optional.empty();
    private Component displayName;

    private final List<AbstractEffect> effects;
    private float baseCooldown = 0;
    private int baseManaCost;

    private Holder<Attribute> durationAttribute = null;
    private boolean isToggle = false;

    public AbstractAbility(ResourceLocation abilityName, ResourceLocation icon, Optional<ResourceLocation> smallIcon,
            List<AbstractEffect> effects, int baseCooldown) {
        this.name = abilityName;
        this.effects = effects;
        this.icon = icon;
        this.smallIcon = smallIcon;
        this.displayName = Component.translatable("ability." + getName().getNamespace() + "." + getName().getPath());
        this.baseCooldown = baseCooldown;
    }

    public abstract MapCodec<? extends AbstractAbility> getCodec();

    public void setIcon(ResourceLocation location) {
        icon = location;
    }

    public ResourceLocation getIcon() {
        return icon;
    }

    public Optional<ResourceLocation> getSmallIcon() {
        return smallIcon;
    }

    public List<AbstractEffect> getEffects() {
        return this.effects;
    }

    public int getBaseManaCost() {
        return baseManaCost;
    }

    public void setBaseManaCost(int baseManaCost) {
        this.baseManaCost = baseManaCost;
    }

    public abstract void onActivate(Player player, int slot, ItemStack abilityItem);

    public abstract void onDeactivate(Player player, int slot);

    public boolean canPlayerUse(Player player) {
//        return p.getData(ModAbilities.ABILITY_UNLOCKED_ATTACHMENTS.get(this.getName()));
        return true;
    }

    public ResourceLocation getName() {
        return name;
    }

    public Component getDisplayName() {
        return displayName;
    }

    /*
     * COOL DOWN RELATED STUFF HERE
     */

    public boolean isOnCooldown(Player player, int slot) {
        // If we registered this ability as one that has a cooldown and the player has a cooldown active for this
        // ability.
        return player.getData(WotrAttachments.ABILITY_COOLDOWNS).isOnCooldown(slot);
//        return ModAbilities.COOL_DOWN_ATTACHMENTS.containsKey(this.getName()) && p.getData(ModAbilities.COOL_DOWN_ATTACHMENTS.get(this.getName())) > 0;
    }

    public void setCooldown(Player player, int slot, float amount) {
        if (this.hasCooldown()) {
            WanderersOfTheRift.LOGGER.info("Setting cooldown for: " + this.getName() + " length: " + amount);
            PlayerCooldownData cooldowns = player.getData(WotrAttachments.ABILITY_COOLDOWNS);
            int finalCooldown = (int) Math.max(getBaseCooldown() * 0.9F, amount);
            cooldowns.setCooldown(slot, finalCooldown);
            player.setData(WotrAttachments.ABILITY_COOLDOWNS, cooldowns);

            PacketDistributor.sendToPlayer((ServerPlayer) player,
                    new AbilityCooldownUpdatePayload(slot, (int) amount, (int) amount));
        }
    }

    public boolean hasCooldown() {
        return getBaseCooldown() > 0;
    }

    public int getActiveCooldown(Player player, int slot) {
        return player.getData(WotrAttachments.ABILITY_COOLDOWNS).getCooldownRemaining(slot);
    }

    public float getBaseCooldown() {
        return baseCooldown;
    }

    /*
     * DURATION RELATED STUFF BELOW
     */
    public boolean hasDuration() {
        return durationAttribute != null;
    }

    public boolean isActive(Player player) {
        return player.getData(WotrAttachments.DURATIONS).isDurationRunning(this.getName());
    }

    public void setDuration(Player player, Holder<Attribute> attribute) {
        // TODO look into combining this and the cooldown
        if (this.hasDuration()) {
            WanderersOfTheRift.LOGGER.info("Setting duration for: " + this.getName());
            PlayerDurationData durations = player.getData(WotrAttachments.DURATIONS);
            durations.beginDuration(this.getName(), (int) player.getAttributeValue(attribute) * 20);
            player.setData(WotrAttachments.DURATIONS, durations);
        }
    }

    public Holder<Attribute> getDurationLength() {
        return durationAttribute;
    }

    public abstract void tick(Player player);

    /*
     * TOGGLE STUFF BELOW
     */
    public boolean isToggle() {
        return this.isToggle;
    }

    public void setIsToggle(boolean shouldToggle) {
        this.isToggle = shouldToggle;
    }

    public boolean isToggled(Player player) {
//        return ModAbilities.TOGGLE_ATTACHMENTS.containsKey(this.getName()) && p.getData(ModAbilities.TOGGLE_ATTACHMENTS.get(this.getName()));
        return false;
    }

    public void toggle(Player player) {
        // Change the toggle to opposite and then tell the player
//        if(TOGGLE_ATTACHMENTS.containsKey(this.getName())) p.setData(TOGGLE_ATTACHMENTS.get(this.getName()), !IsToggled(p));
//        PacketDistributor.sendToPlayer((ServerPlayer) p, new ToggleState(this.getName().toString(), IsToggled(p)));
    }

    public boolean isRelevantModifier(AbstractModifierEffect modifierEffect) {
        if (modifierEffect instanceof AttributeModifierEffect attributeModifierEffect) {
            Holder<Attribute> attribute = attributeModifierEffect.getAttribute();
            if (WotrAttributes.COOLDOWN.equals(attribute) && baseCooldown > 0) {
                return true;
            }
            if (WotrAttributes.MANA_COST.equals(attribute) && baseManaCost > 0) {
                return true;
            }
        }
        for (AbstractEffect effect : effects) {
            if (effect.isRelevant(modifierEffect)) {
                return true;
            }
        }
        return false;
    }
}
