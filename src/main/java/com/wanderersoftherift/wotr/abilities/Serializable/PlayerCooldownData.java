package com.wanderersoftherift.wotr.abilities.Serializable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.item.skillgem.AbilitySlots;
import com.wanderersoftherift.wotr.util.FastUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

public class PlayerCooldownData {

    public static final Codec<PlayerCooldownData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.listOf().<IntList>xmap(IntArrayList::new, FastUtils::toList).fieldOf("cooldowns").forGetter(x -> x.currentCooldowns),
                    Codec.INT.listOf().<IntList>xmap(IntArrayList::new, FastUtils::toList).fieldOf("lastCooldowns").forGetter(x -> x.lastCooldowns)
            ).apply(instance, PlayerCooldownData::new)
    );

    private final IntList lastCooldowns;
    private final IntList currentCooldowns;

    public PlayerCooldownData() {
        lastCooldowns = new IntArrayList(new int[AbilitySlots.ABILITY_BAR_SIZE]);
        currentCooldowns = new IntArrayList(new int[AbilitySlots.ABILITY_BAR_SIZE]);
    }

    public PlayerCooldownData(IntList lastCooldowns, IntList currentCooldowns) {
        this.currentCooldowns = new IntArrayList(currentCooldowns);
        this.lastCooldowns = new IntArrayList(lastCooldowns);
    }

    public void setCooldown(int slot, int amount) {
        if (slot < 0) {
            return;
        }
        while (slot >= currentCooldowns.size()) {
            currentCooldowns.add(0);
        }
        while (slot >= lastCooldowns.size()) {
            lastCooldowns.add(0);
        }
        currentCooldowns.set(slot, amount);
        lastCooldowns.set(slot, amount);
    }

    public int getCooldownRemaining(int slot)
    {
        if (slot >= 0 && slot < currentCooldowns.size()) {
            return currentCooldowns.getInt(slot);
        }
        return 0;
    }

    public int getLastCooldownValue(int slot) {
        if (slot >= 0 && slot < lastCooldowns.size()) {
            return lastCooldowns.getInt(slot);
        }
        return 0;
    }

    public boolean isOnCooldown(int slot)
    {
        return getCooldownRemaining(slot) > 0;
    }

    public void reduceCooldowns()
    {
        currentCooldowns.replaceAll(x -> x > 0 ? x - 1 : 0);
    }

}
