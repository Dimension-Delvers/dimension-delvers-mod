package com.wanderersoftherift.wotr.abilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.abilities.effects.AbstractEffect;
import com.wanderersoftherift.wotr.networking.data.CooldownActivated;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class StandardAbility extends AbstractAbility{


    @Override
    public MapCodec<? extends AbstractAbility> getCodec() {
        return CODEC;
    }
    public static final MapCodec<StandardAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("ability_name").forGetter(StandardAbility::getName),
                    ResourceLocation.CODEC.fieldOf("icon").forGetter(StandardAbility::getIcon),
                    Codec.INT.fieldOf("cooldown").forGetter(ability -> (int) ability.getBaseCooldown()),
                    Codec.list(AbstractEffect.DIRECT_CODEC).fieldOf("effects").forGetter(AbstractAbility::getEffects)
            ).apply(instance, StandardAbility::new)
    );

    public StandardAbility(ResourceLocation resourceLocation, ResourceLocation icon, int baseCooldown, List<AbstractEffect> effects) {
        super(resourceLocation, icon, effects);
        this.baseCooldown = baseCooldown;
    }

    @Override
    public void OnActivate(Player player, int slot) {
        if(this.CanPlayerUse(player)) {
            if(!this.IsOnCooldown(player, slot))
            {
                this.getEffects().forEach(effect -> effect.apply(player, new ArrayList<>(), player));
                this.setCooldown(player, slot);
            }

            //TODO clean this up, since we should just send this data on when the player joins the server. But for now, the player can just press the button to sync back up
            else {
                PacketDistributor.sendToPlayer((ServerPlayer) player, new CooldownActivated(slot,this.getActiveCooldown(player, slot)));
            }

        }

        //this is an example of handing a case where the player cannot use an ability
        if(!this.CanPlayerUse(player))
        {
            ((ServerPlayer) player).sendSystemMessage(Component.literal("You cannot use this"));
        }
    }



    @Override
    public void onDeactivate(Player player, int slot) {

    }

    @Override
    public void tick(Player player) {

    }
}
