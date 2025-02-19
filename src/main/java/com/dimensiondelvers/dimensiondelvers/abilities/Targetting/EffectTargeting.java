package com.dimensiondelvers.dimensiondelvers.abilities.Targetting;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.abilities.BoostAbility;
import com.dimensiondelvers.dimensiondelvers.abilities.effects.AbstractEffect;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

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

    public List<Entity> getTargets(Entity user) {
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

    public void applyToAllInTarget(AbstractEffect effect, Entity user) {
        for(Entity e: getTargets(user)) {
            effect.apply(e);
        }
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

        private TargetingType(String name, int value) {
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