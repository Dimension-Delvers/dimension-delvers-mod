package com.wanderersoftherift.wotr.item.riftkey;

import com.wanderersoftherift.wotr.block.RiftSpawnerBlock;
import com.wanderersoftherift.wotr.entity.RiftEntranceEntity;
import com.wanderersoftherift.wotr.init.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class RiftKey extends Item {
    public RiftKey(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if (!(blockstate.getBlock() instanceof RiftSpawnerBlock spawnerBlock)) {
            return InteractionResult.PASS;
        } else if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            Optional<RiftSpawnerBlock.SpawnLocation> spawnLocation = spawnerBlock.getSpawnLocation(level, blockpos, context.getClickedFace());
            if (spawnLocation.isPresent()) {
                RiftSpawnerBlock.SpawnLocation loc = spawnLocation.get();
                List<RiftEntranceEntity> existingRifts = getExistingRifts(level, loc.blockPos());
                if (!existingRifts.isEmpty()) {
                    for (RiftEntranceEntity entrance : existingRifts) {
                        entrance.remove(Entity.RemovalReason.DISCARDED);
                    }
                    return InteractionResult.SUCCESS;
                }

                spawnRift(level, loc.position(), loc.direction());
                context.getItemInHand().shrink(1);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    private List<RiftEntranceEntity> getExistingRifts(Level level, BlockPos pos) {
        return level.getEntities(EntityTypeTest.forClass(RiftEntranceEntity.class), new AABB(pos), x -> true);
    }

    private void spawnRift(Level level, Vec3 pos, Direction dir) {
        RiftEntranceEntity rift = new RiftEntranceEntity(ModEntityTypes.RIFT_ENTRANCE.get(), level);
        rift.setPos(pos);
        rift.setYRot(dir.toYRot());
        rift.setBillboard(dir.getAxis().isVertical());
        level.addFreshEntity(rift);
    }
}
