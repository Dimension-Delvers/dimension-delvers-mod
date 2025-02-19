package com.dimensiondelvers.dimensiondelvers.abilities.effects;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.abilities.Targetting.EffectTargeting;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class DamageEffect extends AbstractEffect{
    public DamageEffect(EffectTargeting targeting, List<AbstractEffect> effects) {
        super(targeting, effects);
    }

    public static final MapCodec<DamageEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    EffectTargeting.CODEC.fieldOf("targeting").forGetter(AbstractEffect::getTargeting),
                    Codec.list(AbstractEffect.DIRECT_CODEC).fieldOf("effects").forGetter(AbstractEffect::getEffects)
            ).apply(instance, DamageEffect::new)
    );

    @Override
    public MapCodec<? extends AbstractEffect> getCodec() {
        return CODEC;
    }

    public void apply(Entity user) {
        List<Entity> targets = getTargeting().getTargets(user);
        //TODO do thing for this
        for(Entity e: targets) {
            if(e instanceof LivingEntity living)
            {
                living.hurtServer((ServerLevel) user.level(), user.damageSources().generic(), 2.5f);
            }
            //Then apply children affects to targets
            super.apply(e);
        }
    }
}
