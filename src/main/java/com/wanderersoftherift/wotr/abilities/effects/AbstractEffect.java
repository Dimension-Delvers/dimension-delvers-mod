package com.wanderersoftherift.wotr.abilities.effects;

import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.Registries.AbilityRegistry;
import com.wanderersoftherift.wotr.abilities.Targeting.AbstractTargeting;
import com.wanderersoftherift.wotr.abilities.effects.util.ParticleInfo;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractEffect {

    protected static <T extends AbstractEffect> Products.P3<RecordCodecBuilder.Mu<T>, AbstractTargeting, List<AbstractEffect>, Optional<ParticleInfo>> commonFields(RecordCodecBuilder.Instance<T> instance) {
        return instance.group(
                AbstractTargeting.DIRECT_CODEC.fieldOf("targeting").forGetter(AbstractEffect::getTargeting),
                Codec.list(AbstractEffect.DIRECT_CODEC).fieldOf("effects").forGetter(AbstractEffect::getEffects),
                Codec.optionalField("particles", ParticleInfo.CODEC.codec(), true).forGetter(AbstractEffect::getParticles)
        );
    }

    public abstract MapCodec<? extends AbstractEffect> getCodec();

    public static final Codec<AbstractEffect> DIRECT_CODEC = AbilityRegistry.EFFECTS_REGISTRY.byNameCodec().dispatch(AbstractEffect::getCodec, Function.identity());
    private final AbstractTargeting targeting;
    private final List<AbstractEffect> effects;
    private final Optional<ParticleInfo> particles;
    public AbstractEffect(AbstractTargeting targeting, List<AbstractEffect> effects, Optional<ParticleInfo> particles)
    {
        this.targeting = targeting;
        this.effects = effects;
        this.particles = particles;
    }
    public void apply(Entity user, List<BlockPos> blocks, LivingEntity caster) {

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

    public AbstractTargeting getTargeting() {
        return targeting;
    }

    public List<AbstractEffect> getEffects() {
        return this.effects;
    }

    public Optional<ParticleInfo> getParticles() {
        return this.particles;
    }

    public Set<Holder<Attribute>> getApplicableAttributes(){
        return getEffects().stream().map(AbstractEffect::getApplicableAttributes).flatMap(Set::stream).collect(Collectors.toSet());
    };

}
