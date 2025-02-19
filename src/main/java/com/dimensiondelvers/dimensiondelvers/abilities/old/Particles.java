package com.dimensiondelvers.dimensiondelvers.abilities.old;

import com.dimensiondelvers.dimensiondelvers.abilities.AbilityAttributes;
import com.dimensiondelvers.dimensiondelvers.abilities.AbstractAbility;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.registries.DeferredHolder;

public class Particles extends AbstractAbility {
    public Particles(ResourceLocation abilityName) {
        super(abilityName, null);
    }

    @Override
    public MapCodec<? extends AbstractAbility> getCodec() {
        return null;
    }

    @Override
    public void OnActivate(Player p) {
        if(this.CanPlayerUse(p)) {
            this.setDuration(p, AbilityAttributes.PARTICLE_TIME); //TODO maybe make helper to calculate time based on ticks for find a different method (maybe include in the attribute???)

            ((ServerPlayer)p).sendSystemMessage(Component.literal("Making you beautiful!"));

            //TODO sync durations back
            //PacketDistributor.sendToPlayer((ServerPlayer) p, new CooldownActivated(this.GetName().toString(),(int)p.getAttributeValue(AbilityAttributes.BOOST_COOLDOWN) * 20 ));

            return;
        }

        //this is an example of handing a case where the player cannot use an ability
        if(!this.CanPlayerUse(p))
        {
            ((ServerPlayer)p).sendSystemMessage(Component.literal("You Cannot use paticles!"));
        }
    }

    @Override
    public void onDeactivate(Player p) {
        ((ServerPlayer)p).sendSystemMessage(Component.literal("You're no longer beautiful!"));
    }

    @Override
    public DeferredHolder<Attribute, RangedAttribute> getCooldownLength() {
        return null;
    }

    @Override
    public void tick(Player p) {
        p.heal(0.05F);
        ServerLevel level = (ServerLevel) p.level();
        level.sendParticles(ParticleTypes.CHERRY_LEAVES, p.position().x, p.position().y + 1.5, p.position().z, 1, Math.random(), Math.random(), Math.random(), 2);
    }
}
