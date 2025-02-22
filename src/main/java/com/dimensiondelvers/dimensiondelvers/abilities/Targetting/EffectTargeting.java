package com.dimensiondelvers.dimensiondelvers.abilities.Targetting;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.Predicate;

public class EffectTargeting {

    /*TODO optional fields for considering WHO to target, and or what such as should area target friendlies? Or Raycast blocks etc..
     * Determine defaults for such cases
     */
    public static final MapCodec<EffectTargeting> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    TargetingType.CODEC.fieldOf("target_type").forGetter(EffectTargeting::getTargetingType),
                    Codec.DOUBLE.fieldOf("range").forGetter(EffectTargeting::getRange)
            ).apply(instance, EffectTargeting::new)
    );
    private final TargetingType targetingType;
    private final double range;
    public EffectTargeting( TargetingType target, double range) {
        this.targetingType = target;
        this.range = range;
    }

    public double getRange() {
        return this.range;
    }

    public TargetingType getTargetingType() {
        return this.targetingType;
    }


    /**
     * @param user This is the entity which is using the effect, this can be any entity down a chain based on the effect list, this determines the location around where the effect is targeting
     * @param blocks A list of blocks which can be a point of reference for targeting enemies around them. This is mainly used for raycasting based effects
     * @param caster The original player starting the effect chain
     * @return The list of entities selected by the targeting method.
     */
    public List<Entity> getTargets(Entity user, List<BlockPos> blocks, Player caster) {
        List<Entity> targets = new ArrayList<>();
        if(user != null)
        {
            targets.addAll(getTargetsFromUser(user));
        }
        else
        {
            targets.addAll(getTargetsFromBlocks(blocks, caster));
        }

        return targets;
    }

    private List<Entity> getTargetsFromUser(Entity user) {
        List<Entity> targets = new ArrayList<>();
        switch(targetingType) {
            case SELF -> {
                DimensionDelvers.LOGGER.info("Targeting Self");
                targets.add(user);
            }

            case RAYCAST -> {
                //TODO optimize AABB to not look behind player
                DimensionDelvers.LOGGER.info("Targeting Raycast");
                List<LivingEntity> LookedAtEntities = user.level().getEntities(EntityTypeTest.forClass(LivingEntity.class), new AABB(user.position().x - (range / 2), user.position().y - (range / 2), user.position().z - (range / 2), user.position().x + (range / 2), user.position().y + (range / 2), user.position().z + (range / 2)), (Predicate<LivingEntity>) entity -> !entity.is(user) && entity.isLookingAtMe((LivingEntity) user, 0.025, true, false, entity.getEyeY())
                );

                targets.addAll(LookedAtEntities);
                return targets;
            }


            case AREA -> {
                DimensionDelvers.LOGGER.info("Targeting AOE");
                return user.level().getEntities(user, new AABB(user.position().x - (range/2), user.position().y - (range/2), user.position().z - (range/2), user.position().x + (range/2), user.position().y + (range/2), user.position().z + (range/2)), (entity -> !(entity instanceof Player) && !entity.is(user)));
            }

        }

        return targets;
    }

    private List<Entity> getTargetsFromBlocks(List<BlockPos> blocks, Player caster) {
        List<Entity> targets = new ArrayList<>();

        if(blocks.isEmpty()) return targets;

        switch(targetingType) {
            case SELF -> {
            }

            case RAYCAST -> {

            }

            //Gets first block and makes an area around it where the block is in the center
            case AREA -> {
                //TODO look into config for selecting conditions in area since we may want to select other players for large scale heals etc
                return caster.level().getEntities(caster, new AABB(blocks.get(0).getX() - (range / 2), blocks.get(0).getY() - (range / 2), blocks.get(0).getZ() - (range / 2), blocks.get(0).getX() + (range / 2), blocks.get(0).getY() + (range / 2), blocks.get(0).getZ() + (range / 2)), (entity -> !(entity instanceof Player) && !entity.is(caster)));
            }

        }

        return targets;
    }

    public List<BlockPos> getBlocks(Entity user)
    {
        List<BlockPos> blocks = new ArrayList<>();
        switch(targetingType) {
            case SELF -> {
            }

            case RAYCAST -> {
                if(user == null) return blocks;
                DimensionDelvers.LOGGER.info("Raycasting Blocks");
                BlockHitResult hitBlock = getEntityPOVHitResult(user, this.range);
                blocks.add(hitBlock.getBlockPos());
                return blocks;
            }


            case AREA -> {
                DimensionDelvers.LOGGER.info("Targeting AOE");
                //TODO think about handling all blocks in an AOE?
//                return user.level().getEntities(user, new AABB(user.position().x - (range/2), user.position().y - (range/2), user.position().z - (range/2), user.position().x + (range/2), user.position().y + (range/2), user.position().z + (range/2)), (entity -> !(entity instanceof Player) && !entity.is(user)));
            }

        }
        return blocks;
    }

    public List<BlockPos> getBlocksInArea(Level level, Entity user, List<BlockPos> targetPos) {
        List<BlockPos> blockPos = new ArrayList<>();

        if(user == null && targetPos.isEmpty()) return blockPos;
        switch(targetingType) {
            case RAYCAST -> {
                if(user == null) return blockPos;
                DimensionDelvers.LOGGER.info("Raycasting Blocks");
                BlockHitResult hitBlock = getEntityPOVHitResult(user, this.range);
                blockPos.add(hitBlock.getBlockPos());
            }


            case AREA -> {
                DimensionDelvers.LOGGER.info("Targeting AOE");
                int startX, startY, startZ;
                int endX, endY, endZ;

                //TODO maybe look into java ranges?
                if(user == null)
                {

                    BlockPos first = targetPos.get(0);
                    startX = (int) (first.getX() - (range/2));
                    startY = (int) (first.getY() - (range/2));
                    startZ = (int) (first.getZ() - (range/2));

                    endX = (int) (first.getX() + (range/2));
                    endY = (int) (first.getY() + (range/2));
                    endZ = (int) (first.getZ() + (range/2));
                }
                else
                {
                    //TODO make this use player block position rather than absolute pos since it causes rounding issues converting to int.
                    startX = (int) (user.getX() - (range/2));
                    startY = (int) (user.getY() - (range/2));
                    startZ = (int) (user.getZ() - (range/2));

                    endX = (int) (user.getX() + (range/2));
                    endY = (int) (user.getY() + (range/2));
                    endZ = (int) (user.getZ() + (range/2));
                }

                for(int x = startX ; x < endX; x++)
                {
                    for(int y = startY; y < endY; y++)
                    {
                        for(int z = startZ; z < endZ; z++)
                        {
                            if(!level.getBlockState(new BlockPos(x, y, z)).isAir())
                            {
                                blockPos.add(new BlockPos(x, y, z));
                            }
                        }
                    }
                }
            }

        }
        return blockPos;
    }


    public static BlockHitResult getEntityPOVHitResult(Entity entity, double range) {
        Level level = entity.level();
        Vec3 eyePosition = entity.getEyePosition();
        Vec3 rayVector = eyePosition.add(entity.calculateViewVector(entity.getXRot(), entity.getYRot()).scale(range));
        return level.clip(new ClipContext(eyePosition, rayVector, net.minecraft.world.level.ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));
    }


    public enum TargetingType implements StringRepresentable {
        AREA("area", 0),
        SELF("self", 1),
        RAYCAST("raycast", 2);

        public static final IntFunction<TargetingType> BY_ID = ByIdMap.continuous(TargetingType::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final StreamCodec<ByteBuf, TargetingType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, TargetingType::id);
        public static final Codec<TargetingType> CODEC = StringRepresentable.fromEnum(TargetingType::values);
        private final String name;
        private final int id;

        TargetingType(String name, int value) {
            this.name = name;
            this.id = value;
        }

        public int id() {
            return this.id;
        }

        public String getSerializedName() {
            return this.name;
        }
    }
}