package com.dimensiondelvers.dimensiondelvers.gui.menu;

import com.dimensiondelvers.dimensiondelvers.init.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

public class AbilityBagMenu extends AbstractContainerMenu {
    private ItemStack bag;
    private  Inventory playerInventory;
    private  Container bagInventory;

    // Server Constructor
    public AbilityBagMenu(int containerId, Inventory playerInventory, ItemStack bag) {
        super(ModMenuTypes.ABILITY_BAG_MENU.get(), containerId);
        this.bag = bag;
        this.playerInventory = playerInventory;
        this.bagInventory = new ComponentContainer(bag);
        this.createInventorySlots(this.playerInventory);
        this.createBagSlots(this.bagInventory);
    }

    // Client Constructor
    public AbilityBagMenu(int containerId, Inventory inventory, RegistryFriendlyByteBuf buf) {
        this(containerId, inventory, buf.readJsonWithCodec(ItemStack.OPTIONAL_CODEC));
    }


    // Creates player inventory slots
    private void createInventorySlots(Inventory inventory) {
        int startX = 8;
        int startY = 166;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(inventory, col + row * 9 + 9, startX + col * 18, startY + row * 18));
            }
        }

        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(inventory, col, startX + col * 18, startY + 58));
        }
    }

    // Creates Ability Bag Slots
    private void createBagSlots(Container bagInventory) {
        int startX = 8;
        int startY = 20;

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(bagInventory, i, startX + i * 18, startY));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
