package com.wanderersoftherift.wotr.modifier.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.wanderersoftherift.wotr.modifier.source.ModifierSource;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;

import static com.wanderersoftherift.wotr.init.ModModifierEffects.MODIFIER_EFFECT_KEY;
import static com.wanderersoftherift.wotr.init.ModModifierEffects.MODIFIER_TYPE_REGISTRY;


public abstract class AbstractModifierEffect {
    public abstract MapCodec<? extends AbstractModifierEffect> getCodec();

    public abstract void enableModifier(float roll, Entity entity,  ModifierSource source);

    public abstract void disableModifier(float roll, Entity entity, ModifierSource source);

    public abstract void applyModifier();

    public static final Codec<AbstractModifierEffect> DIRECT_CODEC = MODIFIER_TYPE_REGISTRY.byNameCodec().dispatch(AbstractModifierEffect::getCodec, Function.identity());

    public static final Codec<Holder<AbstractModifierEffect>> CODEC = RegistryFixedCodec.create(MODIFIER_EFFECT_KEY);

}
