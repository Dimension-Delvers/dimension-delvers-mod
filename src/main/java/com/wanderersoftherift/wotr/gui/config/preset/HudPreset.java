package com.wanderersoftherift.wotr.gui.config.preset;

import com.mojang.serialization.Codec;
import com.wanderersoftherift.wotr.gui.config.ConfigurableLayer;
import com.wanderersoftherift.wotr.init.client.ModConfigurableLayers;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public record HudPreset(Map<ResourceLocation, ElementPreset> elementMap) {
    public static final Codec<HudPreset> CODEC = Codec.unboundedMap(ResourceLocation.CODEC, ElementPreset.CODEC)
            .xmap(HudPreset::new, HudPreset::elementMap);

    public static HudPreset fromConfig(RegistryAccess registryAccess) {
        Registry<ConfigurableLayer> registry = registryAccess
                .lookupOrThrow(ModConfigurableLayers.CONFIGURABLE_LAYER_KEY);
        return new HudPreset(
                registry.stream()
                        .filter(layer -> !layer.getConfig().isDefault())
                        .collect(Collectors.toMap(registry::getKey,
                                layer -> new ElementPreset(layer.getConfig().isVisible(), layer.getConfig().getAnchor(),
                                        layer.getConfig().getX(), layer.getConfig().getY(),
                                        layer.getConfig().hasOrientation()
                                                ? Optional.of(layer.getConfig().getOrientation())
                                                : Optional.empty()))));
    }
}
