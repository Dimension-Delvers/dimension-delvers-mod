package com.wanderersoftherift.wotr.init;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.entity.projectile.SimpleEffectProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {

    public static final DeferredRegister.Entities ENTITIES = DeferredRegister.createEntities(WanderersOfTheRift.MODID);

    public static DeferredHolder<EntityType<?>, EntityType<SimpleEffectProjectile>> SIMPLE_EFFECT_PROJECTILE = ENTITIES.registerEntityType("simple_effect_projectile",
            SimpleEffectProjectile::new,
            MobCategory.MISC);

}
