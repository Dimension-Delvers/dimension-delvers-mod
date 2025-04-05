package com.wanderersoftherift.wotr.init;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, WanderersOfTheRift.MODID);

    public static final DeferredHolder<Attribute, RangedAttribute> ABILITY_AOE = ATTRIBUTES.register("ability_aoe", () -> new RangedAttribute(WanderersOfTheRift.translationId("attribute", "ability.aoe"), 0, 0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, RangedAttribute> COOLDOWN = ATTRIBUTES.register("ability_cooldown", () -> new PercentageAttribute(WanderersOfTheRift.translationId("attribute", "ability.cooldown"), 0, 0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, RangedAttribute> HEAL_POWER = ATTRIBUTES.register("ability_heal_power", () -> new PercentageAttribute(WanderersOfTheRift.translationId("attribute", "ability.heal_amount"), 0, 0, Integer.MAX_VALUE));

    public static final DeferredHolder<Attribute, RangedAttribute> MAX_MANA = ATTRIBUTES.register("max_mana", () -> new RangedAttribute(WanderersOfTheRift.translationId("attribute","max_mana"), 100, 0, Integer.MAX_VALUE));

    public static final DeferredHolder<Attribute, RangedAttribute> PROJECTILE_SPREAD = ATTRIBUTES.register("projectile_spread", () -> new RangedAttribute(WanderersOfTheRift.translationId("attribute", "projectile_spread"), 0, 0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, RangedAttribute> PROJECTILE_COUNT = ATTRIBUTES.register("projectile_count", () -> new RangedAttribute(WanderersOfTheRift.translationId("attribute", "projectile_count"), 0, 0, Integer.MAX_VALUE));

}
