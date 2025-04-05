package com.wanderersoftherift.wotr.abilities.Targeting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.wanderersoftherift.wotr.Registries.AbilityRegistry;
import com.wanderersoftherift.wotr.abilities.effects.EffectContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractTargeting {
    public abstract MapCodec<? extends AbstractTargeting> getCodec();
    public static final Codec<AbstractTargeting> DIRECT_CODEC = AbilityRegistry.EFFECT_TARGETING_REGISTRY.byNameCodec().dispatch(AbstractTargeting::getCodec, Function.identity());

    /**
     * @param currentEntity This is the entity which is using the effect, this can be any entity down a chain based on the effect list, this determines the location around where the effect is targeting
     * @param blocks A list of blocks which can be a point of reference for targeting enemies around them. This is mainly used for raycasting based effects
     * @param context Context of the effect
     * @return The list of entities selected by the targeting method.
     */
    public List<Entity> getTargets(Entity currentEntity, List<BlockPos> blocks, EffectContext context) {
        List<Entity> targets = new ArrayList<>();
        if(currentEntity != null) {
            targets.addAll(getTargetsFromEntity(currentEntity, context));
        }
        else {
            targets.addAll(getTargetsFromBlocks(blocks, context));
        }

        return targets;
    }

    protected List<Entity> getTargetsFromEntity(Entity entity, EffectContext context) {
        return new ArrayList<>();
    }

    protected List<Entity> getTargetsFromBlocks(List<BlockPos> blocks, EffectContext context) {
        return new ArrayList<>();
    }

    public List<BlockPos> getBlocks(Entity user) {
        return new ArrayList<>();
    }

    public List<BlockPos> getBlocksInArea(Entity entity, List<BlockPos> targetPos, EffectContext context) {
        return new ArrayList<>();
    }
}
