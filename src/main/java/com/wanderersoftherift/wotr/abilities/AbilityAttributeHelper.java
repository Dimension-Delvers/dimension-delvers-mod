package com.wanderersoftherift.wotr.abilities;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.init.ModDataComponentType;
import com.wanderersoftherift.wotr.item.skillgem.UpgradePool;
import com.wanderersoftherift.wotr.modifier.ModifierHelper;
import com.wanderersoftherift.wotr.modifier.source.AbilityUpgradeModifierSource;
import com.wanderersoftherift.wotr.modifier.source.ModifierSource;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

public class AbilityAttributeHelper {
    public static float getAbilityAttribute(Holder<Attribute> attributeHolder, float baseValue, LivingEntity caster, ItemStack abilityItem) {
        AttributeInstance attribute = caster.getAttribute(attributeHolder);
        if (attribute == null) {
            return 0;
        }

        AttributeModifier baseModifier = new AttributeModifier(WanderersOfTheRift.id("base_value"), baseValue, AttributeModifier.Operation.ADD_VALUE);

        UpgradePool pool = null;
        if (!abilityItem.isEmpty() && abilityItem.has(ModDataComponentType.UPGRADE_POOL)) {
            pool = abilityItem.get(ModDataComponentType.UPGRADE_POOL);
        }

        attribute.addTransientModifier(baseModifier);
        ModifierHelper.enableModifier(caster);
        if (pool != null) {
            pool.forEachSelected((upgrade, selection) -> {
                ModifierSource source = new AbilityUpgradeModifierSource(selection);
                upgrade.modifierEffects().forEach(effect -> effect.enableModifier(0, caster, source));
            });
        }

        float value = (float) attribute.getValue();

        attribute.removeModifier(baseModifier);
        ModifierHelper.disableModifier(caster);
        if (pool != null) {
            pool.forEachSelected((upgrade, selection) -> {
                ModifierSource source = new AbilityUpgradeModifierSource(selection);
                upgrade.modifierEffects().forEach(effect -> effect.disableModifier(0, caster, source));
            });
        }
        return value;
    }
}
