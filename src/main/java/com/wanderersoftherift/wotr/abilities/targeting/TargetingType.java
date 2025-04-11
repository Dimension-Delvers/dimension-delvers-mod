package com.wanderersoftherift.wotr.abilities.targeting;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

public enum TargetingType implements StringRepresentable {
    AREA("area", 0),
    SELF("self", 1),
    RAYCAST("raycast", 2);

    public static final IntFunction<TargetingType> BY_ID = ByIdMap.continuous(TargetingType::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, TargetingType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, TargetingType::id);
    public static final Codec<TargetingType> CODEC = StringRepresentable.fromEnum(TargetingType::values);
    private final String name;
    private final int id;

    TargetingType(String name, int value) {
        this.name = name;
        this.id = value;
    }

    public int id() {
        return this.id;
    }

    public String getSerializedName() {
        return this.name;
    }
}