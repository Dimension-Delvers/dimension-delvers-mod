package com.wanderersoftherift.wotr.gui.menu;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.wanderersoftherift.wotr.mixin.InvokerAbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class QuickMover {
    public static final int PLAYER_INVENTORY_SLOTS = 3 * 9;
    public static final int PLAYER_SLOTS = PLAYER_INVENTORY_SLOTS + 9;

    private final AbstractContainerMenu menu;
    private final InvokerAbstractContainerMenu menuInvoker;
    private final List<SlotMover> slotMovers;

    private QuickMover(AbstractContainerMenu menu, List<SlotMover> slotMovers) {
        this.slotMovers = ImmutableList.copyOf(slotMovers);
        this.menu = menu;
        this.menuInvoker = (InvokerAbstractContainerMenu) menu;
    }

    public ItemStack quickMove(Player player, int slotIndex) {
        Slot slot = menu.slots.get(slotIndex);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack slotStack = slot.getItem();
        ItemStack resultStack = slotStack.copy();
        return slotMovers.stream().filter(x -> x.isFor(slotIndex)).findFirst().map(mover -> {
            boolean moved = false;
            for (MoveAction moveAction : mover.moveActions) {
                if (menuInvoker.wotr_moveItemStackTo(slotStack, moveAction.startSlot,
                        moveAction.startSlot + moveAction.count, moveAction.reverse)) {
                    moved = true;
                    break;
                }
            }
            if (moved) {
                slot.onQuickCraft(slotStack, resultStack);
                if (slotStack.isEmpty()) {
                    slot.set(ItemStack.EMPTY);
                } else {
                    slot.setChanged();
                }
                return resultStack;
            }
            return ItemStack.EMPTY;
        }).orElse(ItemStack.EMPTY);
    }

    public static QuickMover.Builder create(AbstractContainerMenu menu) {
        return new QuickMover.Builder(menu);
    }

    private static class SlotMover {
        private final int start;
        private final int count;
        private final List<MoveAction> moveActions;

        private SlotMover(int start, int count, List<MoveAction> moveActions) {
            this.start = start;
            this.count = count;
            this.moveActions = moveActions;
        }

        public boolean isFor(int slot) {
            return slot >= start && slot < start + count;
        }
    }

    public static class Builder {
        private static final int UNSET = -1;

        private int playerSlotsStart = UNSET;
        private AbstractContainerMenu menu;

        private List<SlotMover> slotMovers = new ArrayList<>();

        private Builder(AbstractContainerMenu menu) {
            this.menu = menu;
        }

        /**
         * Set the start index for the player slots (inventory + hotbar)
         * 
         * @param start
         * @return
         */
        public SlotMoverBuilder forPlayerSlots(int start) {
            Preconditions.checkState(playerSlotsStart == UNSET, "Player slots already specified.");
            playerSlotsStart = start;
            return new PlayerSlotMoverBuilder(start);
        }

        /**
         * Start setup for the given slot
         * 
         * @param slot
         * @return
         */
        public SlotMoverBuilder forSlot(int slot) {
            return new SlotMoverBuilder(slot, 1);
        }

        /**
         * Start setup for the given slot range
         * 
         * @param startSlot
         * @param count
         * @return
         */
        public SlotMoverBuilder forSlots(int startSlot, int count) {
            return new SlotMoverBuilder(startSlot, count);
        }

        /**
         * @return The finalised QuickMover
         */
        public QuickMover build() {
            return new QuickMover(menu, slotMovers);
        }

        public class SlotMoverBuilder {
            protected final int start;
            protected final int count;
            protected List<MoveAction> moveActions = new ArrayList<>();

            private SlotMoverBuilder(int start, int count) {
                this.start = start;
                this.count = count;
            }

            /**
             * Sets the current slots to try to move to the specified slot
             * 
             * @param slot
             * @return
             */
            public SlotMoverBuilder tryMoveTo(int slot) {
                return tryMoveTo(slot, 1, false);
            }

            /**
             * Sets the current slots to try to move to the specified slots
             * 
             * @param startSlot
             * @param count
             * @return
             */
            public SlotMoverBuilder tryMoveTo(int startSlot, int count) {
                return tryMoveTo(startSlot, count, false);
            }

            /**
             * Sets the current slots to try to move to the specified slots
             * 
             * @param startSlot
             * @param count
             * @param reverse
             * @return
             */
            public SlotMoverBuilder tryMoveTo(int startSlot, int count, boolean reverse) {
                moveActions.add(new MoveAction(startSlot, count, reverse));
                return this;
            }

            /**
             *
             * @return
             */
            public SlotMoverBuilder tryMoveToPlayer() {
                Preconditions.checkState(playerSlotsStart != UNSET, "Player slots must be specified first.");
                return tryMoveTo(playerSlotsStart, PLAYER_SLOTS, true);
            }

            /**
             * Set the start index for the player slots (inventory + hotbar)
             * 
             * @param start
             * @return
             */
            public SlotMoverBuilder forPlayerSlots(int start) {
                createSlotMovers();
                return Builder.this.forPlayerSlots(start);
            }

            /**
             * Start setup for the given slot
             * 
             * @param slot
             * @return
             */
            public SlotMoverBuilder forSlot(int slot) {
                createSlotMovers();
                return Builder.this.forSlot(slot);
            }

            /**
             * Start setup for the given slot range
             * 
             * @param startSlot
             * @param count
             * @return
             */
            public SlotMoverBuilder forSlots(int startSlot, int count) {
                createSlotMovers();
                return Builder.this.forSlots(startSlot, count);
            }

            /**
             * @return The finalised QuickMover
             */
            public QuickMover build() {
                createSlotMovers();
                return Builder.this.build();
            }

            protected void createSlotMovers() {
                slotMovers.add(new SlotMover(start, count, ImmutableList.copyOf(moveActions)));
            }
        }

        private class PlayerSlotMoverBuilder extends SlotMoverBuilder {

            private PlayerSlotMoverBuilder(int start) {
                super(start, PLAYER_SLOTS);
            }

            @Override
            protected void createSlotMovers() {
                // Main inventory
                List<MoveAction> mainActions = new ArrayList<>(moveActions);
                mainActions.add(new MoveAction(playerSlotsStart + PLAYER_INVENTORY_SLOTS, 9, false));
                slotMovers.add(new SlotMover(start, PLAYER_INVENTORY_SLOTS, ImmutableList.copyOf(mainActions)));
                // Hotbar inventory
                List<MoveAction> hotbarActions = new ArrayList<>(moveActions);
                hotbarActions.add(new MoveAction(playerSlotsStart, PLAYER_INVENTORY_SLOTS, false));
                slotMovers.add(new SlotMover(start + PLAYER_INVENTORY_SLOTS, 9, ImmutableList.copyOf(hotbarActions)));
            }

        }
    }

    private record MoveAction(int startSlot, int count, boolean reverse) {
    }

}
