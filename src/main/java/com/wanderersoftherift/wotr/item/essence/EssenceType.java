package com.wanderersoftherift.wotr.item.essence;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class EssenceType {

    public static final String LANG_PREFIX = "essence_type";

    public static final Codec<EssenceType> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(EssenceType::getId)
    ).apply(instance, EssenceType::new));

    private final ResourceLocation id;
    private final MutableComponent name;

    public EssenceType(ResourceLocation id) {
        this.id = id;
        this.name = Component.translatable(LANG_PREFIX + "." + id.getNamespace() + "." + id.getPath());
    }

    public ResourceLocation getId() { return id; }

    public MutableComponent getName() {
        return name;
    }

}
