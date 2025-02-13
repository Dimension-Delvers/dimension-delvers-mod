package com.dimensiondelvers.dimensiondelvers.ability;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.mojang.serialization.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;

import static com.dimensiondelvers.dimensiondelvers.init.ModAbilityTypes.ABILITY_TYPES_REGISTRY;

public abstract class AbstractAbility {
    public abstract MapCodec<? extends AbstractAbility> getCodec();

    public abstract void activateAbility(Player player);

    public static final Codec<AbstractAbility> DIRECT_CODEC = ABILITY_TYPES_REGISTRY.byNameCodec().dispatch(AbstractAbility::getCodec, Function.identity());
}
