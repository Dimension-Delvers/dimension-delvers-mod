package com.dimensiondelvers.dimensiondelvers.abilities;

import com.dimensiondelvers.dimensiondelvers.abilities.effects.AbstractEffect;
import com.dimensiondelvers.dimensiondelvers.networking.data.CooldownActivated;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class StandardAbility extends AbstractAbility{

    public StandardAbility(ResourceLocation resourceLocation, Holder<Attribute> cooldown, List<AbstractEffect> effects) {
        super(resourceLocation, effects);
        this.cooldownAttribute = cooldown;
    }

    @Override
    public MapCodec<? extends AbstractAbility> getCodec() {
        return CODEC;
    }
    public static final MapCodec<StandardAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("ability_name").forGetter(StandardAbility::getName),
                    RangedAttribute.CODEC.fieldOf("cooldown").forGetter(StandardAbility::getCooldownLength),
                    Codec.list(AbstractEffect.DIRECT_CODEC).fieldOf("effects").forGetter(AbstractAbility::getEffects)
            ).apply(instance, StandardAbility::new)
    );

    @Override
    public void OnActivate(Player p) {
        //TODO move this to abstract effect and perform on cooldown
        this.getEffects().forEach(effect -> effect.apply(p, new ArrayList<>(), p));

        if(this.CanPlayerUse(p)) {
            if(!this.IsOnCooldown(p))
            {
                this.setCooldown(p, getCooldownLength()); //TODO maybe make helper to calculate time based on ticks for find a different method (maybe include in the attribute???)

            }

            //TODO clean this up, since we should just send this data on when the player joins the server. But for now, the player can just press the button to sync back up
            else {
                PacketDistributor.sendToPlayer((ServerPlayer) p, new CooldownActivated(this.getName().toString(),this.getCooldown(p) ));
            }

        }

        //this is an example of handing a case where the player cannot use an ability
        if(!this.CanPlayerUse(p))
        {
            ((ServerPlayer)p).sendSystemMessage(Component.literal("You cannot use this"));
        }
    }



    @Override
    public void onDeactivate(Player p) {

    }

    @Override
    public void tick(Player p) {

    }
}
