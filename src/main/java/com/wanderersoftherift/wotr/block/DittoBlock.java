package com.wanderersoftherift.wotr.block;

import com.mojang.serialization.MapCodec;
import com.wanderersoftherift.wotr.block.blockentity.DittoBlockEntity;
import com.wanderersoftherift.wotr.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.registries.DeferredBlock;

import javax.annotation.Nullable;

public class DittoBlock extends BaseEntityBlock {
	public static final MapCodec<DittoBlock> CODEC = simpleCodec(DittoBlock::new);
	public static final BooleanProperty HAS_ITEM = BooleanProperty.create("has_item");


	public MapCodec<DittoBlock> codec() {
		return CODEC;
	}

	public DittoBlock(BlockBehaviour.Properties p_273064_) {
		super(p_273064_);
		this.registerDefaultState(this.defaultBlockState().setValue(HAS_ITEM, false));
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (!(blockEntity instanceof DittoBlockEntity dittoBlockEntity))
			return InteractionResult.PASS;

		if (level.isClientSide)
			return InteractionResult.SUCCESS;

		ItemStack itemstack1 = dittoBlockEntity.getTheItem();
		if (!itemstack1.isEmpty()) {
			ItemStack itemstack = getBlock().toStack();
			if (dittoBlockEntity.isEmpty()) {
				return InteractionResult.PASS;
			} else {
				if (itemstack1.getItem() != getBlock().asItem())
					Containers.dropContents(level, pos, dittoBlockEntity);
				else
					return InteractionResult.PASS;
				dittoBlockEntity.setTheItem(itemstack);
			}

			level.playSound((Player) null, pos, SoundEvents.DECORATED_POT_INSERT, SoundSource.BLOCKS, 1.0F, 0.5F);
			if (level instanceof ServerLevel) {
				ServerLevel serverlevel = (ServerLevel) level;
				serverlevel.sendParticles(ParticleTypes.DUST_PLUME, (double) pos.getX() + (double) 0.5F, (double) pos.getY() + 1.2, (double) pos.getZ() + (double) 0.5F, 7, (double) 0.0F, (double) 0.0F, (double) 0.0F, (double) 0.0F);
			}

			dittoBlockEntity.setChanged();
			level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);

			level.setBlock(pos, dittoBlockEntity.getBlockState().setValue(HAS_ITEM, !dittoBlockEntity.getBlockState().getValue(HAS_ITEM)), 3);

			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	protected InteractionResult useItemOn(ItemStack item, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		BlockEntity blockEntity1 = level.getBlockEntity(pos);
		if (blockEntity1 instanceof DittoBlockEntity dittoBlockEntity) {
			if (level.isClientSide) {
				return InteractionResult.SUCCESS;
			} else {
				if (!(item.getItem() instanceof BlockItem)) {
					return item == ItemStack.EMPTY ? InteractionResult.TRY_WITH_EMPTY_HAND : InteractionResult.PASS;
				}
				ItemStack itemstack1 = dittoBlockEntity.getTheItem();
				if (!item.isEmpty() && (itemstack1.isEmpty() || !ItemStack.isSameItemSameComponents(itemstack1, item))) {
					player.awardStat(Stats.ITEM_USED.get(item.getItem()));
					ItemStack itemstack = item.consumeAndReturn(1, player);
					float f;
					if (dittoBlockEntity.isEmpty()) {
						dittoBlockEntity.setTheItem(itemstack);
						f = (float)itemstack.getCount() / (float)itemstack.getMaxStackSize();
					} else {
						if (!(itemstack1.getItem() == getBlock().asItem()))
							Containers.dropContents(level, pos, dittoBlockEntity);
						dittoBlockEntity.setTheItem(itemstack);
						f = (float)itemstack1.getCount() / (float)itemstack1.getMaxStackSize();
					}

					level.playSound((Player)null, pos, SoundEvents.DECORATED_POT_INSERT, SoundSource.BLOCKS, 1.0F, 0.7F + 0.5F * f);
					if (level instanceof ServerLevel) {
						ServerLevel serverlevel = (ServerLevel)level;
						serverlevel.sendParticles(ParticleTypes.DUST_PLUME, (double)pos.getX() + (double)0.5F, (double)pos.getY() + 1.2, (double)pos.getZ() + (double)0.5F, 7, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F);
					}

					dittoBlockEntity.setChanged();
					level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);

					level.setBlock(pos, dittoBlockEntity.getBlockState().setValue(HAS_ITEM, !dittoBlockEntity.getBlockState().getValue(HAS_ITEM)), 3);

					return InteractionResult.SUCCESS;
				} else {
					return InteractionResult.TRY_WITH_EMPTY_HAND;
				}
			}
		} else {
			return InteractionResult.PASS;
		}
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_273169_) {
		p_273169_.add(HAS_ITEM);
	}

	@Nullable
	public BlockEntity newBlockEntity(BlockPos p_273396_, BlockState p_272674_) {
		return new DittoBlockEntity(p_273396_, p_272674_);
	}

	protected void onRemove(BlockState p_305821_, Level p_306245_, BlockPos p_305894_, BlockState p_306294_, boolean p_306159_) {
		Containers.dropContentsOnDestroy(p_305821_, p_306294_, p_306245_, p_305894_);
		super.onRemove(p_305821_, p_306245_, p_305894_, p_306294_, p_306159_);
	}

	protected boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if ((blockEntity instanceof DittoBlockEntity dittoBlockEntity)){
			if (dittoBlockEntity.getTheItem() != ItemStack.EMPTY && dittoBlockEntity.getTheItem().getItem() != ModBlocks.DITTO_BLOCK.asItem()) {
				return 15;
			}
		}
		return 0;
	}

	public boolean shouldRender(BlockState state) {
		return true;
	}

	public DeferredBlock getBlock() {
		return ModBlocks.DITTO_BLOCK;
	}
}
