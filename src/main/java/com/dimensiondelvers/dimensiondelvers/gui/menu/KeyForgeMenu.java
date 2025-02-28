package com.dimensiondelvers.dimensiondelvers.gui.menu;

import com.dimensiondelvers.dimensiondelvers.init.ModBlocks;
import com.dimensiondelvers.dimensiondelvers.init.ModDataComponentType;
import com.dimensiondelvers.dimensiondelvers.init.ModItems;
import com.dimensiondelvers.dimensiondelvers.init.ModMenuTypes;
import com.dimensiondelvers.dimensiondelvers.item.essence.EssenceType;
import com.dimensiondelvers.dimensiondelvers.item.essence.EssenceTypeLookup;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Menu for the Key Forge.
 * This menu totals the essence value of inputs and uses it to produce a key. The total essence determines the tier,
 * the essence type distribution determines the theme.
 */
public class KeyForgeMenu extends AbstractContainerMenu {
    public final static int NUM_SLOTS = 4;
    private final static int OUTPUT_SLOTS = 1;
    private final static int INPUT_SLOTS_X = 31;
    private final static int INPUT_SLOTS_Y = 33;
    private final static int INPUT_SLOT_X_OFFSET = 25;
    private final static int INPUT_SLOT_Y_OFFSET = 25;
    private static final List<Integer> TIER_COSTS = ImmutableList.of(20, 60, 180, 540, 1620);

    private final ContainerLevelAccess access;
    private final Container inputContainer;
    private final ResultContainer resultContainer;
    private final DataSlot tierPercent;

    public KeyForgeMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public KeyForgeMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(ModMenuTypes.KEY_FORGE_MENU.get(), containerId);
        this.access = access;
        this.tierPercent = DataSlot.standalone();
        this.inputContainer = new SimpleContainer(5) {
            @Override
            public void setChanged() {
                super.setChanged();
                KeyForgeMenu.this.slotsChanged(this);
            }
        };
        this.resultContainer = new ResultContainer();
        for (int slotY = 0; slotY < 2; slotY++) {
            for (int slotX = 0; slotX < 2; slotX++) {
                addSlot(new Slot(inputContainer, slotY * 2 + slotX, INPUT_SLOTS_X + INPUT_SLOT_X_OFFSET * slotX, INPUT_SLOTS_Y + INPUT_SLOT_Y_OFFSET * slotY));
            }
        }
        addSlot(new KeyOutputSlot(resultContainer, 4, 148, 78, inputContainer));

        addStandardInventorySlots(playerInventory, 8, 114);

        addDataSlot(tierPercent);
    }

    public int getTierPercent() {
        return tierPercent.get();
    }

    @Override
    public void slotsChanged(@NotNull Container container) {
        this.access.execute((level, pos) -> {
            if (level instanceof ServerLevel) {
                updateResults();
            }
        });
    }

    private void updateResults() {
        int totalEssence = 0;
        for (int i = 0; i < inputContainer.getContainerSize(); i++) {
            ItemStack input = inputContainer.getItem(i);
            for (Map.Entry<EssenceType, Integer> essenceValue : EssenceTypeLookup.getEssencesFor(input.getItem()).entrySet()) {
                totalEssence += essenceValue.getValue() * input.getCount();
            }
        }
        updateTier(totalEssence);
        updateOutput();
    }

    private void updateOutput() {
        int tier = tierPercent.get() / 100;
        if (tier == 0 && !resultContainer.isEmpty()) {
            resultContainer.clearContent();
        } else if (tier > 0 && resultContainer.isEmpty() || resultContainer.getItem(0).getOrDefault(ModDataComponentType.RIFT_TIER.get(), 0) != tier) {
            ItemStack output = ModItems.RIFT_KEY.toStack();
            output.applyComponents(DataComponentPatch.builder().set(ModDataComponentType.RIFT_TIER.get(), tier).build());
            resultContainer.setItem(0, output);
        }
    }

    private void updateTier(int totalEssence) {
        int remainingEssence = totalEssence;
        int result = 0;
        for (int i = 0; i < TIER_COSTS.size() && remainingEssence > 0; i++) {
            if (remainingEssence >= TIER_COSTS.get(i)) {
                result += 100;
                remainingEssence -= TIER_COSTS.get(i);
            } else {
                result += 100 * remainingEssence / TIER_COSTS.get(i);
                remainingEssence = 0;
            }
        }
        tierPercent.set(result);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.access, player, ModBlocks.KEY_FORGE.get());
    }

    @Override
    public void removed(@NotNull Player player) {
        this.access.execute((world, pos) -> this.clearContainer(player, inputContainer));
    }
}
