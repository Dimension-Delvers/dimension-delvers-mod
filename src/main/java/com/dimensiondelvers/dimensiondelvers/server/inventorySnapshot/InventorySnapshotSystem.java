package com.dimensiondelvers.dimensiondelvers.server.inventorySnapshot;

import com.dimensiondelvers.dimensiondelvers.init.ModAttachments;
import com.dimensiondelvers.dimensiondelvers.init.ModDataComponentType;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.IBackpackWrapper;
import net.p3pp3rf1y.sophisticatedcore.inventory.InventoryHandler;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeHandler;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * System for capturing Inventory Snapshots and applying them on death and respawn of a player
 * <p>
 * The envisioned behavior is:
 * </p>
 * <ul>
 *     <li>When a snapshot is initially captured, all items in the players inventory and sub-inventories are enumerated</li>
 *     <li>When a player dies, the player's inventory is compared to the snapshot. Their inventory is split into a set of items that they have from the snapshot,
 *     and a set of items that are new</li>
 *     <li>When a player respawns the items they still had from the snapshot are returned and the snapshot is removed</li>
 *     <li>All other items are dropped where they died</li>
 * </ul>
 * <p>
 * Snapshots are created by adding a unique snapshot id to any non-stackable items, and directly record any stackable, with the assumption that stackable items will
 * not vary in a non-comparable manner.
 * </p>
 */
public class InventorySnapshotSystem {

    private static final DataComponentPatch REMOVE_SNAPSHOT_ID_PATCH = DataComponentPatch.builder().remove(ModDataComponentType.INVENTORY_SNAPSHOT_ID.get()).build();

    private static final InventorySnapshotSystem instance = new InventorySnapshotSystem();

    public static InventorySnapshotSystem getInstance() {
        return instance;
    }

    private InventorySnapshotSystem() {
    }

    /**
     * Generates a snapshot for the given player
     *
     * @param player
     */
    public void captureSnapshot(ServerPlayer player) {
        clearItemIds(player);
        player.setData(ModAttachments.INVENTORY_SNAPSHOT, new InventorySnapshotBuilder(player).build());
    }

    /**
     * Clears any snapshot on the player
     *
     * @param player
     */
    public void clearSnapshot(ServerPlayer player) {
        clearItemIds(player);
        player.setData(ModAttachments.INVENTORY_SNAPSHOT, new InventorySnapshot());
    }

    /**
     * Updates the snapshot for the player's death. This will reduce the captured items to what the player
     * still had on them at time of death.
     *
     * @param player
     * @param event
     */
    public void retainSnapshotItemsOnDeath(ServerPlayer player, LivingDropsEvent event) {
        InventorySnapshot snapshot = player.getData(ModAttachments.INVENTORY_SNAPSHOT);
        if (snapshot.isEmpty()) {
            return;
        }

        RespawnItemsCalculator refiner = new RespawnItemsCalculator(player, snapshot, event.getDrops());

        event.getDrops().clear();
        event.getDrops().addAll(refiner.dropItems);

        player.setData(ModAttachments.RESPAWN_ITEMS, refiner.retainItems);
    }

    /**
     * Populate the player's inventory with all items from their snapshot, drop any that don't fit
     *
     * @param player
     */
    public void restoreItemsOnRespawn(ServerPlayer player) {
        for (ItemStack item : player.getData(ModAttachments.RESPAWN_ITEMS)) {
            if (!player.getInventory().add(item)) {
                item.applyComponents(REMOVE_SNAPSHOT_ID_PATCH);
                player.level().addFreshEntity(new ItemEntity(player.level(), player.position().x, player.position().y, player.position().z, item));
            }
        }
        player.setData(ModAttachments.RESPAWN_ITEMS, new ArrayList<>());
        clearItemIds(player);
    }

    public static final class InventorySnapshotBuilder {

        private UUID snapshotId = UUID.randomUUID();
        private List<ItemStack> items = new ArrayList<>();

        private DataComponentPatch addSnapshotIdPatch = DataComponentPatch.builder().set(ModDataComponentType.INVENTORY_SNAPSHOT_ID.get(), snapshotId).build();

        /**
         * Generates an InventorySnapshot for a player's inventory
         *
         * @param player
         * @return A new InventorySnapshot
         */
        public InventorySnapshotBuilder(ServerPlayer player) {
            for (ItemStack item : player.getInventory().items) {
                captureItem(item, item::applyComponents);
            }
            for (ItemStack item : player.getInventory().armor) {
                captureItem(item, item::applyComponents);
            }
            captureItem(player.getOffhandItem(), player.getOffhandItem()::applyComponents);
        }

