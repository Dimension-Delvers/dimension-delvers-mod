package com.wanderersoftherift.wotr.abilities.effects.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public record TargetPredicate(
        Optional<EntityPredicate> entityPredicate,
        EntityAttitude attitude,
        boolean excludeCaster
) {
    public static final Codec<TargetPredicate> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            EntityPredicate.CODEC.optionalFieldOf("entity").forGetter(TargetPredicate::entityPredicate),
                            EntityAttitude.CODEC.optionalFieldOf("attitude", EntityAttitude.ANY).forGetter(TargetPredicate::attitude),
                            Codec.BOOL.optionalFieldOf("exclude_caster", false).forGetter(TargetPredicate::excludeCaster)
                    )
                    .apply(instance, TargetPredicate::new)
    );

    public TargetPredicate() {
        this(Optional.empty(), EntityAttitude.ANY, false);
    }

    public boolean matches(Entity target, Entity caster) {
        if (excludeCaster && target == caster) {
            return false;
        }
        if (!(caster.level() instanceof ServerLevel serverLevel)) {
            return false;
        }
        if (entityPredicate.isPresent() && !entityPredicate.get().matches(serverLevel, caster.position(), target)) {
            return false;
        }
        if (!attitude.matches(target, caster)) {
            return false;
        }
        return true;
    }

}
