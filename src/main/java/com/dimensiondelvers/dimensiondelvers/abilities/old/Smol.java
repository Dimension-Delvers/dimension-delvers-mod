package com.dimensiondelvers.dimensiondelvers.abilities.old;

import com.dimensiondelvers.dimensiondelvers.abilities.AbilityAttributes;
import com.dimensiondelvers.dimensiondelvers.abilities.AbstractAbility;
import com.dimensiondelvers.dimensiondelvers.init.ModAbilities;
import com.dimensiondelvers.dimensiondelvers.networking.data.CooldownActivated;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;


public class Smol extends AbstractAbility {
    public Smol(ResourceLocation abilityName) {
        super(abilityName);
        this.setIcon(ResourceLocation.withDefaultNamespace("textures/mob_effect/infested.png"));
    }

    @Override
    public MapCodec<? extends AbstractAbility> getCodec() {
        return null;
    }

    /*
     * Going forward have to look how to better implement a combo of CD and Duration.
     * Right now, there is redundant code from AbstractCooldownAbility and here.
     */
    @Override
    public void OnActivate(Player p) {
        if(this.CanPlayerUse(p) && !this.IsOnCooldown(p)) {
            this.setCooldown(p, getCooldownLength());
            this.setDuration(p, AbilityAttributes.SMOL_TIME); //TODO maybe make helper to calculate time based on ticks for find a different method (maybe include in the attribute???)

            ((ServerPlayer)p).sendSystemMessage(Component.literal("Making you smol!"));
            p.getAttribute(Attributes.SCALE).addOrReplacePermanentModifier(AbilityAttributes.SMOL_MODIFIER);
            return;
        }

        //this is an example of handing a case where the player cannot use an ability
        if(!this.CanPlayerUse(p))
        {
            ((ServerPlayer)p).sendSystemMessage(Component.literal("You cannot be smol!"));
        }
    }

    @Override
    public void onDeactivate(Player p) {
        p.getAttribute(Attributes.SCALE).removeModifier(AbilityAttributes.SMOL_MODIFIER);
        ((ServerPlayer)p).sendSystemMessage(Component.literal("You're no longer smol!"));
    }

    @Override
    public void tick(Player p) {
    }

    @Override
    public boolean IsOnCooldown(Player p) {
        //If we registered this ability as one that has a cooldown and the player has a cooldown active for this ability.
//        return ModAbilities.COOL_DOWN_ATTACHMENTS.containsKey(this.getName()) && p.getData(ModAbilities.COOL_DOWN_ATTACHMENTS.get(this.getName())) > 0;
        return false;
    }

    @Override
    public void setCooldown(Player p, Holder<Attribute> attribute) {
//        p.setData(COOL_DOWN_ATTACHMENTS.get(this.getName()), (int)p.getAttributeValue(attribute) * 20); //TODO maybe make helper to calculate time based on ticks for find a different method (maybe include in the attribute???)
        PacketDistributor.sendToPlayer((ServerPlayer) p, new CooldownActivated(this.getName().toString(),(int)p.getAttributeValue(getCooldownLength()) * 20 ));
    }

    @Override
    public DeferredHolder<Attribute, RangedAttribute> getCooldownLength() {
        return AbilityAttributes.SMOL_COOLDOWN;
    }
}
