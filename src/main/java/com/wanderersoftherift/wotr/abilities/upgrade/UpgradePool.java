package com.wanderersoftherift.wotr.abilities.upgrade;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.abilities.AbstractAbility;
import com.wanderersoftherift.wotr.modifier.effect.AbstractModifierEffect;
import com.wanderersoftherift.wotr.util.FastUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntLists;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class UpgradePool {
    public static final int UNSELECTED = -1;
    public static final Codec<UpgradePool> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Upgrade.REGISTRY_CODEC.listOf().listOf().fieldOf("choices").forGetter(x -> x.choices),
            Codec.INT.listOf().<IntList>xmap(IntArrayList::new, FastUtils::toList).fieldOf("selectedUpgrades").forGetter(x -> x.selectedUpgrades)
    ).apply(instance, UpgradePool::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, UpgradePool> STREAM_CODEC = StreamCodec
            .composite(
                    ByteBufCodecs.holderRegistry(Upgrade.UPGRADE_REGISTRY_KEY).apply(ByteBufCodecs.list()).apply(ByteBufCodecs.list()), x -> x.choices,
                    ByteBufCodecs.INT.apply(ByteBufCodecs.list()).map(IntArrayList::new, FastUtils::toList), x -> x.selectedUpgrades,
                    UpgradePool::new
            );
    public static final int SELECTION_PER_LEVEL = 3;

    protected final List<List<Holder<Upgrade>>> choices;
    protected final IntList selectedUpgrades;

    public UpgradePool(List<List<Holder<Upgrade>>> choices, IntList selectedUpgrades) {
        this.choices = new ArrayList<>();
        for (List<Holder<Upgrade>> choice : choices) {
            this.choices.add(new ArrayList<>(choice));
        }
        this.selectedUpgrades = new IntArrayList(selectedUpgrades);
    }

    public UpgradePool(UpgradePool other) {
        this(other.choices, other.selectedUpgrades);
    }

    public int getChoiceCount() {
        return choices.size();
    }

    public List<Holder<Upgrade>> getChoice(int index) {
        return Collections.unmodifiableList(choices.get(index));
    }

    public int getSelectedIndex(int choice) {
        if (choice >= selectedUpgrades.size()) {
            return UNSELECTED;
        }
        return selectedUpgrades.getInt(choice);
    }

    public Optional<Upgrade> getSelectionForChoice(int choice) {
        if (choice >= selectedUpgrades.size()) {
            return Optional.empty();
        }
        int index = selectedUpgrades.getInt(choice);
        if (index >= 0 && index < choices.get(choice).size()) {
            return Optional.of(choices.get(choice).get(index).value());
        }
        return Optional.empty();
    }

    public Object2IntMap<Upgrade> getAllSelected() {
        Object2IntArrayMap<Upgrade> result = new Object2IntArrayMap<>();
        for (int i = 0; i < getChoiceCount(); i++) {
            getSelectionForChoice(i).ifPresent(upgrade -> result.merge(upgrade, 1, Integer::sum));
        }
        return result;
    }

    public void forEachSelected(BiConsumer<Upgrade, Integer> consumer) {
        for (int i = 0; i < getChoiceCount(); i++) {
            final int choice = i;
            getSelectionForChoice(i).ifPresent(x -> consumer.accept(x, choice));
        }
    }

    public UpgradePool.Mutable getMutable() {
        return new Mutable(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof UpgradePool other) {
            return Objects.equals(choices, other.choices) && Objects.equals(selectedUpgrades, other.selectedUpgrades);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(choices, selectedUpgrades);
    }

    public static final class Mutable extends UpgradePool {

        public Mutable() {
            this(Collections.emptyList(), IntLists.emptyList());
        }


        public Mutable(List<List<Holder<Upgrade>>> choices, IntList selectedChoices) {
            super(choices, selectedChoices);
        }

        public Mutable(UpgradePool other) {
            super(other);
        }

        public UpgradePool toImmutable() {
            return new UpgradePool(this);
        }

        public Mutable selectChoice(int choice, int selection) {
            Preconditions.checkArgument(choice >= 0 && choice < choices.size());
            Preconditions.checkArgument(selection >= 0 && selection < choices.get(choice).size());
            while (selectedUpgrades.size() <= choice) {
                selectedUpgrades.add(UNSELECTED);
            }
            selectedUpgrades.set(choice, selection);
            return this;
        }

        public Mutable generateChoice(RegistryAccess registryAccess, AbstractAbility ability, RandomSource random, int optionCount) {
            generateChoices(registryAccess, ability, 1, random, optionCount);
            return this;
        }

        public Mutable generateChoices(RegistryAccess registryAccess, AbstractAbility ability, int count, RandomSource random, int optionCount) {
            Object2IntMap<Holder<Upgrade>> availableUpgrades = determineChoices(registryAccess, ability);

            for (int i = 0; i < count; i++) {
                ObjectList<Holder<Upgrade>> upgradeSet = new ObjectArrayList<>(availableUpgrades.keySet());
                List<Holder<Upgrade>> choice = new ArrayList<>();
                for (int j = 0; j < optionCount && !upgradeSet.isEmpty(); j++) {
                    choice.add(upgradeSet.remove(random.nextInt(upgradeSet.size())));
                }
                for (Holder<Upgrade> item : choice) {
                    int previousCount = availableUpgrades.getInt(item);
                    availableUpgrades.put(item, previousCount - 1);
                }
                choices.add(choice);
                selectedUpgrades.add(UNSELECTED);
            }
            return this;
        }

        private Object2IntMap<Holder<Upgrade>> determineChoices(RegistryAccess registryAccess, AbstractAbility ability) {
            Registry<Upgrade> upgrades = registryAccess.lookupOrThrow(Upgrade.UPGRADE_REGISTRY_KEY);
            Object2IntArrayMap<Holder<Upgrade>> availableUpgrades = upgrades.stream().filter(x -> isRelevant(x, ability)).map(upgrades::wrapAsHolder).collect(Collectors.toMap(x -> x, x -> x.value().maxCount(), Integer::sum, Object2IntArrayMap::new));
            choices.forEach(options -> options.forEach((item) -> availableUpgrades.mergeInt(item, 0, (a, b) -> a + b - 1)));
            return availableUpgrades;
        }

        private boolean isRelevant(Upgrade upgrade, AbstractAbility ability) {
            for (AbstractModifierEffect modifierEffect : upgrade.modifierEffects()) {
                if (!ability.isRelevantModifier(modifierEffect)) {
                    return false;
                }
            }
            return true;
        }
    }
}
