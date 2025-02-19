package com.dimensiondelvers.dimensiondelvers.abilities.effects;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.abilities.BoostAbility;
import com.dimensiondelvers.dimensiondelvers.abilities.Targetting.EffectTargeting;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

import java.util.List;

public class HealEffect extends AbstractEffect{

    //TODO setup healing amount as part of the codec
    public static final MapCodec<HealEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    EffectTargeting.CODEC.fieldOf("targeting").forGetter(AbstractEffect::getTargeting),
                    Codec.list(AbstractEffect.DIRECT_CODEC).fieldOf("effects").forGetter(AbstractEffect::getEffects)
            ).apply(instance, HealEffect::new)
    );

    @Override
    public MapCodec<? extends AbstractEffect> getCodec() {
        return CODEC;
    }

    public HealEffect(EffectTargeting targeting, List<AbstractEffect> effects) {
        super(targeting, effects);
    }

    public void apply(Entity user) {
        List<Entity> targets = getTargeting().getTargets(user);
        //TODO do thing for this
        DimensionDelvers.LOGGER.info("Healing: " + targets.size());
        for(Entity e: targets) {
            if(e instanceof LivingEntity living)
            {
                living.heal(2.5f);
            }
            //Then apply children affects to targets
            super.apply(e);
        }
    }
}
