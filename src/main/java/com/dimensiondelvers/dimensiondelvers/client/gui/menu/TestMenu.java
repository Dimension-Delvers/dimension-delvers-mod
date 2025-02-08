package com.dimensiondelvers.dimensiondelvers.client.gui.menu;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.abilities.AbstractAbility;
import com.dimensiondelvers.dimensiondelvers.init.ModMenuTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

import static com.dimensiondelvers.dimensiondelvers.init.ModMenuTypes.TEST_MENU;

public class TestMenu extends AbstractContainerMenu {

    // In MyMenu, an AbstractContainerMenu subclass
    public TestMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public TestMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(TEST_MENU.get(), containerId);
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
