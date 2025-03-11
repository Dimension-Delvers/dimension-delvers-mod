package com.wanderersoftherift.wotr.item.skillgem;

import com.wanderersoftherift.wotr.abilities.AbstractAbility;
import com.wanderersoftherift.wotr.init.ModDataComponentType;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SkillGem extends Item {
    public SkillGem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        Holder<AbstractAbility> abilityHolder = stack.get(ModDataComponentType.ABILITY);
        if (abilityHolder != null) {
            return Component.translatable(abilityHolder.value().GetTranslationString());
        }
        return super.getName(stack);
    }


}
