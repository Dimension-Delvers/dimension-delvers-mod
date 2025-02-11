package com.dimensiondelvers.dimensiondelvers.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;


// Create the Codec for the modifier
public class ModifierInstance {

    public static Codec<ModifierInstance> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            AbstractModifier.CODEC.fieldOf("modifier").forGetter(ModifierInstance::getModifier),
            Codec.FLOAT.fieldOf("roll").forGetter(ModifierInstance::getRoll)
    ).apply(inst, ModifierInstance::new));

    public final Holder<AbstractModifier> modifier;

    public final float roll;

    public ModifierInstance(Holder<AbstractModifier> modifier, float roll) {
        this.modifier = modifier;
        this.roll = roll;
    }

    public Holder<AbstractModifier> getModifier() {
        return modifier;
    }

    public float getRoll() {
        return roll;
    }
}
