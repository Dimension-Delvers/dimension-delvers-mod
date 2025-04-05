package com.wanderersoftherift.wotr.abilities.effects;

import com.wanderersoftherift.wotr.abilities.AbilityAttributeHelper;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public record EffectContext(LivingEntity caster, ItemStack abilityItem) {

    public Level level() {
        return caster.level();
    }

    public float getAbilityAttribute(Holder<Attribute> attributeHolder, float baseValue) {
        return AbilityAttributeHelper.getAbilityAttribute(attributeHolder, baseValue, caster, abilityItem);
    }

}
