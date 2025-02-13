package com.dimensiondelvers.dimensiondelvers.item.gearmodifier;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.util.ExtraCodecs;


// Vanilla Equivalent = Enchantment
public record GearModifier(GearModifierDefinition definition, DataComponentMap effects) {

   /* public static final Codec<GearModifier> DIRECT_CODEC = RecordCodecBuilder.create(
            p_344998_ -> p_344998_.group(
                            GearModifier.GearModifierDefinition.CODEC.forGetter(GearModifier::definition),
                            EnchantmentEffectComponents.CODEC.optionalFieldOf("effects", DataComponentMap.EMPTY).forGetter(GearModifier::effects)
                    )
                    .apply(p_344998_, GearModifier::new)
    );
    public static final Codec<Holder<GearModifier>> CODEC = RegistryFixedCodec.create(Registries.ENCHANTMENT);*/


    public static record GearModifierDefinition(
            int weight
    ) {
        public static final MapCodec<GearModifier.GearModifierDefinition> CODEC = RecordCodecBuilder.mapCodec(
                p_344890_ -> p_344890_.group(
                                ExtraCodecs.intRange(1, 1024).fieldOf("weight").forGetter(GearModifier.GearModifierDefinition::weight)
                        )
                        .apply(p_344890_, GearModifier.GearModifierDefinition::new)
        );
    }
}
