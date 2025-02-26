package com.dimensiondelvers.dimensiondelvers.block.blockentity;

import com.dimensiondelvers.dimensiondelvers.gui.menu.EssenceExtractorMenu;
import com.dimensiondelvers.dimensiondelvers.init.ModBlockEntities;
import com.dimensiondelvers.dimensiondelvers.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class EssenceExtractorBlockEntity extends BaseContainerBlockEntity {
    public static final int INPUT_ITEM = 0;
    public static final int OUTPUT_ITEM = 1;
    public static final int NUM_ITEMS = 2;
    public static final int DATA_PROGRESS = 0;
    public static final int DATA_GOAL = 1;
    public static final int NUM_DATA_ITEMS = 2;

    private static final String TOTAL_ESSENCE = "totalEssence";

    private NonNullList<ItemStack> items = NonNullList.withSize(NUM_ITEMS, ItemStack.EMPTY);
    private int totalEssence;
    private int requiredEssence = 64;

    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case DATA_PROGRESS -> totalEssence;
                case DATA_GOAL -> requiredEssence;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case DATA_PROGRESS -> totalEssence = value;
                case DATA_GOAL -> requiredEssence = value;
            }
        }

        @Override
        public int getCount() {
            return NUM_DATA_ITEMS;
        }
    };

    public EssenceExtractorBlockEntity(BlockPos blockPos, BlockState state) {
        super(ModBlockEntities.ESSENCE_EXTRACTOR.get(), blockPos, state);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.dimensiondelvers.essence_extractor");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory) {
        return new EssenceExtractorMenu(containerId, inventory, this, dataAccess);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, EssenceExtractorBlockEntity entity) {
        boolean changed = false;
        ItemStack inputItem = entity.items.get(INPUT_ITEM);
        if (!inputItem.isEmpty()) {
            entity.totalEssence++;
            inputItem.shrink(1);
            changed = true;
        }

        while (entity.totalEssence >= entity.requiredEssence) {
            if (generateEssence(entity)) {
                entity.totalEssence -= entity.requiredEssence;
                changed = true;
            } else {
                break;
            }
        }

        if (changed) {
            setChanged(level, blockPos, blockState);
        }
    }

    private static boolean generateEssence(EssenceExtractorBlockEntity entity) {
        ItemStack outputItem = entity.items.get(OUTPUT_ITEM);
        if (outputItem.isEmpty()) {
            entity.setItem(OUTPUT_ITEM, ModItems.ESSENCE.toStack(1));
            return true;
        } else if (outputItem.getCount() < outputItem.getMaxStackSize()) {
            outputItem.grow(1);
            return true;
        }
        return false;
    }

    @Override
    protected @NotNull NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(@NotNull NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(tag, provider);
        ContainerHelper.loadAllItems(tag, this.items, provider);
        totalEssence = tag.getInt(TOTAL_ESSENCE);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(tag, provider);
        ContainerHelper.saveAllItems(tag, this.items, provider);
        tag.putInt(TOTAL_ESSENCE, totalEssence);
    }

}
