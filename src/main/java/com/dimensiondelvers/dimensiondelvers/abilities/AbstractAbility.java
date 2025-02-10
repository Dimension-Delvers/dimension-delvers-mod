package com.dimensiondelvers.dimensiondelvers.abilities;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.enchantment.providers.EnchantmentProviderTypes;

public abstract class AbstractAbility {
    public MapCodec<? extends AbstractAbility> getCodec() {
     return null;
    }
    public ResourceLocation getAbilityType() {
        return DimensionDelvers.id("default");
    }




}
