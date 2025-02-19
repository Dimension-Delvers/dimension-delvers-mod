package com.dimensiondelvers.dimensiondelvers.abilities.effects;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.abilities.Targetting.EffectTargeting;
import com.dimensiondelvers.dimensiondelvers.abilities.effects.util.ParticleInfo;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Optional;

public class DamageEffect extends AbstractEffect{
    public DamageEffect(EffectTargeting targeting, List<AbstractEffect> effects, Optional<ParticleInfo> particles) {
        super(targeting, effects, particles);
    }

    public static final MapCodec<DamageEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    EffectTargeting.CODEC.fieldOf("targeting").forGetter(AbstractEffect::getTargeting),
                    Codec.list(AbstractEffect.DIRECT_CODEC).fieldOf("effects").forGetter(AbstractEffect::getEffects),
                    Codec.optionalField("particles", ParticleInfo.CODEC.codec(), true).forGetter(AbstractEffect::getParticles)
            ).apply(instance, DamageEffect::new)
    );

    @Override
    public MapCodec<? extends AbstractEffect> getCodec() {
        return CODEC;
    }

    public void apply(Entity user) {
        List<Entity> targets = getTargeting().getTargets(user);
        applyPariclesToUser(user);
        //TODO do thing for this
        for(Entity target: targets) {
            applyPariclesToTarget(target);
            if(target instanceof LivingEntity livingTarget)
            {
                livingTarget.hurtServer((ServerLevel) user.level(), user.damageSources().generic(), 2.5f);
            }
            //Then apply children affects to targets
            super.apply(target);
        }
    }
}
