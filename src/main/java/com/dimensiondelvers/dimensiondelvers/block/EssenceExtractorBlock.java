package com.dimensiondelvers.dimensiondelvers.block;

import com.dimensiondelvers.dimensiondelvers.block.blockentity.EssenceExtractorBlockEntity;
import com.dimensiondelvers.dimensiondelvers.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class EssenceExtractorBlock extends Block implements EntityBlock {
    private static final Component CONTAINER_TITLE = Component.translatable("container.dimensiondelvers.essence_extractor");

    public EssenceExtractorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new EssenceExtractorBlockEntity(pos, state);
    }

    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(level, pos));
            return InteractionResult.CONSUME;
        }
    }

    protected MenuProvider getMenuProvider(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos) {
        return new SimpleMenuProvider((containerId, playerInventory, player) -> {
            EssenceExtractorBlockEntity blockEntity = (EssenceExtractorBlockEntity) level.getBlockEntity(pos);
            return blockEntity == null ? null : blockEntity.createMenu(containerId, playerInventory, player);
        }, CONTAINER_TITLE);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> entityType) {
        if (level instanceof ServerLevel) {
            return entityType == ModBlockEntities.ESSENCE_EXTRACTOR.get() ? (l, pos, s, blockEntity) -> EssenceExtractorBlockEntity.serverTick(level, pos, s, (EssenceExtractorBlockEntity) blockEntity) : null;
        } else {
            return null;
        }
    }
}
