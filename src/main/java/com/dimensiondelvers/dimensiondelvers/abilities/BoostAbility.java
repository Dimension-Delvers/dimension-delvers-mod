package com.dimensiondelvers.dimensiondelvers.abilities;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.abilities.effects.AbstractEffect;
import com.dimensiondelvers.dimensiondelvers.networking.data.CooldownActivated;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;

public class BoostAbility extends AbstractAbility {

    //Todo make this a list and also put it in the abstract ability
    private List<AbstractEffect> effects;

    Holder<Attribute> boostStrength;
    public BoostAbility(ResourceLocation abilityName) {
        super(abilityName);
        setIcon(ResourceLocation.withDefaultNamespace("textures/mob_effect/wind_charged.png"));
    }

    public BoostAbility(ResourceLocation abilityName, DeferredHolder<Attribute, RangedAttribute> cooldown) {
        super(abilityName);
        setIcon(ResourceLocation.withDefaultNamespace("textures/mob_effect/wind_charged.png"));
    }

    public BoostAbility(ResourceLocation resourceLocation, Holder<Attribute> cooldown , Holder<Attribute> strength, List<AbstractEffect> effects) {
        super(resourceLocation);
        setIcon(ResourceLocation.withDefaultNamespace("textures/mob_effect/wind_charged.png"));
        this.cooldownAttribute = cooldown;
        this.boostStrength = strength;
        this.effects = effects;
    }

    @Override
    public MapCodec<? extends AbstractAbility> getCodec() {
        return CODEC;
    }
    public static final MapCodec<BoostAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("ability_name").forGetter(BoostAbility::getName),
                    RangedAttribute.CODEC.fieldOf("cooldown").forGetter(BoostAbility::getCooldownLength),
                    RangedAttribute.CODEC.fieldOf("strength").forGetter(BoostAbility::getBoostStrength),
                    Codec.list(AbstractEffect.DIRECT_CODEC).fieldOf("effects").forGetter(BoostAbility::getEffects)
            ).apply(instance, BoostAbility::new)
    );

    //TODO move this to abstract ability...
    private List<AbstractEffect> getEffects() {
        return this.effects;
    }

    @Override
    public void OnActivate(Player p) {
        //TODO move this to abstract effect and perform on cooldown
        this.effects.forEach(effect -> effect.apply(p));

        if(this.CanPlayerUse(p)) {
            if(!this.IsOnCooldown(p))
            {
                this.setCooldown(p, getCooldownLength()); //TODO maybe make helper to calculate time based on ticks for find a different method (maybe include in the attribute???)

//                DimensionDelvers.LOGGER.info("BOOSTING!");
//                DimensionDelvers.LOGGER.info("Strength: " + String.valueOf((int)p.getAttributeValue(this.getBoostStrength())));
//                p.setDeltaMovement(p.getLookAngle().normalize().scale((int)p.getAttributeValue(this.getBoostStrength())));
//                ((ServerPlayer)p).connection.send(new ClientboundSetEntityMotionPacket(p)); //This is the secret sauce to making the movement work
            }

            //TODO clean this up, since we should just send this data on when the player joins the server. But for now, the player can just press the button to sync back up
            else {
                PacketDistributor.sendToPlayer((ServerPlayer) p, new CooldownActivated(this.getName().toString(),this.getCooldown(p) ));
            }

        }

        //this is an example of handing a case where the player cannot use an ability
        if(!this.CanPlayerUse(p))
        {
            ((ServerPlayer)p).sendSystemMessage(Component.literal("You Cannot use Boost!"));
        }
    }

    public Holder<Attribute> getBoostStrength() {
        return this.boostStrength;
    }



    @Override
    public void onDeactivate(Player p) {

    }

    @Override
    public void tick(Player p) {

    }


}
