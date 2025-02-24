package com.dimensiondelvers.dimensiondelvers.block.entity;

import com.dimensiondelvers.dimensiondelvers.init.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.chunk.RenderRegionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class DittoBlockEntityRenderer implements BlockEntityRenderer<DittoBlockEntity> {
	private final BlockRenderDispatcher dispatcher;

	public DittoBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		this.dispatcher = context.getBlockRenderDispatcher();
	}

	@Override
	public int getViewDistance() {
		return 256;
	}

	@Override
	public void render(@NotNull DittoBlockEntity blockEntity, float partialTick, PoseStack stack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		DittoBlock dittoBlock = (DittoBlock)blockEntity.getBlockState().getBlock();
		if (!dittoBlock.shouldRender(blockEntity.getBlockState())) {
			return;
		}
		Vec3 color = dittoBlock.getTint(blockEntity.getBlockState());
		if ((blockEntity.getTheItem().getItem() instanceof BlockItem) && blockEntity.getTheItem().getItem() != ModItems.DITTO_BLOCK_ITEM.asItem()) {
			BlockState blockstate = ((BlockItem)blockEntity.getTheItem().getItem()).getBlock().defaultBlockState();
			if (blockEntity.getLevel() == null) return;

			RenderType rendertype = blockstate.isSolidRender() ? RenderType.cutout() : RenderType.translucent();

			this.dispatcher.getModelRenderer().tesselateBlock(
					blockEntity.getLevel(),
					dispatcher.getBlockModel(blockstate),
					blockstate,
					blockEntity.getBlockPos(),
					stack,
					bufferSource.getBuffer(rendertype),
					false,
					RandomSource.create(),
					blockstate.getSeed(blockEntity.getBlockPos()),
					packedOverlay,
					ModelData.EMPTY,
					rendertype
			);
			//this.dispatcher.renderBreakingTexture(blockEntity.getBlockState(), blockEntity.getBlockPos(), blockEntity.getLevel(), stack, bufferSource.getBuffer(rendertype), ModelData.EMPTY);
		} else {
			this.dispatcher.getModelRenderer().tesselateBlock(
					blockEntity.getLevel(),
					dispatcher.getBlockModel(blockEntity.getBlockState()),
					blockEntity.getBlockState(),
					blockEntity.getBlockPos(),
					stack,
					bufferSource.getBuffer(RenderType.CUTOUT),
					false,
					RandomSource.create(),
					blockEntity.getBlockState().getSeed(blockEntity.getBlockPos()),
					packedOverlay,
					ModelData.EMPTY,
					RenderType.CUTOUT
			);
		}
	}
}
