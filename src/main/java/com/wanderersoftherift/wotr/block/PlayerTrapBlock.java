package com.wanderersoftherift.wotr.block;


import com.wanderersoftherift.wotr.init.ModBlocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

public class PlayerTrapBlock extends TrapBlock {
    public PlayerTrapBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean legalEntity(@NotNull Entity entity) {
        if (entity.getType() == EntityType.PLAYER) {
            return true;
        }
        if (entity.getType() == EntityType.ITEM) {
            return true;
        }
        return false;
    }

    @Override
    public BlockState getTweak() {
        return ModBlocks.MOB_TRAP_BLOCK.get().defaultBlockState();
    }

    @Override
    public DeferredBlock getBlock() {
        return ModBlocks.PLAYER_TRAP_BLOCK;
    }
}
