package com.dimensiondelvers.dimensiondelvers.abilities.effects;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.abilities.Targetting.EffectTargeting;
import com.dimensiondelvers.dimensiondelvers.abilities.effects.util.TeleportInfo;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.portal.TeleportTransition;

import java.util.List;
import java.util.function.IntFunction;

public class TeleportEffect extends AbstractEffect{
    TeleportInfo teleInfo;

    //TODO look into handling different types of teleports and better handle relative motion
    //TODO also look into teleporting "towards" a location to find the nearest safe spot that isnt the exact location

    public static final MapCodec<TeleportEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    EffectTargeting.CODEC.fieldOf("targeting").forGetter(AbstractEffect::getTargeting),
                    Codec.list(AbstractEffect.DIRECT_CODEC).fieldOf("effects").forGetter(AbstractEffect::getEffects),
                    TeleportInfo.CODEC.fieldOf("tele_info").forGetter(TeleportEffect::getTeleportInfo)
            ).apply(instance, TeleportEffect::new)
    );

    @Override
    public MapCodec<? extends AbstractEffect> getCodec() {
        return CODEC;
    }

    public TeleportEffect(EffectTargeting targeting, List<AbstractEffect> effects, TeleportInfo teleInfo) {
        super(targeting, effects);
        this.teleInfo = teleInfo;
    }

    public TeleportInfo getTeleportInfo() {
        return this.teleInfo;
    }

    public void apply(Entity user) {
        List<Entity> targets = getTargeting().getTargets(user);

        DimensionDelvers.LOGGER.info(user.getDirection().getName());
        for(Entity e: targets) {
            switch(teleInfo.getTarget()) {
                case USER -> {
                    DimensionDelvers.LOGGER.info("Teleporting Self");
                    Entity random = targets.get(user.getRandom().nextIntBetweenInclusive(0, targets.size() - 1));
                    user.teleportTo(random.getX() + teleInfo.getPosition().x, random.getY() + teleInfo.getPosition().y, random.getZ() + teleInfo.getPosition().z);

                }

                case TARGET -> {
                    DimensionDelvers.LOGGER.info("Teleporting Target");
                    if(teleInfo.isRelative().isEmpty() || (teleInfo.isRelative().isPresent() && teleInfo.isRelative().get()))
                    {
                        e.teleportRelative(teleInfo.getPosition().x, teleInfo.getPosition().y, teleInfo.getPosition().z);
                    }
                    else
                    {
                        e.teleportTo(teleInfo.getPosition().x, teleInfo.getPosition().y, teleInfo.getPosition().z);
                    }
                }
            }



            //Then apply children affects to targets
            super.apply(e);
        }
    }

}
