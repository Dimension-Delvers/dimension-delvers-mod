package com.wanderersoftherift.wotr.gui.menu;

import com.wanderersoftherift.wotr.init.ModBlocks;
import com.wanderersoftherift.wotr.init.ModDataComponentType;
import com.wanderersoftherift.wotr.init.ModMenuTypes;
import com.wanderersoftherift.wotr.item.skillgem.AbilitySlots;
import com.wanderersoftherift.wotr.item.skillgem.Upgrade;
import com.wanderersoftherift.wotr.item.skillgem.UpgradePool;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The skill bench allows viewing and managing a skill and its upgrades
 */
public class SkillBenchMenu extends AbstractContainerMenu {
    private static final int INPUT_SLOTS = 1;
    private final static int PLAYER_INVENTORY_SLOTS = 3 * 9;
    private final static int PLAYER_SLOTS = PLAYER_INVENTORY_SLOTS + 9;

    private final ContainerLevelAccess access;
    private final Container inputContainer;

    public SkillBenchMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL, new ItemStackHandler(AbilitySlots.ABILITY_BAR_SIZE));
    }

    public SkillBenchMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access, IItemHandler abilities) {
        super(ModMenuTypes.SKILL_BENCH_MENU.get(), containerId);
        this.access = access;
        this.inputContainer = new SimpleContainer(1);
        addSlot(new SkillSlot(inputContainer, 0, 32, 17));

        addStandardInventorySlots(playerInventory, 32, 154);
        addPlayerSkillSlots(abilities, 4, 46);

    }

    protected void addPlayerSkillSlots(IItemHandler abilitySlots, int x, int y) {
        for (int i = 0; i < abilitySlots.getSlots(); i++) {
            addSlot(new SlotItemHandler(abilitySlots, i, x, y + i * 18));
        }
    }

    public boolean isSkillItemPresent() {
        ItemStack item = inputContainer.getItem(0);
        return !item.isEmpty() && item.has(ModDataComponentType.UPGRADE_POOL);
    }

    public ItemStack getSkillItem() {
        return inputContainer.getItem(0);
    }

    public @Nullable UpgradePool getUpgradePool() {
        ItemStack item = inputContainer.getItem(0);
        if (!item.isEmpty() && item.has(ModDataComponentType.UPGRADE_POOL)) {
            return item.get(ModDataComponentType.UPGRADE_POOL);
        }
        return null;
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.access.execute((world, pos) -> this.clearContainer(player, inputContainer));
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            ItemStack originalStack = slotStack.copy();
            if (slot instanceof SkillSlot) {
                if (!this.moveItemStackTo(slotStack, INPUT_SLOTS, INPUT_SLOTS + PLAYER_SLOTS, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, originalStack);
            } else {
                if (!this.moveItemStackTo(slotStack, 0, INPUT_SLOTS, false)) {
                    // Move from player inventory to hotbar
                    if (index < INPUT_SLOTS + PLAYER_INVENTORY_SLOTS) {
                        if (!this.moveItemStackTo(slotStack, INPUT_SLOTS + PLAYER_INVENTORY_SLOTS, INPUT_SLOTS + PLAYER_SLOTS, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                    // Move from hotbar to player inventory
                    else if (!this.moveItemStackTo(slotStack, INPUT_SLOTS, INPUT_SLOTS + PLAYER_INVENTORY_SLOTS, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.access, player, ModBlocks.SKILL_BENCH.get());
    }

    public void selectSkill(int choice, int selection) {
        if (isSkillItemPresent()) {
            UpgradePool pool = getUpgradePool();
            if (choice < 0 || choice >= pool.getChoiceCount()) {
                return;
            }
            List<Holder<Upgrade>> options = pool.getChoice(choice);
            if (selection < 0 || selection >= options.size()) {
                return;
            }

            UpgradePool.Mutable mutable = pool.getMutable();
            mutable.selectChoice(choice, selection);
            DataComponentPatch patch = DataComponentPatch.builder().set(ModDataComponentType.UPGRADE_POOL.get(), mutable.toImmutable()).build();
            getSkillItem().applyComponents(patch);
        }
    }

}
