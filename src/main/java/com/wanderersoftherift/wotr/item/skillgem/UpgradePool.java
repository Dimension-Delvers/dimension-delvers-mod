package com.wanderersoftherift.wotr.item.skillgem;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wanderersoftherift.wotr.util.FastUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntLists;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;

import java.util.*;

public class UpgradePool {
    public static final int UNSELECTED = -1;
    public static final Codec<UpgradePool> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(Upgrade.REGISTRY_CODEC, Codec.INT).<Object2IntMap<Holder<Upgrade>>>xmap(Object2IntArrayMap::new, FastUtils::toMap).fieldOf("availableUpgrades").forGetter(UpgradePool::getAvailableUpgrades),
            Upgrade.REGISTRY_CODEC.listOf().listOf().fieldOf("choices").forGetter(x -> x.choices),
            Codec.INT.listOf().<IntList>xmap(IntArrayList::new, FastUtils::toList).fieldOf("selectedUpgrades").forGetter(x -> x.selectedUpgrades)
    ).apply(instance, UpgradePool::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, UpgradePool> STREAM_CODEC = StreamCodec
            .composite(
                    ByteBufCodecs.map(
                            Object2IntArrayMap::new,
                            ByteBufCodecs.holderRegistry(Upgrade.UPGRADE_REGISTRY_KEY),
                            ByteBufCodecs.INT
                    ), UpgradePool::getAvailableUpgrades,
                    ByteBufCodecs.holderRegistry(Upgrade.UPGRADE_REGISTRY_KEY).apply(ByteBufCodecs.list()).apply(ByteBufCodecs.list()), x -> x.choices,
                    ByteBufCodecs.INT.apply(ByteBufCodecs.list()).map(IntArrayList::new, FastUtils::toList), x -> x.selectedUpgrades,
                    UpgradePool::new
            );

    protected final Object2IntMap<Holder<Upgrade>> availableUpgrades;
    protected final List<List<Holder<Upgrade>>> choices;
    protected final IntList selectedUpgrades;

    public UpgradePool(Object2IntMap<Holder<Upgrade>> availableUpgrades, List<List<Holder<Upgrade>>> choices, IntList selectedUpgrades) {
        this.availableUpgrades = new Object2IntArrayMap<>(availableUpgrades);
        this.choices = new ArrayList<>();
        for (List<Holder<Upgrade>> choice : choices) {
            this.choices.add(new ArrayList<>(choice));
        }
        this.selectedUpgrades = new IntArrayList(selectedUpgrades);
    }

    public UpgradePool(UpgradePool other) {
        this(other.availableUpgrades, other.choices, other.selectedUpgrades);
    }

    public Object2IntMap<Holder<Upgrade>> getAvailableUpgrades() {
        return Object2IntMaps.unmodifiable(availableUpgrades);
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

    public UpgradePool.Mutable getMutable() {
        return new Mutable(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof UpgradePool other) {
            return Objects.equals(availableUpgrades, other.availableUpgrades) && Objects.equals(choices, other.choices) && Objects.equals(selectedUpgrades, other.selectedUpgrades);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(availableUpgrades, choices, selectedUpgrades);
    }

    public static final class Mutable extends UpgradePool {

        public Mutable() {
            this(Object2IntMaps.emptyMap(), Collections.emptyList(), IntLists.emptyList());
        }

        public Mutable(Collection<Holder<Upgrade>> upgrades) {
            this(Object2IntMaps.emptyMap(), Collections.emptyList(), IntLists.emptyList());
            for (Holder<Upgrade> upgrade : upgrades) {
                availableUpgrades.put(upgrade, upgrade.value().maxCount());
            }
        }

        public Mutable(Object2IntMap<Holder<Upgrade>> availableUpgrades, List<List<Holder<Upgrade>>> choices, IntList selectedChoices) {
            super(availableUpgrades, choices, selectedChoices);
        }

        public Mutable(UpgradePool other) {
            super(other);
        }

        public UpgradePool toImmutable() {
            return new UpgradePool(this);
        }

        public void selectChoice(int choice, int selection) {
            Preconditions.checkArgument(choice >= 0 && choice < choices.size());
            Preconditions.checkArgument(selection >= 0 && selection < choices.get(choice).size());
            while (selectedUpgrades.size() <= choice) {
                selectedUpgrades.add(UNSELECTED);
            }
            selectedUpgrades.set(choice, selection);
        }

        public void generateChoice(RandomSource random, int optionCount) {
            ObjectList<Holder<Upgrade>> upgradeSet = new ObjectArrayList<>(availableUpgrades.keySet());
            List<Holder<Upgrade>> choice = new ArrayList<>();
            for (int i = 0; i < optionCount && !upgradeSet.isEmpty(); i++) {
                choice.add(upgradeSet.remove(random.nextInt(upgradeSet.size())));
            }
            for (Holder<Upgrade> item : choice) {
                int previousCount = availableUpgrades.getInt(item);
                if (previousCount > 1) {
                    availableUpgrades.put(item, previousCount - 1);
                } else {
                    availableUpgrades.removeInt(item);
                }
            }
            choices.add(choice);
            selectedUpgrades.add(UNSELECTED);
        }

        public void generateChoices(int count, RandomSource random, int optionCount) {
            for (int i = 0; i < count; i++) {
                generateChoice(random, optionCount);
            }
        }
    }
}
