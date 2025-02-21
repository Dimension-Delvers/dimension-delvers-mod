package com.dimensiondelvers.dimensiondelvers.abilities.effects;

import com.dimensiondelvers.dimensiondelvers.abilities.Targetting.EffectTargeting;
import com.dimensiondelvers.dimensiondelvers.abilities.effects.util.ParticleInfo;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class MovementEffect extends AbstractEffect {
    private Vec3 velocity;
    public MovementEffect(EffectTargeting targeting, List<AbstractEffect> effects, Vec3 velocity, Optional<ParticleInfo> particles) {
        super(targeting, effects, particles);
        this.velocity = velocity;
    }

    public static final MapCodec<MovementEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    EffectTargeting.CODEC.fieldOf("targeting").forGetter(AbstractEffect::getTargeting),
                    Codec.list(AbstractEffect.DIRECT_CODEC).fieldOf("effects").forGetter(AbstractEffect::getEffects),
                    Vec3.CODEC.fieldOf("velocity").forGetter(MovementEffect::getVelocity),
                    Codec.optionalField("particles", ParticleInfo.CODEC.codec(), true).forGetter(AbstractEffect::getParticles)
            ).apply(instance, MovementEffect::new)
    );

    @Override
    public MapCodec<? extends AbstractEffect> getCodec() {
        return CODEC;
    }

    @Override
    public void apply(Entity user, List<BlockPos> blocks, Player caster) {
        List<Entity> targets = getTargeting().getTargets(user, blocks, caster);

        applyParticlesToUser(user);

        for(Entity target: targets) {
            applyParticlesToTarget(target);
            //TODO look into implementing scaling still

            //TODO look into relative vs directional
            target.setDeltaMovement(velocity);
            ChunkSource chunk = target.level().getChunkSource();
            if (chunk instanceof ServerChunkCache chunkCache) {
                chunkCache.broadcast(target, new ClientboundSetEntityMotionPacket(target));
            }

            if(target instanceof Player player) {
                //This is the secret sauce to making the movement work for players
                ((ServerPlayer)player).connection.send(new ClientboundSetEntityMotionPacket(player));
            }

            //Then apply children affects to targets
            super.apply(target, getTargeting().getBlocks(user), caster);
        }


        if(targets.isEmpty())
        {
            super.apply(null, getTargeting().getBlocks(user), caster);
        }

    }

    public Vec3 getVelocity() {
        return this.velocity;
    }
}
