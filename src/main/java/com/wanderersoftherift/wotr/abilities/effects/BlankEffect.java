package com.wanderersoftherift.wotr.abilities.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.abilities.EffectContext;
import com.wanderersoftherift.wotr.abilities.Targeting.AbstractTargeting;
import com.wanderersoftherift.wotr.abilities.effects.util.ParticleInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;

import java.util.List;
import java.util.Optional;

public class BlankEffect extends AbstractEffect{

    //TODO setup healing amount as part of the codec
    public static final MapCodec<BlankEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            AbstractEffect.commonFields(instance).apply(instance, BlankEffect::new)
    );

    @Override
    public MapCodec<? extends AbstractEffect> getCodec() {
        return CODEC;
    }

    public BlankEffect(AbstractTargeting targeting, List<AbstractEffect> effects, Optional<ParticleInfo> particles) {
        super(targeting, effects, particles);
    }

    @Override
    public void apply(Entity user, List<BlockPos> blocks, EffectContext context) {
        List<Entity> targets = getTargeting().getTargets(user, blocks, context);
        applyParticlesToUser(user);

        for(Entity target: targets) {
            applyParticlesToTarget(target);
            //Then apply children affects to targets
            super.apply(target, getTargeting().getBlocks(user), context);
        }


        if(targets.isEmpty())
        {
            super.apply(null, getTargeting().getBlocks(user), context);
        }
    }
}
