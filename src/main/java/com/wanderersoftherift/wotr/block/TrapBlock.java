package com.wanderersoftherift.wotr.block;


import com.wanderersoftherift.wotr.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.NotNull;


public class TrapBlock extends DittoBlock {
    private static final int DEACTIVATION_TIME = 60;
    private static final int TICK_DELAY = 4;
    private static final int STAGES = 2;
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, STAGES);
    public static final BooleanProperty WAITING_FOR_TICK = BooleanProperty.create("waiting_for_tick");
    public static final BooleanProperty DEACTIVATED = BooleanProperty.create("deactivated");


    public TrapBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.defaultBlockState().setValue(STAGE, 0));
        this.registerDefaultState(this.defaultBlockState().setValue(DEACTIVATED, false));
        this.registerDefaultState(this.defaultBlockState().setValue(WAITING_FOR_TICK, false));
    }

    protected void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (state.getValue(DEACTIVATED)) {
            level.setBlockAndUpdate(pos, state.setValue(DEACTIVATED, false).setValue(STAGE, 0).setValue(WAITING_FOR_TICK, false));
            return;
        }

        if (state.getValue(STAGE) + 1 > STAGES) {
            fail(level, pos, state);
            return;
        }

        level.setBlockAndUpdate(pos, state.setValue(STAGE, state.getValue(STAGE) + 1).setValue(WAITING_FOR_TICK, false));
    }

    @Override
    public void stepOn(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity) {
        if (!this.canEntityTick(pos, entity)) {
            return;
        }
        scheduleTick(level, pos, state);
    }

    @Override
    public void fallOn(@NotNull Level level, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull Entity entity, float fallDistance) {
        if (!this.canEntityTick(pos, entity)) {
            return;
        }
        if (fallDistance > 5) {
            level.setBlockAndUpdate(pos, state.setValue(STAGE, STAGES).setValue(WAITING_FOR_TICK, false));
        }
        scheduleTick(level, pos, state);
    }

    public void scheduleTick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state) {
        if (state.getValue(WAITING_FOR_TICK)) {
            return;
        }
        level.setBlockAndUpdate(pos, state.setValue(WAITING_FOR_TICK, true));
        level.scheduleTick(pos, this, TICK_DELAY);
    }

    public boolean legalEntity(@NotNull Entity entity) {
        return true;
    }

    public boolean canEntityTick(@NotNull BlockPos pos, @NotNull Entity entity) {
        if (!this.legalEntity(entity)) {
            return false;
        }
        return entity.onGround() && entity.position().y > (double)((float)pos.getY() + 0.6875F);
    }

    public void fail(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state) {
        BlockPos[] positions = {pos.west(), pos.north(), pos.east(), pos.south(), pos.above(), pos.below()};
        for (BlockPos checked_pos : positions) {
            BlockState checked_state = level.getBlockState(checked_pos);
            if (!(checked_state.getBlock() instanceof TrapBlock)) {
                continue;
            }
            level.setBlockAndUpdate(checked_pos, checked_state.setValue(STAGE, STAGES));
            level.scheduleTick(checked_pos, checked_state.getBlock(), 1);
        }

        level.setBlockAndUpdate(pos, state.setValue(DEACTIVATED, true));
        level.scheduleTick(pos, state.getBlock(), DEACTIVATION_TIME);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(STAGE);
        builder.add(WAITING_FOR_TICK);
        builder.add(DEACTIVATED);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(BlockState state) {
        if (state.getValue(DEACTIVATED)) {
            return RenderShape.INVISIBLE;
        }
        return super.getRenderShape(state);
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        if (state.getValue(DEACTIVATED)) {
            return Shapes.empty();
        }
        return super.getCollisionShape(state, level, pos, context);
    }

    public BlockState getTweak() {
        return ModBlocks.PLAYER_TRAP_BLOCK.get().defaultBlockState();
    }

    @Override
    public boolean shouldRender(BlockState state) {
		return !state.getValue(DEACTIVATED);
	}

    @Override
    public DeferredBlock getBlock() {
        return ModBlocks.TRAP_BLOCK;
    }
}

