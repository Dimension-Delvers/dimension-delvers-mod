package com.dimensiondelvers.dimensiondelvers.abilities.effects;

import com.dimensiondelvers.dimensiondelvers.Registries.AbilityRegistry;
import com.dimensiondelvers.dimensiondelvers.abilities.AbstractAbility;
import com.dimensiondelvers.dimensiondelvers.abilities.Targetting.EffectTargeting;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;

import java.util.List;
import java.util.function.Function;

//TODO maybe look into how we can make this abstract
public abstract class AbstractEffect {


    public abstract MapCodec<? extends AbstractEffect> getCodec();

    public static final Codec<AbstractEffect> DIRECT_CODEC = AbilityRegistry.EFFECTS_REGISTRY.byNameCodec().dispatch(AbstractEffect::getCodec, Function.identity());
    private final EffectTargeting targeting;
    private final List<AbstractEffect> effects;
    public AbstractEffect(EffectTargeting targeting, List<AbstractEffect> effects)
    {
        this.targeting = targeting;
        this.effects = effects;
    }
    public void apply(Entity user) {
        for(AbstractEffect effect: getEffects())
        {
            effect.apply(user);
        }
    };

    public EffectTargeting getTargeting() {
        return targeting;
    }

    public List<AbstractEffect> getEffects() {
        return this.effects;
    }

}
