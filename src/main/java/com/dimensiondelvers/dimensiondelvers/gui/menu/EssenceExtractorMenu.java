package com.dimensiondelvers.dimensiondelvers.gui.menu;

import com.dimensiondelvers.dimensiondelvers.block.blockentity.EssenceExtractorBlockEntity;
import com.dimensiondelvers.dimensiondelvers.gui.menu.slots.TakeOnlySlot;
import com.dimensiondelvers.dimensiondelvers.init.ModMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EssenceExtractorMenu extends AbstractContainerMenu {

    private final int PLAYER_INVENTORY_SLOTS = 4 * 9;
    private final Container container;
    private final ContainerData data;

    public EssenceExtractorMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(EssenceExtractorBlockEntity.NUM_ITEMS), new SimpleContainerData(EssenceExtractorBlockEntity.NUM_DATA_ITEMS));
    }

    public EssenceExtractorMenu(int containerId, Inventory playerInventory, Container container, ContainerData dataAccess) {
        super(ModMenuTypes.ESSENCE_EXTRACTOR_MENU.get(), containerId);
        this.container = container;
        this.data = dataAccess;
        checkContainerSize(container, EssenceExtractorBlockEntity.NUM_ITEMS);

        addStandardInventorySlots(playerInventory, 8, 84);

        addSlot(new Slot(container, 0, 26, 35));
        addSlot(new TakeOnlySlot(container, 1, 132, 35));

        addDataSlots(dataAccess);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return container.stillValid(player);
    }

    public float getProgress() {
        int goal = data.get(EssenceExtractorBlockEntity.DATA_GOAL);
        if (goal == 0) {
            return 0;
        }
        int progress = data.get(EssenceExtractorBlockEntity.DATA_PROGRESS);
        return Math.clamp((float) progress / goal, 0, 1);
    }

    protected boolean quickMoveToContainer(ItemStack stack) {
        return this.moveItemStackToContainerSlot(stack, EssenceExtractorBlockEntity.INPUT_ITEM, EssenceExtractorBlockEntity.INPUT_ITEM + 1, false);
    }

    protected final boolean moveItemStackToContainerSlot(ItemStack stack, int firstSlot, int lastSlot, boolean reverseOrder) {
        return this.moveItemStackTo(stack, firstSlot + PLAYER_INVENTORY_SLOTS, lastSlot + PLAYER_INVENTORY_SLOTS, reverseOrder);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack intermediateStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack sourceStack = slot.getItem();
            intermediateStack = sourceStack.copy();
            if (index < PLAYER_INVENTORY_SLOTS) {
                if (!quickMoveToContainer(sourceStack)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(sourceStack, 0, PLAYER_INVENTORY_SLOTS, true)) {
                return ItemStack.EMPTY;
            }

            if (sourceStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return intermediateStack;
    }
}
