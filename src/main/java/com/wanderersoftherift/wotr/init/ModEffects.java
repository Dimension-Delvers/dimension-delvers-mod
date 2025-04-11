package com.wanderersoftherift.wotr.init;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
//import com.wotr.wotr.effects.AttributeEffect;
import com.wanderersoftherift.wotr.effects.FireBurnEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, WanderersOfTheRift.MODID);

    public static final DeferredHolder<MobEffect, FireBurnEffect> FIRE_BURN_EFFECT = EFFECTS.register("fire_burn", FireBurnEffect::new);
}
