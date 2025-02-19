package com.dimensiondelvers.dimensiondelvers.abilities.effects.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class ParticleInfo {
    ResourceLocation userParticle;
    ResourceLocation targetParticle;
    public static final MapCodec<ParticleInfo> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("user").forGetter(ParticleInfo::getUserParticle),
                    ResourceLocation.CODEC.fieldOf("target").forGetter(ParticleInfo::getTargetParticle)
            ).apply(instance, ParticleInfo::new)
    );

    public ParticleInfo(ResourceLocation user, ResourceLocation target)
    {
        this.userParticle = user;
        this.targetParticle = target;
    }

    public ResourceLocation getUserParticle() {
        return this.userParticle;
    }

    public ResourceLocation getTargetParticle() {
        return this.targetParticle;
    }
}
