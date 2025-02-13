package com.dimensiondelvers.dimensiondelvers.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.function.Function;

public class ModCodecs {
    public static final Codec<BlockState> BLOCK_STATE_CODEC = Codec.either(
            BuiltInRegistries.BLOCK.byNameCodec(),
            BlockState.CODEC
    ).xmap(either -> either.map(Block::defaultBlockState, Function.identity()), entry -> entry == entry.getBlock().defaultBlockState() ? Either.left(entry.getBlock()) : Either.right(entry));

    /*private static final Codec<BlockState> BLOCK_STATE_INNER_CODEC = BuiltInRegistries.BLOCK.byNameCodec().dispatch(
            "Name",
            block1 -> block1,
            p_338076_ -> {
                BlockState s = p_338076_.defaultBlockState();
                return s.getValues().isEmpty()
                        ? MapCodec.unit(s)
                        : s.propertiesCodec.codec().lenientOptionalFieldOf("Properties").xmap(p_187544_ -> p_187544_.orElse(s), Optional::of);
            }
    ).xmap(data ->);
    private static record BlockStateData(Block block, ){}*/
}
