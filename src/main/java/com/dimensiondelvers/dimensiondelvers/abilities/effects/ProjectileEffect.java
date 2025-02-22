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
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class ProjectileEffect extends AbstractEffect{
    ResourceLocation entityType;
    Vec3 velocity;

    /*
     * For now just handle any projectile given, but we will look into handling a dynamic projectile that can handle effects attached to it
     */

    public static final MapCodec<ProjectileEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    EffectTargeting.CODEC.fieldOf("targeting").forGetter(AbstractEffect::getTargeting),
                    Codec.list(AbstractEffect.DIRECT_CODEC).fieldOf("effects").forGetter(AbstractEffect::getEffects),
                    Codec.optionalField("particles", ParticleInfo.CODEC.codec(), true).forGetter(AbstractEffect::getParticles),
                    ResourceLocation.CODEC.fieldOf("projectile_type").forGetter(ProjectileEffect::getEntityType),
                    Vec3.CODEC.fieldOf("velocity").forGetter(ProjectileEffect::getVelocity)
            ).apply(instance, ProjectileEffect::new)
    );

    public Vec3 getVelocity() {
        return this.velocity;
    }

    private ResourceLocation getEntityType() {
        return this.entityType;
    }

    @Override
    public MapCodec<? extends AbstractEffect> getCodec() {
        return CODEC;
    }

    public ProjectileEffect(EffectTargeting targeting, List<AbstractEffect> effects, Optional<ParticleInfo> particles, ResourceLocation entityType, Vec3 velocity) {
        super(targeting, effects, particles);
        this.entityType = entityType;
        this.velocity = velocity;
    }

    @Override
    public void apply(Entity user, List<BlockPos> blocks, Player caster) {
        List<Entity> targets = getTargeting().getTargets(user, blocks, caster);
        applyParticlesToUser(user);
        if(!targets.isEmpty()) {
            Entity random = targets.get(caster.getRandom().nextIntBetweenInclusive(0, targets.size() - 1));
            if (BuiltInRegistries.ENTITY_TYPE.get(this.entityType).isPresent())
            {
                EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(this.entityType).get().value();
                Entity summon = type.create((ServerLevel) caster.level(), null, random.getOnPos(), EntitySpawnReason.MOB_SUMMONED, false, false);
                if (summon != null)
                {

                    summon.setPos(random.getEyePosition());
                    if(summon instanceof Projectile projectileEntity)
                    {
                        projectileEntity.setOwner(random);

                        //TODO tweak this calculation its not quite working right

                        projectileEntity.shootFromRotation(random, (float) (random.getXRot() + velocity.y), (float) (random.getYRot() + velocity.x), 0, 1, 0);

                    }

                    caster.level().addFreshEntity(summon);
                    applyParticlesToTarget(summon);
                    super.apply(summon, getTargeting().getBlocks(user), caster);
                }
            }
        }


        //No entity was selected as the target position
        if(targets.isEmpty())
        {
            super.apply(null, getTargeting().getBlocks(user), caster);
        }



    }

}
