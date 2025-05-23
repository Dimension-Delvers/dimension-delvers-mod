package com.wanderersoftherift.wotr.block;

import com.mojang.serialization.MapCodec;
import com.wanderersoftherift.wotr.block.blockentity.DittoBlockEntity;
import com.wanderersoftherift.wotr.init.WotrBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
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
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.registries.DeferredBlock;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class DittoBlock extends BaseEntityBlock {
    public static final MapCodec<DittoBlock> CODEC = simpleCodec(DittoBlock::new);

    public DittoBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState());
    }

    @Override
    public MapCodec<DittoBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof DittoBlockEntity dittoBlockEntity)) {
            return InteractionResult.PASS;
        }

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        ItemStack itemInBlock = dittoBlockEntity.getTheItem();
        if (itemInBlock.isEmpty()) {
            return InteractionResult.PASS;
        }

        ItemStack originalItem = dittoBlockEntity.getTheItem();
        ItemStack newItemstack = ItemStack.EMPTY;
        updateSetItem(level, pos, player, dittoBlockEntity, newItemstack, originalItem);

        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useItemOn(
            ItemStack item,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult result) {
        BlockEntity blockEntity1 = level.getBlockEntity(pos);
        if (!(blockEntity1 instanceof DittoBlockEntity dittoBlockEntity)) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (!(item.getItem() instanceof BlockItem)) {
            if (item == ItemStack.EMPTY) {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            } else {
                return InteractionResult.PASS;
            }
        }
        ItemStack originalItem = dittoBlockEntity.getTheItem();
        if (!item.isEmpty() && (originalItem.isEmpty() || !ItemStack.isSameItemSameComponents(originalItem, item))) {
            player.awardStat(Stats.ITEM_USED.get(item.getItem()));
            ItemStack newItemstack = item.consumeAndReturn(1, player);
            updateSetItem(level, pos, player, dittoBlockEntity, newItemstack, originalItem);

            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
    }

    private static void updateSetItem(
            Level level,
            BlockPos pos,
            Player player,
            DittoBlockEntity dittoBlockEntity,
            ItemStack newItemstack,
            ItemStack oldItemstack) {
        float fillPercent;
        Containers.dropContents(level, pos, dittoBlockEntity);
        dittoBlockEntity.setTheItem(newItemstack);
        if (dittoBlockEntity.isEmpty()) {
            fillPercent = (float) newItemstack.getCount() / (float) newItemstack.getMaxStackSize();
        } else {
            fillPercent = (float) oldItemstack.getCount() / (float) oldItemstack.getMaxStackSize();
        }

        level.playSound(null, pos, SoundEvents.DECORATED_POT_INSERT, SoundSource.BLOCKS, 1.0F,
                0.7F + 0.5F * fillPercent);
        if (level instanceof ServerLevel sLevel) {
            sLevel.sendParticles(ParticleTypes.DUST_PLUME, (double) pos.getX() + 0.5, (double) pos.getY() + 1.2,
                    (double) pos.getZ() + 0.5, 7, 0.0, 0.0, 0.0, 0.0);
        }

        dittoBlockEntity.setChanged();
        level.sendBlockUpdated(pos, dittoBlockEntity.getBlockState(), dittoBlockEntity.getBlockState(), 3);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    }

    @Override
    @Nullable public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DittoBlockEntity(pos, state);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        Containers.dropContentsOnDestroy(state, newState, level, pos);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if ((blockEntity instanceof DittoBlockEntity dittoBlockEntity)) {
            if (dittoBlockEntity.getTheItem() != ItemStack.EMPTY
                    && dittoBlockEntity.getTheItem().getItem() != WotrBlocks.DITTO_BLOCK.asItem()) {
                return 15;
            }
        }
        return 0;
    }

    public boolean shouldRender(BlockState state) {
        return true;
    }

    public DeferredBlock getBlock() {
        return WotrBlocks.DITTO_BLOCK;
    }
}
