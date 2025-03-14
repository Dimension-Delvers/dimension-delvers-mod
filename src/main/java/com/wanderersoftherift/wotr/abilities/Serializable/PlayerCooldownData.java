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
                    Codec.INT.listOf().<IntList>xmap(IntArrayList::new, FastUtils::toList).fieldOf("cooldowns").forGetter(x -> x.cooldowns)
            ).apply(instance, PlayerCooldownData::new)
    );

    private final IntList cooldowns = new IntArrayList(new int[AbilitySlots.ABILITY_BAR_SIZE]);

    public PlayerCooldownData() {

    }

    public PlayerCooldownData(IntList cooldowns) {
        this.cooldowns.clear();
        this.cooldowns.addAll(cooldowns);
    }

    public void setCooldown(int slot, int amount) {
        if (slot < 0) {
            return;
        }
        while (slot >= cooldowns.size()) {
            cooldowns.add(0);
        }
        cooldowns.set(slot, amount);
    }

    public int getCooldown(int slot)
    {
        if (slot >= 0 && slot < cooldowns.size()) {
            return cooldowns.getInt(slot);
        }
        return 0;
    }

    public boolean isOnCooldown(int slot)
    {
        return getCooldown(slot) > 0;
    }

    public void reduceCooldowns()
    {
        cooldowns.replaceAll(x -> x - 1);
    }

}
