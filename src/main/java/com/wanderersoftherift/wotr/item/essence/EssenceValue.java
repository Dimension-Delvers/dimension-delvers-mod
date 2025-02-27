package com.wanderersoftherift.wotr.item.essence;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.init.ModEssenceTypes;

/**
 * Data Map capturing the essence value of an item
 * @param type The type of essence the item provides
 * @param value The quantity of essence
 */
public record EssenceValue(EssenceType type, int value) {
    public static final Codec<EssenceValue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ModEssenceTypes.ESSENCE_TYPE_REGISTRY.byNameCodec().fieldOf("type").forGetter(EssenceValue::type),
            Codec.INT.fieldOf("value").forGetter(EssenceValue::value)
    ).apply(instance, EssenceValue::new));
}
