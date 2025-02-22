package com.dimensiondelvers.dimensiondelvers.abilities.effects;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.abilities.Targetting.EffectTargeting;
import com.dimensiondelvers.dimensiondelvers.abilities.effects.util.ParticleInfo;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Optional;

public class SummonEffect extends AbstractEffect{
    ResourceLocation entityType;
    int summonAmount = 1;

    //TODO look into handling different types of teleports and better handle relative motion
    //TODO also look into teleporting "towards" a location to find the nearest safe spot that isnt the exact location

    public static final MapCodec<SummonEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    EffectTargeting.CODEC.fieldOf("targeting").forGetter(AbstractEffect::getTargeting),
                    Codec.list(AbstractEffect.DIRECT_CODEC).fieldOf("effects").forGetter(AbstractEffect::getEffects),
                    Codec.optionalField("particles", ParticleInfo.CODEC.codec(), true).forGetter(AbstractEffect::getParticles),
                    ResourceLocation.CODEC.fieldOf("entity_type").forGetter(SummonEffect::getEntityType),
                    Codec.INT.fieldOf("amount").forGetter(SummonEffect::getAmount)
            ).apply(instance, SummonEffect::new)
    );

    private Integer getAmount() {
        return this.summonAmount;
    }

    private ResourceLocation getEntityType() {
        return this.entityType;
    }

    @Override
    public MapCodec<? extends AbstractEffect> getCodec() {
        return CODEC;
    }

    public SummonEffect(EffectTargeting targeting, List<AbstractEffect> effects, Optional<ParticleInfo> particles, ResourceLocation entityType, int amount) {
        super(targeting, effects, particles);
        this.entityType = entityType;
        this.summonAmount = amount;
    }

    @Override
    public void apply(Entity user, List<BlockPos> blocks, Player caster) {
        List<Entity> targets = getTargeting().getTargets(user, blocks, caster);
        applyParticlesToUser(user);

        //No entity was selected as the summon position
        if(targets.isEmpty())
        {
            List<BlockPos> blockInArea = getTargeting().getBlocksInArea(caster.level(), user, blocks);
            //TODO look into more systematically placing summons
            for(int i = 0; i < summonAmount; i++) {
                if(blockInArea.isEmpty())
                {
                    DimensionDelvers.LOGGER.info("No Suitable Spawn Location!");
                    super.apply(null, getTargeting().getBlocks(user), caster);
                }
                BlockPos random = blockInArea.get(caster.getRandom().nextIntBetweenInclusive(0, blockInArea.size() - 1));
                if (BuiltInRegistries.ENTITY_TYPE.get(this.entityType).isPresent())
                {
                    EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(this.entityType).get().value();
                    Entity summon = type.create((ServerLevel) caster.level(), null, random, EntitySpawnReason.MOB_SUMMONED, true, true);
                    if (summon != null)
                    {
                        caster.level().addFreshEntity(summon);
                        applyParticlesToTarget(summon);
                        super.apply(summon, getTargeting().getBlocks(user), caster);
                    }
                }
            }
        }
        else
        {
            for(int i = 0; i < summonAmount; i++)
            {
                Entity random = targets.get(caster.getRandom().nextIntBetweenInclusive(0, targets.size() - 1));
                if (BuiltInRegistries.ENTITY_TYPE.get(this.entityType).isPresent())
                {
                    EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(this.entityType).get().value();
                    Entity summon = type.create((ServerLevel) caster.level(), null, random.getOnPos(), EntitySpawnReason.MOB_SUMMONED, false, false);
                    if (summon != null)
                    {
                        caster.level().addFreshEntity(summon);
                        applyParticlesToTarget(summon);
                        super.apply(summon, getTargeting().getBlocks(user), caster);
                    }
                }
            }
        }
    }

}
