package com.dimensiondelvers.dimensiondelvers.abilities.effects;

import com.dimensiondelvers.dimensiondelvers.Registries.AbilityRegistry;
import com.dimensiondelvers.dimensiondelvers.abilities.Targetting.EffectTargeting;
import com.dimensiondelvers.dimensiondelvers.abilities.effects.util.ParticleInfo;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

//TODO maybe look into how we can make this abstract
public abstract class AbstractEffect {


    public abstract MapCodec<? extends AbstractEffect> getCodec();

    public static final Codec<AbstractEffect> DIRECT_CODEC = AbilityRegistry.EFFECTS_REGISTRY.byNameCodec().dispatch(AbstractEffect::getCodec, Function.identity());
    private final EffectTargeting targeting;
    private final List<AbstractEffect> effects;
    private final Optional<ParticleInfo> particles;
    public AbstractEffect(EffectTargeting targeting, List<AbstractEffect> effects, Optional<ParticleInfo> particles)
    {
        this.targeting = targeting;
        this.effects = effects;
        this.particles = particles;
    }
    public void apply(Entity user, List<BlockPos> blocks, Player caster) {

        for(AbstractEffect effect: getEffects())
        {
            effect.apply(user, blocks, caster);
        }


    };

    //TODO consolidate this code below
    public void applyPariclesToUser(Entity user) {
        if(particles.isPresent())
        {
            if(!user.level().isClientSide()) {
                ServerLevel level = (ServerLevel) user.level();
                Optional<Holder.Reference<ParticleType<?>>> particleType = BuiltInRegistries.PARTICLE_TYPE.get(particles.get().getUserParticle());
                if(particleType.isPresent())
                {
                    SimpleParticleType particle = (SimpleParticleType) particleType.get().value();
                    level.sendParticles(particle,false, true,  user.position().x, user.position().y + 1.5, user.position().z, 10, Math.random(), Math.random(), Math.random(), 2);
                }
            }
        }
    }

    public void applyPariclesToTarget(Entity target) {
        if(particles.isPresent())
        {
            if(!target.level().isClientSide()) {
                ServerLevel level = (ServerLevel) target.level();
                Optional<Holder.Reference<ParticleType<?>>> particleType = BuiltInRegistries.PARTICLE_TYPE.get(particles.get().getTargetParticle());
                if(particleType.isPresent())
                {
                    SimpleParticleType particle = (SimpleParticleType) particleType.get().value();
                    level.sendParticles(particle, false, true, target.position().x, target.position().y + 1.5, target.position().z, 10, Math.random(), Math.random(), Math.random(), 2);
                }
            }
        }
    }

    public EffectTargeting getTargeting() {
        return targeting;
    }

    public List<AbstractEffect> getEffects() {
        return this.effects;
    }

    public Optional<ParticleInfo> getParticles() {
        return this.particles;
    }

}
