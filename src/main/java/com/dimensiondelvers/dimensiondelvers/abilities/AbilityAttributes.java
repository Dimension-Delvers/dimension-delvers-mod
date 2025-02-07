package com.dimensiondelvers.dimensiondelvers.abilities;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.UUID;

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
    public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(Registries.ATTRIBUTE, DimensionDelvers.MODID);
    public static final DeferredHolder<Attribute, RangedAttribute> MAX_MANA = REGISTRY.register("max_mana", () -> new RangedAttribute("attribute." + DimensionDelvers.MODID + ".max_mana", 100, 0, 100));

    public static final DeferredHolder<Attribute, RangedAttribute> HEAL_EFFECTIVENESS = REGISTRY.register("heal_amount", () -> new RangedAttribute("attribute." + DimensionDelvers.MODID + ".heal_amount", 3, 0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, RangedAttribute> HEAL_COOLDOWN = REGISTRY.register("heal_cooldown", () -> new RangedAttribute("attribute." + DimensionDelvers.MODID + ".heal_cooldown", 10, 0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, RangedAttribute> BOOST_STRENGTH = REGISTRY.register("boost_strength", () -> new RangedAttribute("attribute." + DimensionDelvers.MODID + ".boost_strength", 10, 0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, RangedAttribute> BOOST_COOLDOWN = REGISTRY.register("boost_cooldown", () -> new RangedAttribute("attribute." + DimensionDelvers.MODID + ".boost_cooldown", 20, 0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, RangedAttribute> ARROW_COOLDOWN = REGISTRY.register("arrow_cooldown", () -> new RangedAttribute("attribute." + DimensionDelvers.MODID + ".arrow_cooldown", 3, 0, Integer.MAX_VALUE));
    public static final DeferredHolder<Attribute, RangedAttribute> PARTICLE_TIME = REGISTRY.register("particle_time", () -> new RangedAttribute("attribute." + DimensionDelvers.MODID + ".particle_time", 20, 0, Integer.MAX_VALUE));


    //TODO these can be stages from the level up system to apply different rates or multipliers to the different skills
    public static final AttributeModifier HEAL_MODIFIER = new AttributeModifier(DimensionDelvers.id("increase_max_heal"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    public static final AttributeModifier BOOST_COOLDOWN_MODIFIER = new AttributeModifier(DimensionDelvers.id("reduce_boost_cooldown"), -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);


}
