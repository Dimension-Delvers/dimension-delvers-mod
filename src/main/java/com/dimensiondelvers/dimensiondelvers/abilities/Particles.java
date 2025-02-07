package com.dimensiondelvers.dimensiondelvers.abilities;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.networking.data.CooldownActivated;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

public class Particles extends AbstractDurationAbility{
    public Particles(ResourceLocation abilityName) {
        super(abilityName);
    }

    @Override
    public void OnActivate(Player p) {
        if(this.CanPlayerUse(p)) {
            this.setDuration(p, AbilityAttributes.PARTICLE_TIME); //TODO maybe make helper to calculate time based on ticks for find a different method (maybe include in the attribute???)

            p.sendSystemMessage(Component.literal("Making you beautiful!"));

            //TODO sync durations back
            //PacketDistributor.sendToPlayer((ServerPlayer) p, new CooldownActivated(this.GetName().toString(),(int)p.getAttributeValue(AbilityAttributes.BOOST_COOLDOWN) * 20 ));

            return;
        }

        //this is an example of handing a case where the player cannot use an ability
        if(!this.CanPlayerUse(p))
        {
            p.sendSystemMessage(Component.literal("You Cannot use paticles!"));
        }
    }

    @Override
    public void OnDeactivate(Player p) {
        p.sendSystemMessage(Component.literal("You're no longer beautiful!"));
    }

    @Override
    public void Tick(Player p) {
        p.level().addParticle(ParticleTypes.CHERRY_LEAVES, p.position().x, p.position().y + 2, p.position().z, 1, 1, 1);
    }
}
