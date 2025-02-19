package com.dimensiondelvers.dimensiondelvers.abilities.effects;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.abilities.Targetting.EffectTargeting;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class MovementEffect extends AbstractEffect {
    private Vec3 velocity;
    public MovementEffect(EffectTargeting targeting, List<AbstractEffect> effects, Vec3 velocity) {
        super(targeting, effects);
        this.velocity = velocity;
    }

    public static final MapCodec<MovementEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    EffectTargeting.CODEC.fieldOf("targeting").forGetter(AbstractEffect::getTargeting),
                    Codec.list(AbstractEffect.DIRECT_CODEC).fieldOf("effects").forGetter(AbstractEffect::getEffects),
                    Vec3.CODEC.fieldOf("velocity").forGetter(MovementEffect::getVelocity)
            ).apply(instance, MovementEffect::new)
    );

    @Override
    public MapCodec<? extends AbstractEffect> getCodec() {
        return CODEC;
    }

    public void apply(Entity user) {
        List<Entity> targets = getTargeting().getTargets(user);

        for(Entity e: targets) {
            //TODO look into implementing scaling still

            //TODO look into relative vs directional
            e.setDeltaMovement(velocity);
            ChunkSource chunk = e.level().getChunkSource();
            if (chunk instanceof ServerChunkCache chunkCache) {
                chunkCache.broadcast(e, new ClientboundSetEntityMotionPacket(e));
            }

            if(e instanceof Player player) {
                //This is the secret sauce to making the movement work for players
                ((ServerPlayer)player).connection.send(new ClientboundSetEntityMotionPacket(player));
            }

            //Then apply children affects to targets
            super.apply(e);
        }
    }

    public Vec3 getVelocity() {
        return this.velocity;
    }
}
