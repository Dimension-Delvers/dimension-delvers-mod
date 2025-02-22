package com.dimensiondelvers.dimensiondelvers.abilities.effects;

import com.dimensiondelvers.dimensiondelvers.abilities.Targetting.EffectTargeting;
import com.dimensiondelvers.dimensiondelvers.abilities.effects.util.ParticleInfo;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Optional;

public class DamageEffect extends AbstractEffect{
    private float damageAmount = 0;
    public DamageEffect(EffectTargeting targeting, List<AbstractEffect> effects, Optional<ParticleInfo> particles, float amount) {
        super(targeting, effects, particles);
        this.damageAmount = amount;
    }

    public static final MapCodec<DamageEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    EffectTargeting.CODEC.fieldOf("targeting").forGetter(AbstractEffect::getTargeting),
                    Codec.list(AbstractEffect.DIRECT_CODEC).fieldOf("effects").forGetter(AbstractEffect::getEffects),
                    Codec.optionalField("particles", ParticleInfo.CODEC.codec(), true).forGetter(AbstractEffect::getParticles),
                    Codec.FLOAT.fieldOf("amount").forGetter(DamageEffect::getAmount)
            ).apply(instance, DamageEffect::new)
    );

    private float getAmount() {
        return damageAmount;
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
                livingTarget.hurtServer((ServerLevel) user.level(), user.damageSources().generic(), damageAmount);
            }
            //Then apply children affects to targets
            super.apply(target, getTargeting().getBlocks(user), caster);
        }

        if(targets.isEmpty())
        {
            super.apply(null, getTargeting().getBlocks(user), caster);
        }
    }
}
