package com.wanderersoftherift.wotr.abilities.Targeting;

import com.mojang.serialization.MapCodec;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.abilities.effects.EffectContext;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class SelfTargeting extends AbstractTargeting {

    public static final MapCodec<SelfTargeting> CODEC = MapCodec.unit(SelfTargeting::new);

    @Override
    public MapCodec<? extends AbstractTargeting> getCodec() {
        return CODEC;
    }

    @Override
    public List<Entity> getTargetsFromEntity(Entity entity, EffectContext context) {
        WanderersOfTheRift.LOGGER.debug("Targeting Self");

        List<Entity> targets = new ArrayList<>();
        targets.add(entity);

        return targets;
    }
}
