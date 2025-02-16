package com.dimensiondelvers.dimensiondelvers.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * A Rift Spawner block is a usable block for generating rift entrances.
 */
public class RiftSpawnerBlock extends Block {
    public static final MapCodec<RiftSpawnerBlock> CODEC = simpleCodec(RiftSpawnerBlock::new);

    public @NotNull MapCodec<RiftSpawnerBlock> codec() {
        return CODEC;
    }

    public RiftSpawnerBlock(Properties properties) {
        super(properties);
    }

    /**
     * Provides the location to create the rift, if a valid location exists
     * @param level
     * @param pos
     * @param dir
     * @return A valid spawn location, or Optional#empty
     */
    public Optional<SpawnLocation> getSpawnLocation(Level level, BlockPos pos, Direction dir) {
        boolean freeStanding = true;
        BlockPos abovePos = pos.above();
        for (int i = 0; i < 3; i++) {
            if (!allowsPortal(level, abovePos)) {
                freeStanding = false;
                break;
            }
            abovePos = abovePos.above();
        }
        if (freeStanding) {
            return Optional.of(new SpawnLocation(pos.above(), pos.above().getBottomCenter(), Direction.UP));
        }

        BlockPos adjacentPos = pos.relative(dir);
        if (allowsPortal(level, adjacentPos) && allowsPortal(level, adjacentPos.above()) && allowsPortal(level, adjacentPos.below())) {
            return Optional.of(new SpawnLocation(adjacentPos, adjacentPos.below().getBottomCenter().subtract(dir.getUnitVec3().scale(0.475)), dir));
        }
        return Optional.empty();
    }

    private boolean allowsPortal(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.isAir() || state.getCollisionShape(level, pos).isEmpty();
    }

    public record SpawnLocation(BlockPos blockPos, Vec3 position, Direction direction) {}
}
