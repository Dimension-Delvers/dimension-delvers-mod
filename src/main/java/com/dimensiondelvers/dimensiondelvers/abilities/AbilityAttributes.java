package com.dimensiondelvers.dimensiondelvers.abilities;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AbilityAttributes {

    /*
     * ------THINGS HERE------
     * CDR
     * PER SKILL COOLDOWNS (that way each cooldown amount can differ per person and be modified)
     * MAX MANA
     * Pretty much any stat we want to modify on the player
     *
     * ----THINGS NOT HERE!----
     * Things that change often, and not controlled a ton such as actual cooldown tracking, active mana amount, etc
     */

    //TODO Replace these with our custom modifier system
    public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(Registries.ATTRIBUTE, DimensionDelvers.MODID);
    public static final DeferredHolder<Attribute, RangedAttribute> MAX_MANA = REGISTRY.register("max_mana", () -> new RangedAttribute("attribute." + DimensionDelvers.MODID + ".max_mana", 100, 0, 100));

    public static final DeferredHolder<Attribute, RangedAttribute> HEAL_EFFECTIVENESS = REGISTRY.register("heal_amount", () -> new RangedAttribute("attribute." + DimensionDelvers.MODID + ".heal_amount", 3, 0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, RangedAttribute> HEAL_COOLDOWN = REGISTRY.register("heal_cooldown", () -> new RangedAttribute("attribute." + DimensionDelvers.MODID + ".heal_cooldown", 10, 0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, RangedAttribute> BOOST_STRENGTH = REGISTRY.register("small_boost_strength", () -> new RangedAttribute("attribute." + DimensionDelvers.MODID + ".small_boost_strength", 2, 0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, RangedAttribute> LARGE_BOOST_STRENGTH = REGISTRY.register("large_boost_strength", () -> new RangedAttribute("attribute." + DimensionDelvers.MODID + ".large_boost_strength", 20, 0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, RangedAttribute> BOOST_COOLDOWN = REGISTRY.register("boost_cooldown", () -> new RangedAttribute("attribute." + DimensionDelvers.MODID + ".boost_cooldown", 5, 0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, RangedAttribute> SMOL_COOLDOWN = REGISTRY.register("smol_cooldown", () -> new RangedAttribute("attribute." + DimensionDelvers.MODID + ".smol_cooldown", 10, 0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, RangedAttribute> ARROW_COOLDOWN = REGISTRY.register("arrow_cooldown", () -> new RangedAttribute("attribute." + DimensionDelvers.MODID + ".arrow_cooldown", 3, 0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, RangedAttribute> PARTICLE_TIME = REGISTRY.register("particle_time", () -> new RangedAttribute("attribute." + DimensionDelvers.MODID + ".particle_time", 20, 0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, RangedAttribute> SMOL_TIME = REGISTRY.register("smol_time", () -> new RangedAttribute("attribute." + DimensionDelvers.MODID + ".smol_time", 5, 0, Integer.MAX_VALUE));


    //TODO these can be stages from the level up system to apply different rates or multipliers to the different skills
    public static final AttributeModifier HEAL_MODIFIER = new AttributeModifier(DimensionDelvers.id("increase_max_heal"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    public static final AttributeModifier BOOST_COOLDOWN_MODIFIER = new AttributeModifier(DimensionDelvers.id("reduce_boost_cooldown"), -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    public static final AttributeModifier SMOL_MODIFIER = new AttributeModifier(DimensionDelvers.id("make_smol"), -0.75, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);


}
