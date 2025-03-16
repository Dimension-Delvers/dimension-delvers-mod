package com.wanderersoftherift.wotr.block;

import com.mojang.serialization.MapCodec;
import com.wanderersoftherift.wotr.block.blockentity.SkyboxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class SkyboxBlock extends BaseEntityBlock {
	public static final MapCodec<SkyboxBlock> CODEC = simpleCodec(SkyboxBlock::new);

	public MapCodec<SkyboxBlock> codec() {
		return CODEC;
	}

	public SkyboxBlock(BlockBehaviour.Properties p_273064_) {
		super(p_273064_);
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Nullable
	public BlockEntity newBlockEntity(BlockPos p_273396_, BlockState p_272674_) {
		return new SkyboxBlockEntity(p_273396_, p_272674_);
	}
}
