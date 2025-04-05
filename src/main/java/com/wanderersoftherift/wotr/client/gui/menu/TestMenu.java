package com.wanderersoftherift.wotr.client.gui.menu;

import com.wanderersoftherift.wotr.init.ModMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;


public class TestMenu extends AbstractContainerMenu {

    // In MyMenu, an AbstractContainerMenu subclass
    public TestMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public TestMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(ModMenuTypes.TEST_MENU.get(), containerId);
//        this.playerInventory = playerInventory;
//        this.access = access;
//        this.createInventorySlots(playerInventory);
//        this.createGearSlot();
//        this.createSocketSlots();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
