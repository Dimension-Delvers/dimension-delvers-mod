package com.dimensiondelvers.dimensiondelvers.item.tools;

import com.dimensiondelvers.dimensiondelvers.block.TrapBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class DDTweaker extends Item {
    public DDTweaker(Properties properties) {
        super(properties);

    }
    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        BlockState state = level.getBlockState(pos);

        if (state.getBlock() instanceof TrapBlock) {
            level.setBlockAndUpdate(pos, ((TrapBlock) state.getBlock()).getTweak());
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
