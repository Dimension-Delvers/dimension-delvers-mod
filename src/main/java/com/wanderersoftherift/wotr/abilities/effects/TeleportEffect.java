package com.wanderersoftherift.wotr.abilities.effects;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.abilities.Targeting.AbstractTargeting;
import com.wanderersoftherift.wotr.abilities.effects.util.ParticleInfo;
import com.wanderersoftherift.wotr.abilities.effects.util.TeleportInfo;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Optional;

public class TeleportEffect extends AbstractEffect{
    TeleportInfo teleInfo;

    //TODO look into handling different types of teleports and better handle relative motion
    //TODO also look into teleporting "towards" a location to find the nearest safe spot that isnt the exact location

    public static final MapCodec<TeleportEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    AbstractTargeting.DIRECT_CODEC.fieldOf("targeting").forGetter(AbstractEffect::getTargeting),
                    Codec.list(AbstractEffect.DIRECT_CODEC).fieldOf("effects").forGetter(AbstractEffect::getEffects),
                    TeleportInfo.CODEC.fieldOf("tele_info").forGetter(TeleportEffect::getTeleportInfo),
                    Codec.optionalField("particles", ParticleInfo.CODEC.codec(), true).forGetter(AbstractEffect::getParticles)
            ).apply(instance, TeleportEffect::new)
    );

    @Override
    public MapCodec<? extends AbstractEffect> getCodec() {
        return CODEC;
    }

    public TeleportEffect(AbstractTargeting targeting, List<AbstractEffect> effects, TeleportInfo teleInfo, Optional<ParticleInfo> particles) {
        super(targeting, effects, particles);
        this.teleInfo = teleInfo;
    }

    public TeleportInfo getTeleportInfo() {
        return this.teleInfo;
    }

    @Override
    public void apply(Entity user, List<BlockPos> blocks, LivingEntity caster) {
        List<Entity> targets = getTargeting().getTargets(user, blocks, caster);
        applyParticlesToUser(user);
        for(Entity target: targets) {
            applyParticlesToTarget(target);

            switch(teleInfo.getTarget()) {
                case USER -> {
                    WanderersOfTheRift.LOGGER.info("Teleporting Self");
                    Entity random = targets.get(user.getRandom().nextIntBetweenInclusive(0, targets.size() - 1));
                    user.teleportTo(random.getX() + teleInfo.getPosition().x, random.getY() + teleInfo.getPosition().y, random.getZ() + teleInfo.getPosition().z);

                }

                case TARGET -> {
                    WanderersOfTheRift.LOGGER.info("Teleporting Target");
                    if(teleInfo.isRelative().isEmpty() || (teleInfo.isRelative().isPresent() && teleInfo.isRelative().get()))
                    {
                        target.teleportRelative(teleInfo.getPosition().x, teleInfo.getPosition().y, teleInfo.getPosition().z);
                    }
                    else
                    {
                        target.teleportTo(teleInfo.getPosition().x, teleInfo.getPosition().y, teleInfo.getPosition().z);
                    }
                }
            }

            //Then apply children affects to targets
            super.apply(target, getTargeting().getBlocks(user), caster);
        }


        if(targets.isEmpty())
        {
            super.apply(null, getTargeting().getBlocks(user), caster);
        }
    }

}
