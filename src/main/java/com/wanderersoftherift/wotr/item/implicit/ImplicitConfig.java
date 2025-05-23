package com.wanderersoftherift.wotr.item.implicit;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.init.WotrRegistries;
import com.wanderersoftherift.wotr.modifier.Modifier;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;

public record ImplicitConfig(HolderSet<Modifier> implicitModifiers) {
    public static final ImplicitConfig DEFAULT = new ImplicitConfig(HolderSet.empty());
    public static final Codec<ImplicitConfig> CODEC = RecordCodecBuilder
            .create(inst -> inst.group(RegistryCodecs.homogeneousList(WotrRegistries.Keys.MODIFIERS)
                    .fieldOf("modifiers")
                    .forGetter(ImplicitConfig::implicitModifiers)).apply(inst, ImplicitConfig::new));
}
