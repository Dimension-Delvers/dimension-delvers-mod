package com.wanderersoftherift.wotr.gui.menu;

import com.wanderersoftherift.wotr.init.ModDataComponentType;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SkillSlot extends Slot {

    public SkillSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.has(ModDataComponentType.UPGRADE_POOL);
    }

}
