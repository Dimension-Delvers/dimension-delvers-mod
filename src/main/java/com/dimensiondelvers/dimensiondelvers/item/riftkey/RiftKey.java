package com.dimensiondelvers.dimensiondelvers.item.riftkey;

import com.dimensiondelvers.dimensiondelvers.block.RiftSpawnerBlock;
import com.dimensiondelvers.dimensiondelvers.entity.RiftEntranceEntity;
import com.dimensiondelvers.dimensiondelvers.init.ModDataComponentType;
import com.dimensiondelvers.dimensiondelvers.init.ModEntityTypes;
import com.dimensiondelvers.dimensiondelvers.init.ModEssenceTypes;
import com.dimensiondelvers.dimensiondelvers.item.essence.EssenceType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class RiftKey extends Item {
    public RiftKey(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
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

                spawnRift(level, loc.position(), loc.direction(), context.getItemInHand().getOrDefault(ModDataComponentType.RIFT_TIER, 0));
                context.getItemInHand().shrink(1);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        EssenceType theme = stack.getOrDefault(ModDataComponentType.RIFT_THEME, ModEssenceTypes.NONE.get());
        if (theme != ModEssenceTypes.NONE.get()) {
            return Component.translatable("item.dimensiondelvers.rift_key.themed", theme.getName());
        } else {
            return super.getName(stack);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        int tier = stack.getOrDefault(ModDataComponentType.RIFT_TIER, 0);
        if (tier > 0) {
            components.add(Component.translatable("tooltip.dimensiondelvers.rift_key_tier", tier).withColor(Color.GRAY.getRGB()));
        }
    }

    private List<RiftEntranceEntity> getExistingRifts(Level level, BlockPos pos) {
        return level.getEntities(EntityTypeTest.forClass(RiftEntranceEntity.class), new AABB(pos), x -> true);
    }

    private void spawnRift(Level level, Vec3 pos, Direction dir, int riftSize) {
        RiftEntranceEntity rift = new RiftEntranceEntity(ModEntityTypes.RIFT_ENTRANCE.get(), level);
        rift.setPos(pos);
        rift.setYRot(dir.toYRot());
        rift.setBillboard(dir.getAxis().isVertical());
        rift.setRiftSize(riftSize);
        level.addFreshEntity(rift);
    }
}
