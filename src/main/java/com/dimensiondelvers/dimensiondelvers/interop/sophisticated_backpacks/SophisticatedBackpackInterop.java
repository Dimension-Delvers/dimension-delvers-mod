package com.dimensiondelvers.dimensiondelvers.interop.sophisticated_backpacks;

import com.dimensiondelvers.dimensiondelvers.server.inventorySnapshot.ContainerItemWrapper;
import com.dimensiondelvers.dimensiondelvers.server.inventorySnapshot.ContainerType;
import com.dimensiondelvers.dimensiondelvers.server.inventorySnapshot.InventorySnapshotSystem;
import com.dimensiondelvers.dimensiondelvers.server.inventorySnapshot.ItemStackHandlerContainers;
import com.google.common.collect.Iterators;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.IBackpackWrapper;
import net.p3pp3rf1y.sophisticatedcore.inventory.InventoryHandler;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class SophisticatedBackpackInterop {
    private SophisticatedBackpackInterop() {
    }

    public static void load() {
        InventorySnapshotSystem.getInstance().registerContainerStrategy(new SophisticatedBackpackType());
    }

    private static class SophisticatedBackpackType implements ContainerType {

        @Override
        public boolean isContainer(ItemStack item) {
            return item.getItem() instanceof BackpackItem;
        }

        @Override
        public Iterable<ContainerItemWrapper> iterateContainerContents(ItemStack item) {
            return new Iterable<>() {
                @Override
                public @NotNull Iterator<ContainerItemWrapper> iterator() {
                    IBackpackWrapper backpackWrapper = BackpackWrapper.fromStack(item);
                    InventoryHandler inventory = backpackWrapper.getInventoryHandler();
                    UpgradeHandler upgrades = backpackWrapper.getUpgradeHandler();
                    return Iterators.concat(ItemStackHandlerContainers.iterateNonEmpty(inventory), ItemStackHandlerContainers.iterateNonEmpty(upgrades));
                }
            };
        }
    }
}
