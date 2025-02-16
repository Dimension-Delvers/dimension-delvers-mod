package com.dimensiondelvers.dimensiondelvers.abilities.Serializable;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.*;

public class SerializableMap implements INBTSerializable<CompoundTag> {

    HashMap<ResourceLocation, Integer> hashmap = new HashMap<>();


    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        final CompoundTag cooldowns = new CompoundTag();
        for(ResourceLocation loc: hashmap.keySet()) {
//            final CompoundTag abilityCooldown = new CompoundTag();
            cooldowns.putInt(loc.toString(), hashmap.get(loc));
        }
//        cooldowns.putInt(DimensionDelvers.id("cooldowns"), cooldownMap.get(loc));
        return cooldowns;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag compoundTag) {
        hashmap.clear();
        for(String key :compoundTag.getAllKeys())
        {
            hashmap.put(ResourceLocation.parse(key), compoundTag.getInt(key));
        }

    }

    public void insert(ResourceLocation loc, int value)
    {
        hashmap.put(loc, value);
    }

    public void set(ResourceLocation loc, int value)
    {
        hashmap.replace(loc, value);
    }

    public int get(ResourceLocation loc)
    {
        return hashmap.getOrDefault(loc, 0);
    }

    public void remove(ResourceLocation loc)
    {
        hashmap.remove(loc);
    }

    public Set<ResourceLocation> getKeys() {
        return hashmap.keySet();
    }

    public Collection<Integer> getValues() {
        return hashmap.values();
    }

    public boolean containsKey(ResourceLocation loc)
    {
        return hashmap.containsKey(loc);
    }

    public void reduceAll(int amount)
    {
        List<ResourceLocation> toRemove = new ArrayList<>();
        for(ResourceLocation loc: this.getKeys())
        {
            this.set(loc, Math.max(this.get(loc) - amount, 0));
            if(this.get(loc) == 0) toRemove.add(loc);
        }

        toRemove.forEach(this::remove);
    }
}
