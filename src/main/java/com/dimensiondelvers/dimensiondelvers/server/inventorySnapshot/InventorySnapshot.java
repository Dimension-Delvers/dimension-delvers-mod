package com.dimensiondelvers.dimensiondelvers.server.inventorySnapshot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;

import java.util.*;

/**
 * InventorySnapshot is used to record the contents of a player's inventory at a point in time.
 */
public class InventorySnapshot {

    private final Set<UUID> itemIds;
    private final List<ItemStack> items;

    public static final Codec<ItemStack> NONSTRICT_ITEMSTACK_CODEC = Codec.lazyInitialized(
            () -> RecordCodecBuilder.create(
                    instance -> instance.group(
                                    ItemStack.ITEM_NON_AIR_CODEC.fieldOf("id").forGetter(ItemStack::getItemHolder),
                                    ExtraCodecs.POSITIVE_INT.fieldOf("count").forGetter(ItemStack::getCount),
                                    DataComponentPatch.CODEC
                                            .optionalFieldOf("components", DataComponentPatch.EMPTY)
                                            .forGetter(x -> {
                                                if (x.getComponents() instanceof PatchedDataComponentMap patchedMap) {
                                                    return patchedMap.asPatch();
                                                }
                                                return DataComponentPatch.EMPTY;
                                            })
                            )
                            .apply(instance, ItemStack::new)
            )
    );

    public static final Codec<InventorySnapshot> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    UUIDUtil.CODEC.listOf().fieldOf("itemIds").forGetter(x -> new ArrayList<>(x.itemIds)),
                    NONSTRICT_ITEMSTACK_CODEC.listOf().fieldOf("items").forGetter(x -> x.items)
            ).apply(instance, InventorySnapshot::new)
    );

    public InventorySnapshot() {
        this(Collections.emptyList(), Collections.emptyList());
    }

    public InventorySnapshot(Collection<UUID> itemIds, Collection<ItemStack> items) {
        this.itemIds = new HashSet<>(itemIds);
        this.items = new ArrayList<>(items);
    }

    public Set<UUID> itemIds() {
        return Collections.unmodifiableSet(itemIds);
    }

    public List<ItemStack> items() {
        return Collections.unmodifiableList(items);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof InventorySnapshot other) {
            return Objects.equals(itemIds, other.itemIds) && Objects.equals(items, other.items);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemIds, items);
    }
}
