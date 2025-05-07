package com.wanderersoftherift.wotr.init.client;

import com.google.common.collect.ImmutableList;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.config.ClientConfig;
import com.wanderersoftherift.wotr.gui.config.ConfigurableLayer;
import com.wanderersoftherift.wotr.gui.config.ConfigurableLayerProxy;
import com.wanderersoftherift.wotr.gui.config.HudElementConfig;
import com.wanderersoftherift.wotr.gui.layer.AbilityBar;
import com.wanderersoftherift.wotr.gui.layer.EffectBar;
import com.wanderersoftherift.wotr.gui.layer.ManaBar;
import com.wanderersoftherift.wotr.gui.layer.objective.ObjectiveLayer;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.List;
import java.util.function.Supplier;

public class ModConfigurableLayers {

    // Registry and key for Abstract Modifiers
    public static final ResourceKey<Registry<ConfigurableLayer>> CONFIGURABLE_LAYER_KEY = ResourceKey
            .createRegistryKey(WanderersOfTheRift.id("configurable_layer"));

    public static final Registry<ConfigurableLayer> CONFIGURABLE_LAYER_REGISTRY = new RegistryBuilder<>(
            CONFIGURABLE_LAYER_KEY).create();

    // Register our layers
    public static final DeferredRegister<ConfigurableLayer> LAYERS = DeferredRegister.create(CONFIGURABLE_LAYER_KEY,
            WanderersOfTheRift.MODID);

    public static final Supplier<ConfigurableLayer> ABILITY_BAR = LAYERS.register("ability_bar", AbilityBar::new);
    public static final Supplier<ConfigurableLayer> MANA_BAR = LAYERS.register("mana_bar", ManaBar::new);
    public static final Supplier<ConfigurableLayer> EFFECT_BAR = LAYERS.register("effect_bar", EffectBar::new);
    public static final Supplier<ConfigurableLayer> OBJECTIVE = LAYERS.register("objective", ObjectiveLayer::new);

    // Register vanilla layers
    public static final DeferredRegister<ConfigurableLayer> VANILLA_LAYERS = DeferredRegister
            .create(CONFIGURABLE_LAYER_KEY, "minecraft");

    public static final Supplier<ConfigurableLayerProxy> VANILLA_HOT_BAR = VANILLA_LAYERS.register(
            VanillaGuiLayers.HOTBAR.getPath(), () -> newProxy(VanillaGuiLayers.HOTBAR, ClientConfig.HOT_BAR, 182, 22));

    public static final Supplier<ConfigurableLayerProxy> VANILLA_XP_BAR = VANILLA_LAYERS.register(
            VanillaGuiLayers.EXPERIENCE_BAR.getPath(),
            () -> newProxy(VanillaGuiLayers.EXPERIENCE_BAR, ClientConfig.EXPERIENCE_BAR, 182, 5));

    public static final Supplier<ConfigurableLayerProxy> VANILLA_XP_LEVEL = VANILLA_LAYERS.register(
            VanillaGuiLayers.EXPERIENCE_LEVEL.getPath(),
            () -> newProxy(VanillaGuiLayers.EXPERIENCE_LEVEL, ClientConfig.EXPERIENCE_LEVEL, 10, 10));

    public static final Supplier<ConfigurableLayerProxy> VANILLA_HEALTH_AND_ARMOR = VANILLA_LAYERS
            .register(
                    VanillaGuiLayers.PLAYER_HEALTH.getPath(),
                    () -> newProxy("hud.minecraft.health_armor", ClientConfig.HEALTH_ARMOR, 81, 29,
                            VanillaGuiLayers.PLAYER_HEALTH, VanillaGuiLayers.ARMOR_LEVEL));

    public static final Supplier<ConfigurableLayerProxy> VANILLA_FOOD = VANILLA_LAYERS
            .register(
                    VanillaGuiLayers.FOOD_LEVEL.getPath(),
                    () -> newProxy(VanillaGuiLayers.FOOD_LEVEL, ClientConfig.FOOD_LEVEL, 81, 9));

    public static final Supplier<ConfigurableLayerProxy> VANILLA_AIR = VANILLA_LAYERS
            .register(
                    VanillaGuiLayers.AIR_LEVEL.getPath(),
                    () -> newProxy(VanillaGuiLayers.AIR_LEVEL, ClientConfig.AIR_LEVEL, 81, 9));

    public static final Supplier<ConfigurableLayerProxy> VANILLA_EFFECTS = VANILLA_LAYERS
            .register(
                    VanillaGuiLayers.EFFECTS.getPath(),
                    () -> newProxy(VanillaGuiLayers.EFFECTS, ClientConfig.VANILLA_EFFECTS, 375, 50));

    /**
     * Creates a proxy configuration layer for an existing layer. You will need a {@link HudElementConfig} to link it
     * to, that should default to the standard positioning of the layer
     *
     * @param id     The id of the layer
     * @param config The config to link it to
     * @param width  The width of the layer
     * @param height The height of the layer
     * @return A proxy configurable layer
     */
    private static ConfigurableLayerProxy newProxy(
            ResourceLocation id,
            HudElementConfig config,
            int width,
            int height) {
        return new ConfigurableLayerProxy(
                Component.translatable("hud." + id.getNamespace() + "." + id.getPath()), config, width, height,
                List.of(id));
    }

    /**
     * Creates a proxy configuration layer for an existing layer. You will need a {@link HudElementConfig} to link it
     * to, that should default to the standard positioning of the layer
     *
     * @param name   The id of the layer
     * @param config The config to link it to
     * @param width  The width of the layer
     * @param height The height of the layer
     * @param id     The first id of a layer to proxy
     * @param ids    Any additional ids to proxy
     * @return A proxy configurable layer
     */
    private static ConfigurableLayerProxy newProxy(
            String name,
            HudElementConfig config,
            int width,
            int height,
            ResourceLocation id,
            ResourceLocation... ids) {
        return new ConfigurableLayerProxy(
                Component.translatable(name), config, width, height,
                ImmutableList.<ResourceLocation>builder().add(id).add(ids).build());
    }

}
