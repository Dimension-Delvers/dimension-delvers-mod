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
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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
    public void applyParticlesToUser(Entity user) {
        if(particles.isPresent() && user != null)
        {
            if(!user.level().isClientSide()) {
                ServerLevel level = (ServerLevel) user.level();
                applyParticlesToPos(level, user.position(), BuiltInRegistries.PARTICLE_TYPE.get(particles.get().getUserParticle()));
            }
        }
    }

    public void applyParticlesToTarget(Entity target) {
        if(particles.isPresent() && target != null)
        {
            if(!target.level().isClientSide()) {
                ServerLevel level = (ServerLevel) target.level();
                applyParticlesToPos(level, target.position(), BuiltInRegistries.PARTICLE_TYPE.get(particles.get().getTargetParticle()));
            }
        }
    }

    public void applyParticlesToPos(ServerLevel level, Vec3 position, Optional<Holder.Reference<ParticleType<?>>> particleType)
    {
         if(particleType.isPresent())
        {
            SimpleParticleType particle = (SimpleParticleType) particleType.get().value();
            level.sendParticles(particle, false, true, position.x, position.y + 1.5, position.z, 10, Math.random(), Math.random(), Math.random(), 2);
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
