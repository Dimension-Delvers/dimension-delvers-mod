package com.wanderersoftherift.wotr.item.socket;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.init.WotrDataComponentType;
import com.wanderersoftherift.wotr.item.runegem.RunegemData;
import com.wanderersoftherift.wotr.item.runegem.RunegemShape;
import com.wanderersoftherift.wotr.modifier.Modifier;
import com.wanderersoftherift.wotr.modifier.ModifierInstance;
import com.wanderersoftherift.wotr.modifier.TieredModifier;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public record GearSocket(RunegemShape shape, Optional<ModifierInstance> modifier, Optional<RunegemData> runegem) {

    public static final Codec<GearSocket> CODEC = RecordCodecBuilder
            .create(inst -> inst.group(RunegemShape.CODEC.fieldOf("shape").forGetter(GearSocket::shape),
                    ModifierInstance.CODEC.optionalFieldOf("modifier").forGetter(GearSocket::modifier),
                    RunegemData.CODEC.optionalFieldOf("runegem").forGetter(GearSocket::runegem)
            ).apply(inst, GearSocket::new));

    public static GearSocket getRandomSocket(RandomSource random) {
        RunegemShape shape = RunegemShape.getRandomShape(random);
        return new GearSocket(shape, Optional.empty(), Optional.empty());
    }

    public boolean isEmpty() {
        return runegem.isEmpty() || modifier.isEmpty();
    }

    public boolean canBeApplied(
            ItemStack gear,
            Level level,
            RunegemData runegemData,
            List<GearSocket> existingSockets) {
        Set<Holder<Modifier>> existingModifiers = existingSockets.stream()
                .filter(socket -> !socket.isEmpty())
                .map(GearSocket::modifier)
                .filter(Optional::isPresent)
                .map(inst -> inst.get().modifier())
                .collect(Collectors.toSet());
        Optional<TieredModifier> tieredModifier = runegemData.getRandomTieredModifierForItem(gear, level,
                existingModifiers);
        return isEmpty() && this.shape().equals(runegemData.shape()) && tieredModifier.isPresent();
    }

    public GearSocket applyRunegem(ItemStack stack, ItemStack runegem, Level level, List<GearSocket> existingSockets) {
        Set<Holder<Modifier>> existingModifiers = existingSockets.stream()
                .filter(socket -> !socket.isEmpty())
                .map(GearSocket::modifier)
                .filter(Optional::isPresent)
                .map(inst -> inst.get().modifier())
                .collect(Collectors.toSet());
        RunegemData runegemData = runegem.get(WotrDataComponentType.RUNEGEM_DATA);
        if (runegemData == null) {
            return this;
        }
        Optional<TieredModifier> tieredModifier = runegemData.getRandomTieredModifierForItem(stack, level,
                existingModifiers);
        if (tieredModifier.isEmpty()) {
            WanderersOfTheRift.LOGGER.error("Failed to get random modifier for runegem: " + stack);
            return this;
        }
        return new GearSocket(this.shape(), Optional
                .of(ModifierInstance.of(tieredModifier.get().modifier(), tieredModifier.get().tier(), level.random)),
                Optional.of(runegemData));
    }
}