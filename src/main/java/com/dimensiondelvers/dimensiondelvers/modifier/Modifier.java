package com.dimensiondelvers.dimensiondelvers.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class Modifier extends AbstractModifier {

    private final Attribute attribute;
    // private final ObjectArrayList<Integer> rolls;
    private final int minRoll;
    private final int maxRoll;

    public Modifier(Attribute attribute, int minRoll, int maxRoll) {

        this.attribute = attribute;
        this.minRoll = minRoll;
        this.maxRoll = maxRoll;
    }

    // Still being worked on, reference modifier.ModifierInstance
    @Override
    public MapCodec<? extends AbstractModifier> getCodec() {
        return MODIFIER_CODEC;
    }

    public static final MapCodec<Modifier> MODIFIER_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ATTRIBUTE.byNameCodec().fieldOf("attribute").forGetter(Modifier::getAttribute),
            Codec.INT.fieldOf("minRoll").forGetter(Modifier::getMinimumRoll),
            Codec.INT.fieldOf("maxRoll").forGetter(Modifier::getMaximumRoll)
            ).apply(instance, Modifier::new)
    );

    @Override
    public void applyModifier() {

    }

    public Attribute getAttribute(){
        return attribute;
    }

    public int getMinimumRoll(){
        return minRoll;
    }

    public int getMaximumRoll(){
        return maxRoll;
    }
}
