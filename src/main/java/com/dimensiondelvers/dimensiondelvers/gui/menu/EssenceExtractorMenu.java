package com.dimensiondelvers.dimensiondelvers.gui.menu;

import com.dimensiondelvers.dimensiondelvers.block.blockentity.EssenceExtractorBlockEntity;
import com.dimensiondelvers.dimensiondelvers.gui.menu.slots.TakeOnlySlot;
import com.dimensiondelvers.dimensiondelvers.init.ModMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EssenceExtractorMenu extends AbstractContainerMenu {

    private Container container;

    public EssenceExtractorMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(EssenceExtractorBlockEntity.SIZE));
    }

    public EssenceExtractorMenu(int containerId, Inventory playerInventory, Container container) {
        super(ModMenuTypes.ESSENCE_EXTRACTOR_MENU.get(), containerId);
        this.container = container;
        checkContainerSize(container, EssenceExtractorBlockEntity.SIZE);

        addSlot(new Slot(container, 0, 26, 35));
        addSlot(new TakeOnlySlot(container, 1, 132, 35));


        addStandardInventorySlots(playerInventory, 8, 84);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return container.stillValid(player);
    }
}
