package com.dimensiondelvers.dimensiondelvers.abilities;

import com.dimensiondelvers.dimensiondelvers.abilities.types.CooldownAbility;
import com.dimensiondelvers.dimensiondelvers.init.ModAbilities;
import com.dimensiondelvers.dimensiondelvers.networking.data.CooldownActivated;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.dimensiondelvers.dimensiondelvers.init.ModAbilities.COOL_DOWN_ATTACHMENTS;

public class Smol extends AbstractDurationAbility implements CooldownAbility {
    public Smol(ResourceLocation abilityName) {
        super(abilityName);
        this.setIcon(ResourceLocation.withDefaultNamespace("textures/mob_effect/infested.png"));
    }

    /*
     * Going forward have to look how to better implement a combo of CD and Duration.
     * Right now, there is redundant code from AbstractCooldownAbility and here.
     */
    @Override
    public void OnActivate(Player p) {
        if(this.CanPlayerUse(p) && !this.IsOnCooldown(p)) {
            this.SetCooldown(p, GetCooldownLength());
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
        return ModAbilities.COOL_DOWN_ATTACHMENTS.containsKey(this.getName()) && p.getData(ModAbilities.COOL_DOWN_ATTACHMENTS.get(this.getName())) > 0;
    }

    @Override
    public void SetCooldown(Player p, DeferredHolder<Attribute, RangedAttribute> attribute) {
        p.setData(COOL_DOWN_ATTACHMENTS.get(this.getName()), (int)p.getAttributeValue(attribute) * 20); //TODO maybe make helper to calculate time based on ticks for find a different method (maybe include in the attribute???)
        PacketDistributor.sendToPlayer((ServerPlayer) p, new CooldownActivated(this.getName().toString(),(int)p.getAttributeValue(GetCooldownLength()) * 20 ));
    }

    @Override
    public DeferredHolder<Attribute, RangedAttribute> GetCooldownLength() {
        return AbilityAttributes.SMOL_COOLDOWN;
    }
}