        public InventorySnapshot build() {
            return new InventorySnapshot(snapshotId, items);
        }

        private void captureItem(ItemStack item, Consumer<DataComponentPatch> dataComponentPatchStrategy) {
            if (item.isEmpty()) {
                return;
            }
            if (item.isStackable()) {
                items.add(item.copy());
            } else {
                dataComponentPatchStrategy.accept(addSnapshotIdPatch);

                if (item.has(DataComponents.CONTAINER)) {
                    ItemContainerContents contents = item.get(DataComponents.CONTAINER);
                    for (ItemStack nonEmptyItem : contents.nonEmptyItems()) {
                        captureItem(nonEmptyItem, item::applyComponents);
                    }
                } else if (item.getItem() instanceof BackpackItem) {
                    IBackpackWrapper backpackWrapper = BackpackWrapper.fromStack(item);
                    InventoryHandler inventory = backpackWrapper.getInventoryHandler();
                    for (int slot = 0; slot < inventory.getSlots(); slot++) {
                        captureItem(inventory.getStackInSlot(slot), new ItemHandlerPatcher(inventory, slot));
                    }
                    UpgradeHandler upgrades = backpackWrapper.getUpgradeHandler();
                    for (int slot = 0; slot < upgrades.getSlots(); slot++) {
                        captureItem(upgrades.getStackInSlot(slot), new ItemHandlerPatcher(inventory, slot));
                    }
                }
            }
        }

    }

    private static final class RespawnItemsCalculator {
        private final List<ItemStack> retainItems = new ArrayList<>();
        private final List<ItemEntity> dropItems = new ArrayList<>();

        private ServerPlayer player;
        private List<ItemStack> snapshotItems;
        private UUID snapshotId;

        public RespawnItemsCalculator(ServerPlayer player, InventorySnapshot snapshot, Collection<ItemEntity> heldItems) {
            this.player = player;
            this.snapshotItems = new ArrayList<>(snapshot.items());
            this.snapshotId = snapshot.id();
            processInventoryItems(heldItems);
        }

        private void processInventoryItems(Collection<ItemEntity> drops) {
            for (ItemEntity itemEntity : drops) {
                ItemStack item = itemEntity.getItem();

                if (item.isStackable()) {
                    int dropCount = calculateDropCount(item);
                    if (dropCount < item.getCount()) {
                        retainItems.add(item.split(item.getCount() - dropCount));
                    }
                    if (!item.isEmpty()) {
                        dropItems.add(itemEntity);
                    }
                } else {
                    boolean retainItem = shouldRetainNonStackable(item);
                    if (item.has(DataComponents.CONTAINER)) {
                        processContainerContents(item, retainItem);
                    } else if (item.getItem() instanceof BackpackItem) {
                        processBackpack(item, retainItem);
                    }

                    if (retainItem) {
                        retainItems.add(item);
                    } else {
                        itemEntity.getItem().applyComponents(REMOVE_SNAPSHOT_ID_PATCH);
                        dropItems.add(itemEntity);
                    }
                }
            }
        }

        private boolean shouldRetainNonStackable(ItemStack item) {
            return item.getComponents().has(ModDataComponentType.INVENTORY_SNAPSHOT_ID.get()) && snapshotId.equals(item.getComponents().get(ModDataComponentType.INVENTORY_SNAPSHOT_ID.get()));
        }

        private void processBackpack(ItemStack backpack, boolean retainingContainer) {
            IBackpackWrapper backpackWrapper = BackpackWrapper.fromStack(backpack);
            InventoryHandler inventoryHandler = backpackWrapper.getInventoryHandler();
            for (int slot = 0; slot < inventoryHandler.getSlots(); slot++) {
                ItemStack item = inventoryHandler.getStackInSlot(slot);
                if (item.isEmpty()) {
                    continue;
                }
                processContainerItem(item, retainingContainer, new ItemHandlerSplitter(inventoryHandler, slot), new ItemHandlerRemover(inventoryHandler, slot));
            }
            UpgradeHandler upgradeHandler = backpackWrapper.getUpgradeHandler();
            for (int slot = 0; slot < upgradeHandler.getSlots(); slot++) {
                ItemStack item = upgradeHandler.getStackInSlot(slot);
                if (item.isEmpty()) {
                    continue;
                }
                processContainerItem(item, retainingContainer, new ItemHandlerSplitter(upgradeHandler, slot), new ItemHandlerRemover(upgradeHandler, slot));
            }
        }

