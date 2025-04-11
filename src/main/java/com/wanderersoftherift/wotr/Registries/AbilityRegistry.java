package com.wanderersoftherift.wotr.Registries;

import com.mojang.serialization.MapCodec;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.abilities.AbstractAbility;
import com.wanderersoftherift.wotr.abilities.StandardAbility;
import com.wanderersoftherift.wotr.abilities.effects.AbstractEffect;
import com.wanderersoftherift.wotr.abilities.effects.ApplyStatusEffect;
import com.wanderersoftherift.wotr.abilities.effects.AttachEffect;
import com.wanderersoftherift.wotr.abilities.effects.BlankEffect;
import com.wanderersoftherift.wotr.abilities.effects.BreakBlockEffect;
import com.wanderersoftherift.wotr.abilities.effects.DamageEffect;
import com.wanderersoftherift.wotr.abilities.effects.HealEffect;
import com.wanderersoftherift.wotr.abilities.effects.MovementEffect;
import com.wanderersoftherift.wotr.abilities.effects.ProjectileEffect;
import com.wanderersoftherift.wotr.abilities.effects.SimpleProjectileEffect;
import com.wanderersoftherift.wotr.abilities.effects.SoundEffect;
import com.wanderersoftherift.wotr.abilities.effects.SummonEffect;
import com.wanderersoftherift.wotr.abilities.effects.TargetEffect;
import com.wanderersoftherift.wotr.abilities.effects.TeleportEffect;
import com.wanderersoftherift.wotr.abilities.targeting.AbstractTargeting;
import com.wanderersoftherift.wotr.abilities.targeting.AreaTargeting;
import com.wanderersoftherift.wotr.abilities.targeting.RaycastTargeting;
import com.wanderersoftherift.wotr.abilities.targeting.SelfTargeting;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class AbilityRegistry {
    /*
     * This is the key for the registry that contains the actual datapacks/abilities
     */
    public static final ResourceKey<Registry<AbstractAbility>> DATA_PACK_ABILITY_REG_KEY = ResourceKey.createRegistryKey(WanderersOfTheRift.id("abilities"));

    /*
     * This sets up the registries for registering the ability types
     */
    public static final ResourceKey<Registry<MapCodec<? extends AbstractAbility>>> ABILITY_TYPES_KEY = ResourceKey.createRegistryKey(WanderersOfTheRift.id("ability_types"));

    public static final Registry<MapCodec<? extends AbstractAbility>> ABILITY_TYPES_REGISTRY = new RegistryBuilder<>(ABILITY_TYPES_KEY).create();

    public static final DeferredRegister<MapCodec<? extends AbstractAbility>> ABILITY_TYPES = DeferredRegister.create(
            ABILITY_TYPES_KEY, WanderersOfTheRift.MODID
    );

    /*
     * This is where we register the different "types" of abilities that can be created and configured using datapacks
     */
//    public static final Supplier<MapCodec<? extends AbstractAbility>> BOOST_ABILITY_TYPE = ABILITY_TYPES.register(
//            "boost", ()-> BoostAbility.CODEC);
    public static final Supplier<MapCodec<? extends AbstractAbility>> STANDARD_ABILITY_TYPE = ABILITY_TYPES.register(
            "standard_ability", () -> StandardAbility.CODEC);
//    public static final Supplier<MapCodec<? extends AbstractAbility>> PROJECTILE_ABILITY_TYPE = ABILITY_TYPES.register(
//            "projectile", ()-> ProjectileAbility.CODEC);
//    public static final Supplier<MapCodec<? extends AbstractAbility>> MODIFIER_ABILITY_TYPE = ABILITY_TYPES.register(
//            "apply_modifier", ()-> ApplyModifierAbility.CODEC);

    public static final ResourceKey<Registry<MapCodec<? extends AbstractEffect>>> EFFECTS_REG_KEY = ResourceKey.createRegistryKey(WanderersOfTheRift.id("effects"));

    public static final Registry<MapCodec<? extends AbstractEffect>> EFFECTS_REGISTRY = new RegistryBuilder<>(EFFECTS_REG_KEY).create();

    public static final DeferredRegister<MapCodec<? extends AbstractEffect>> EFFECTS = DeferredRegister.create(
            EFFECTS_REG_KEY, WanderersOfTheRift.MODID
    );

    public static final Supplier<MapCodec<? extends AbstractEffect>> TARGET_EFFECT = EFFECTS.register(
            "target_effect", () -> TargetEffect.CODEC);

    public static final Supplier<MapCodec<? extends AbstractEffect>> HEAL_EFFECT = EFFECTS.register(
            "heal_effect", () -> HealEffect.CODEC);

    public static final Supplier<MapCodec<? extends DamageEffect>> DAMAGE_EFFECT = EFFECTS.register(
            "damage_effect", () -> DamageEffect.CODEC);

    public static final Supplier<MapCodec<? extends MovementEffect>> MOVEMENT_EFFECT = EFFECTS.register(
            "movement_effect", () -> MovementEffect.CODEC);

    public static final Supplier<MapCodec<? extends TeleportEffect>> TELE_EFFECT = EFFECTS.register(
            "teleport_effect", () -> TeleportEffect.CODEC);

    public static final Supplier<MapCodec<? extends ApplyStatusEffect>> STATUS_EFFECT = EFFECTS.register(
            "status_effect", () -> ApplyStatusEffect.CODEC);

    public static final Supplier<MapCodec<? extends BreakBlockEffect>> BREAK_EFFECT = EFFECTS.register(
            "break_effect", () -> BreakBlockEffect.CODEC);

    public static final Supplier<MapCodec<? extends SummonEffect>> SUMMON_EFFECT = EFFECTS.register(
            "summon_effect", () -> SummonEffect.CODEC);

    public static final Supplier<MapCodec<? extends ProjectileEffect>> PROJECTILE_EFFECT = EFFECTS.register(
            "projectile_effect", () -> ProjectileEffect.CODEC);

    public static final Supplier<MapCodec<? extends SimpleProjectileEffect>> SIMPLE_PROJECTILE_EFFECT = EFFECTS.register(
            "simple_projectile_effect", () -> SimpleProjectileEffect.CODEC);

    public static final Supplier<MapCodec<? extends BlankEffect>> BLANK_EFFECT = EFFECTS.register(
            "blank_effect", () -> BlankEffect.CODEC);

    public static final Supplier<MapCodec<? extends SoundEffect>> SOUND_EFFECT = EFFECTS.register(
            "sound_effect", () -> SoundEffect.CODEC);

    public static final Supplier<MapCodec<? extends AttachEffect>> ATTACH_EFFECT = EFFECTS.register("attach_effect", () -> AttachEffect.CODEC);

    // EFFECT_TARGETING_TYPES Registry
    public static final ResourceKey<Registry<MapCodec<? extends AbstractTargeting>>> EFFECT_TARGETING_REG_KEY = ResourceKey.createRegistryKey(WanderersOfTheRift.id("effect_targeting"));

    public static final Registry<MapCodec<? extends AbstractTargeting>> EFFECT_TARGETING_REGISTRY = new RegistryBuilder<>(EFFECT_TARGETING_REG_KEY).create();

    public static final DeferredRegister<MapCodec<? extends AbstractTargeting>> EFFECT_TARGETING = DeferredRegister.create(
            EFFECT_TARGETING_REG_KEY, WanderersOfTheRift.MODID
    );

    public static final Supplier<MapCodec<AreaTargeting>> AREA_TARGETING = EFFECT_TARGETING.register(
            "area_targeting", () -> AreaTargeting.CODEC
    );
    public static final Supplier<MapCodec<RaycastTargeting>> RAYCAST_TARGETING = EFFECT_TARGETING.register(
            "raycast_targeting", () -> RaycastTargeting.CODEC
    );
    public static final Supplier<MapCodec<SelfTargeting>> SELF_TARGETING = EFFECT_TARGETING.register(
            "self_targeting", () -> SelfTargeting.CODEC
    );
}
