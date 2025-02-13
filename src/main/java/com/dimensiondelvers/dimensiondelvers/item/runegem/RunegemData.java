package com.dimensiondelvers.dimensiondelvers.item.runegem;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

public record RunegemData(RuneGemShape shape, TagKey<Enchantment> tag, RuneGemTier tier) {
    public static Codec<RunegemData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            RuneGemShape.CODEC.fieldOf("shape").forGetter(RunegemData::shape),
            TagKey.codec(Registries.ENCHANTMENT).fieldOf("tag").forGetter(RunegemData::tag),
            RuneGemTier.CODEC.fieldOf("tier").forGetter(RunegemData::tier)
    ).apply(inst, RunegemData::new));
}
