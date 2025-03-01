package com.dimensiondelvers.dimensiondelvers.item.essence;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Map;
import java.util.stream.Stream;

public class EssenceType {

    public static final ResourceKey<Registry<EssenceType>> ESSENCE_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(DimensionDelvers.id("essence_type"));

    public static final Codec<EssenceType> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name_id").forGetter(x -> x.nameId),
            Codec.unboundedMap(Ingredient.NON_AIR_HOLDER_SET_CODEC, Codec.INT).fieldOf("items").forGetter(x -> x.itemEntries)
    ).apply(instance, EssenceType::new));

    private final String nameId;
    private final MutableComponent name;

    private final Map<HolderSet<Item>, Integer> itemEntries;

    public EssenceType(String descriptionId, Map<HolderSet<Item>, Integer> values) {
        this.nameId = descriptionId;
        this.name = Component.translatable(descriptionId);
        this.itemEntries = values;
    }

    public MutableComponent getName() {
        return name;
    }

    public Stream<Map.Entry<HolderSet<Item>, Integer>> streamItems() {
        return itemEntries.entrySet().stream();
    }

}
