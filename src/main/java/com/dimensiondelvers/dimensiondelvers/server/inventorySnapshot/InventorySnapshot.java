package com.dimensiondelvers.dimensiondelvers.server.inventorySnapshot;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.init.ModDataComponentType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.p3pp3rf1y.sophisticatedcore.api.IStashStorageItem;

import java.util.*;

/**
 * InventorySnapshot is used to record the contents of a player's inventory at a point in time.
 */
public class InventorySnapshot {

    private final Set<UUID> itemIds;
    private final Map<Holder<Item>, Integer> items;

    public static final Codec<InventorySnapshot> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                        UUIDUtil.CODEC.listOf().fieldOf("itemIds").forGetter(x -> new ArrayList<>(x.itemIds)),
                        Codec.unboundedMap(ItemStack.ITEM_NON_AIR_CODEC, Codec.INT).fieldOf("items").forGetter(x -> x.items)
                    ).apply(instance, InventorySnapshot::new)
    );

    /**
     * Generates an InventorySnapshot for a player's inventory
     * @param player
     * @return A new InventorySnapshot
     */
    // TODO: move into system
    public static InventorySnapshot capture(ServerPlayer player) {
        Set<UUID> itemIds = new LinkedHashSet<>();
        Map<Holder<Item>, Integer> items = new HashMap<>();

        for (ItemStack item : player.getInventory().items) {
            captureItem(item, itemIds, items);
        }
        for (ItemStack item : player.getInventory().armor) {
            captureItem(item, itemIds, items);
        }
        captureItem(player.getOffhandItem(), itemIds, items);
        return new InventorySnapshot(itemIds, items);
    }

    // TODO: move into system
    private static void captureItem(ItemStack item, Set<UUID> itemIds, Map<Holder<Item>, Integer> items) {
        if (item.isEmpty()) {
            return;
        }
        if (item.isStackable()) {
            Holder<Item> holder = item.getItemHolder();
            items.put(holder, items.getOrDefault(holder, 0) + item.getCount());
        } else {
            UUID id = UUID.randomUUID();
            item.applyComponents(DataComponentPatch.builder().set(ModDataComponentType.INVENTORY_SNAPSHOT_ID.get(), id).build());
            itemIds.add(id);

            if (item.has(DataComponents.CONTAINER)) {
                ItemContainerContents contents = item.get(DataComponents.CONTAINER);
                for (ItemStack nonEmptyItem : contents.nonEmptyItems()) {
                    captureItem(nonEmptyItem, itemIds, items);
                }
            } else if (item.getItem() instanceof IStashStorageItem storageitem) {
                DimensionDelvers.LOGGER.info("Found storage item");
            }
        }
    }

    public InventorySnapshot() {
        this(Collections.emptyList(), Collections.emptyMap());
    }

    public InventorySnapshot(Collection<UUID> itemIds, Map<Holder<Item>, Integer> items) {
        this.itemIds = new HashSet<>(itemIds);
        this.items = new HashMap<>(items);
    }

    public Set<UUID> itemIds() {
        return Collections.unmodifiableSet(itemIds);
    }

    public Map<Holder<Item>, Integer> items() {
        return Collections.unmodifiableMap(items);
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
