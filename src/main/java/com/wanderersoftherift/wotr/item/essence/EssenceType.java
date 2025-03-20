package com.wanderersoftherift.wotr.item.essence;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * Definition of an essence type, which is a category for the essence an item can contain.
 */
public class EssenceType {

    public static final Codec<EssenceType> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(EssenceType::getId)
    ).apply(instance, EssenceType::new));

    public static final String LANG_PREFIX = "essence_type";

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
