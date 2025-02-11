package com.dimensiondelvers.dimensiondelvers.modifier;

import com.mojang.serialization.*;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;

import java.util.function.Function;

import static com.dimensiondelvers.dimensiondelvers.init.ModAbstractModifiers.MODIFIER_KEY;
import static com.dimensiondelvers.dimensiondelvers.init.ModAbstractModifiers.MODIFIER_TYPE_REGISTRY;


public abstract class AbstractModifier {
    public abstract MapCodec<? extends AbstractModifier> getCodec();

    public abstract void applyModifier();

    public static final Codec<AbstractModifier> DIRECT_CODEC = MODIFIER_TYPE_REGISTRY.byNameCodec().dispatch(AbstractModifier::getCodec, Function.identity());

    public static final Codec<Holder<AbstractModifier>> CODEC = RegistryFixedCodec.create(MODIFIER_KEY);

}
