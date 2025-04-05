package com.wanderersoftherift.wotr.abilities.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.abilities.Targeting.AbstractTargeting;
import com.wanderersoftherift.wotr.abilities.effects.util.ParticleInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Optional;

public class SoundEffect extends AbstractEffect {

    public static final MapCodec<SoundEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            AbstractEffect.commonFields(instance).and(
                    SoundEvent.CODEC.fieldOf("sound").forGetter(SoundEffect::getSound)
            ).apply(instance, SoundEffect::new)
    );

    private final Holder<SoundEvent> sound;

    public SoundEffect(AbstractTargeting targeting, List<AbstractEffect> effects, Optional<ParticleInfo> particles, Holder<SoundEvent> sound) {
        super(targeting, effects, particles);
        this.sound = sound;
    }

    @Override
    public void apply(Entity user, List<BlockPos> blocks, EffectContext context) {
        List<Entity> targets = getTargeting().getTargets(user, blocks, context);
        applyParticlesToUser(user);

        for(Entity target: targets) {
            applyParticlesToTarget(target);

            SoundSource source;
            if (context.caster() instanceof Player) {
                source = SoundSource.PLAYERS;
            } else {
                source = SoundSource.HOSTILE;
            }
            target.level().playSound(null, target, sound.value(), source, 1.0f, 1.0f);

            //Then apply children affects to targets
            super.apply(target, getTargeting().getBlocks(user), context);
        }

        for (BlockPos pos : blocks) {
            context.level().playSound(null, pos, sound.value(), SoundSource.BLOCKS);
        }

        if(targets.isEmpty())
        {
            super.apply(null, getTargeting().getBlocks(user), context);
        }
    }

    @Override
    public MapCodec<? extends AbstractEffect> getCodec() {
        return CODEC;
    }

    public Holder<SoundEvent> getSound() {
        return sound;
    }
}
