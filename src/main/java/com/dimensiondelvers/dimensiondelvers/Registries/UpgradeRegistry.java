package com.dimensiondelvers.dimensiondelvers.Registries;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.abilities.AbstractAbility;
import com.dimensiondelvers.dimensiondelvers.upgrades.AbstractUpgrade;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class UpgradeRegistry {
    public static final ResourceKey<Registry<AbstractUpgrade>> UPGRADE_REGISTRY_KEY = ResourceKey.createRegistryKey(DimensionDelvers.id("upgrades"));
    public static final Registry<AbstractUpgrade> UPGRADE_REGISTRY = new RegistryBuilder<>(UPGRADE_REGISTRY_KEY).create();
    public static final DeferredRegister<AbstractUpgrade> UPGRADE_REGISTRY_DEF = DeferredRegister.create(UPGRADE_REGISTRY, DimensionDelvers.MODID);

}
