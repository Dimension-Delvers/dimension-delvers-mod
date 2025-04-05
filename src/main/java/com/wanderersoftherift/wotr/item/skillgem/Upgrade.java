package com.wanderersoftherift.wotr.item.skillgem;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.modifier.effect.AbstractModifierEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.List;

public record Upgrade(Component name, Component description, ResourceLocation icon, int maxCount,
                      List<AbstractModifierEffect> modifierEffects) {

    public static final ResourceKey<Registry<Upgrade>> UPGRADE_REGISTRY_KEY = ResourceKey.createRegistryKey(WanderersOfTheRift.id("upgrade"));

    public static final Codec<Holder<Upgrade>> REGISTRY_CODEC = RegistryFixedCodec.create(UPGRADE_REGISTRY_KEY);

    public static final Codec<Upgrade> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("name").forGetter(Upgrade::name),
            ComponentSerialization.CODEC.fieldOf("description").forGetter(Upgrade::description),
            ResourceLocation.CODEC.fieldOf("icon").forGetter(Upgrade::icon),
            Codec.INT.fieldOf("maxCount").forGetter(Upgrade::maxCount),
            AbstractModifierEffect.DIRECT_CODEC.listOf().optionalFieldOf("effects", Collections.emptyList()).forGetter(Upgrade::modifierEffects)
    ).apply(instance, Upgrade::new));

}