        // If we're retaining the container
        // - Any item that should be retained keep in container
        // - Any item that we don't want, copy and clear and drop the copy
        // If we're not retaining the container
        // - Any item that should be retained copy and clear and put the copy in retain
        // - Any item that we don't want, keep in container
        private void processContainerItem(ItemStack item, boolean retainingContainer, ItemStackSplitter splitFunction, Supplier<ItemStack> removeFunction) {
            if (item.isStackable()) {
                int dropCount = calculateDropCount(item);

                if (retainingContainer && dropCount > 0) {
                    dropItems.addAll(createItemEntity(splitFunction.apply(dropCount)));
                } else if (!retainingContainer && dropCount < item.getCount()) {
                    retainItems.addAll(splitFunction.apply(item.getCount() - dropCount));
                }
            } else {
                boolean retainItem = shouldRetainNonStackable(item);
                if (item.has(DataComponents.CONTAINER)) {
                    processContainerContents(item, retainItem);
                } else if (item.getItem() instanceof BackpackItem) {
                    processBackpack(item, retainItem);
                }

                if (retainingContainer && !retainItem) {
                    ItemStack dropItem = removeFunction.get();
                    dropItem.applyComponents(REMOVE_SNAPSHOT_ID_PATCH);
                    dropItems.addAll(createItemEntity(Collections.singletonList(dropItem)));
                } else if (!retainingContainer && retainItem) {
                    retainItems.add(removeFunction.get());
                }
            }
        }

        private void processContainerContents(ItemStack container, boolean retainingContainer) {
            ItemContainerContents itemContainerContents = container.get(DataComponents.CONTAINER);
            for (ItemStack item : itemContainerContents.nonEmptyItems()) {
                processContainerItem(item, retainingContainer, amount -> Collections.singletonList(item.split(amount)), item::copyAndClear);
            }
        }

        private int calculateDropCount(ItemStack item) {
            int dropCount = item.getCount();
            // Walk through the list of snapshotted items, reducing stack counts of matching stacks until all items are accounted for
            int index = 0;
            while (dropCount > 0 && index < snapshotItems.size()) {
                ItemStack snapshotItem = snapshotItems.get(index);
                if (ItemStack.isSameItemSameComponents(item, snapshotItem)) {
                    if (dropCount <= snapshotItem.getCount()) {
                        snapshotItem.shrink(dropCount);
                        dropCount = 0;
                    } else {
                        snapshotItems.remove(index);
                        dropCount -= snapshotItem.getCount();
                    }
                } else {
                    index++;
                }
            }
            return dropCount;
        }

        private List<ItemEntity> createItemEntity(List<ItemStack> stacks) {
            List<ItemEntity> entities = new ArrayList<>();
            for (ItemStack stack : stacks) {
                entities.add(new ItemEntity(player.level(), player.position().x, player.position().y, player.position().z, stack));
            }
            return entities;
        }
    }

    /**
     * Clears all snapshot item id components from items in the player's inventory
     *
     * @param player
     */
    private static void clearItemIds(ServerPlayer player) {
        DataComponentPatch removeIdPatch = DataComponentPatch.builder().remove(ModDataComponentType.INVENTORY_SNAPSHOT_ID.get()).build();
        for (ItemStack item : player.getInventory().items) {
            item.applyComponents(removeIdPatch);
        }
        for (ItemStack item : player.getInventory().armor) {
            item.applyComponents(removeIdPatch);
        }
        for (ItemStack item : player.getInventory().offhand) {
            item.applyComponents(removeIdPatch);
        }
    }

    public record ItemHandlerPatcher(IItemHandler handler, int slot) implements Consumer<DataComponentPatch> {
        @Override
        public void accept(DataComponentPatch dataComponentPatch) {
            ItemStack stack = handler.extractItem(slot, handler.getStackInSlot(slot).getCount(), false);
            stack.applyComponents(dataComponentPatch);
            handler.insertItem(slot, stack, false);
        }
    }

    public interface ItemStackSplitter {
        List<ItemStack> apply(int amount);
    }

    public record ItemHandlerSplitter(IItemHandler handler, int slot) implements ItemStackSplitter {
        @Override
        public List<ItemStack> apply(int amount) {
            ItemStack existing = handler.getStackInSlot(slot);
            List<ItemStack> result = new ArrayList<>();
            while (amount > existing.getMaxStackSize()) {
                result.add(handler.extractItem(slot, existing.getMaxStackSize(), false));
                amount -= existing.getMaxStackSize();
            }
            result.add(handler.extractItem(slot, amount, false));
            return result;
        }
    }

    public record ItemHandlerRemover(IItemHandler handler, int slot) implements Supplier<ItemStack> {
        @Override
        public ItemStack get() {
            return handler.extractItem(slot, handler.getStackInSlot(slot).getCount(), false);
        }
    }
}
