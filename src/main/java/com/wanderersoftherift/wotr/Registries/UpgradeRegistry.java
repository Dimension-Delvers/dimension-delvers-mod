package com.wanderersoftherift.wotr.Registries;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.upgrades.AbstractUpgrade;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class UpgradeRegistry {
    public static final ResourceKey<Registry<AbstractUpgrade>> UPGRADE_REGISTRY_KEY = ResourceKey.createRegistryKey(WanderersOfTheRift.id("upgrades"));
    public static final Registry<AbstractUpgrade> UPGRADE_REGISTRY = new RegistryBuilder<>(UPGRADE_REGISTRY_KEY).create();
    public static final DeferredRegister<AbstractUpgrade> UPGRADE_REGISTRY_DEF = DeferredRegister.create(UPGRADE_REGISTRY, WanderersOfTheRift.MODID);

}
