package com.wanderersoftherift.wotr.item.essence;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

/**
 * Data Map capturing the essence value of an item
 * @param type The type of essence the item provides
 * @param value The quantity of essence
 */
public record EssenceValue(ResourceLocation type, int value) {
    public static final Codec<EssenceValue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("type").forGetter(EssenceValue::type),
            Codec.INT.fieldOf("value").forGetter(EssenceValue::value)
    ).apply(instance, EssenceValue::new));

    public static final String ESSENCE_TYPE_PREFIX = "essence_type";
}
