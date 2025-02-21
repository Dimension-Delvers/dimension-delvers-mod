package com.dimensiondelvers.dimensiondelvers.abilities.effects;

import com.dimensiondelvers.dimensiondelvers.abilities.Targetting.EffectTargeting;
import com.dimensiondelvers.dimensiondelvers.abilities.effects.util.ParticleInfo;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Optional;

public class ApplyStatusEffect extends AbstractEffect{

    MobEffectInstance statusEffect;
    public ApplyStatusEffect(EffectTargeting targeting, List<AbstractEffect> effects, Optional<ParticleInfo> particles, MobEffectInstance status) {
        super(targeting, effects, particles);
        this.statusEffect = status;
    }

    public static final MapCodec<ApplyStatusEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    EffectTargeting.CODEC.fieldOf("targeting").forGetter(AbstractEffect::getTargeting),
                    Codec.list(AbstractEffect.DIRECT_CODEC).fieldOf("effects").forGetter(AbstractEffect::getEffects),
                    Codec.optionalField("particles", ParticleInfo.CODEC.codec(), true).forGetter(AbstractEffect::getParticles),
                    MobEffectInstance.CODEC.fieldOf("status_effect").forGetter(ApplyStatusEffect::getStatusEffect)
            ).apply(instance, ApplyStatusEffect::new)
    );

    public MobEffectInstance getStatusEffect() {
        return this.statusEffect;
    }

    @Override
    public MapCodec<? extends AbstractEffect> getCodec() {
        return CODEC;
    }

    @Override
    public void apply(Entity user, List<BlockPos> blocks, Player caster) {
        List<Entity> targets = getTargeting().getTargets(user, blocks, caster);
        applyParticlesToUser(user);

        for(Entity target: targets) {
            applyParticlesToTarget(target);
            if(target instanceof LivingEntity livingTarget)
            {
                //TODO look into creating our own mob effect wrapper that can also call an effect list
                livingTarget.addEffect(new MobEffectInstance(getStatusEffect()));
            }

            //Then apply children effects to targets
            super.apply(target, getTargeting().getBlocks(user), caster);
        }

        if(targets.isEmpty())
        {
            super.apply(null, getTargeting().getBlocks(user), caster);
        }
    }
}
