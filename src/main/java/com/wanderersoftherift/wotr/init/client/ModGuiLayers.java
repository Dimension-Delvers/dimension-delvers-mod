package com.wanderersoftherift.wotr.init.client;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.config.ClientConfig;
import com.wanderersoftherift.wotr.config.HudElementConfig;
import com.wanderersoftherift.wotr.gui.configuration.ScreenAnchor;
import com.wanderersoftherift.wotr.gui.layer.AbilityBar;
import com.wanderersoftherift.wotr.gui.layer.ConfigurableLayer;
import com.wanderersoftherift.wotr.gui.layer.ConfigurableLayerProxy;
import com.wanderersoftherift.wotr.gui.layer.EffectBar;
import com.wanderersoftherift.wotr.gui.layer.ManaBar;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = WanderersOfTheRift.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModGuiLayers {
    public static final ResourceLocation ABILITY_BAR = WanderersOfTheRift.id("ability_bar");
    public static final ResourceLocation EFFECT_BAR = WanderersOfTheRift.id("effect_bar");
    public static final ResourceLocation MANA_BAR = WanderersOfTheRift.id("mana_bar");

    public static final List<ConfigurableLayer> CONFIGURABLE_LAYER_LIST = new ArrayList<>();

    public static final Map<ResourceLocation, ConfigurableLayerProxy> PROXY_HOOK = new LinkedHashMap<>();

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        registerAbove(event, VanillaGuiLayers.HOTBAR, ABILITY_BAR, new AbilityBar());
        registerAbove(event, ABILITY_BAR, MANA_BAR, new ManaBar());
        registerAbove(event, MANA_BAR, EFFECT_BAR, new EffectBar());
        addConfigurationFor(VanillaGuiLayers.HOTBAR, ClientConfig.HOT_BAR, ScreenAnchor.BOTTOM_CENTER, 0, 0, 182, 22);

    }

    private static void addConfigurationFor(
            ResourceLocation id,
            HudElementConfig config,
            ScreenAnchor anchor,
            int x,
            int y,
            int width,
            int height) {
        ConfigurableLayerProxy proxy = new ConfigurableLayerProxy(
                Component.translatable("hud." + id.getNamespace() + "." + id.getPath()), config, anchor, x, y, width,
                height);
        CONFIGURABLE_LAYER_LIST.add(proxy);
        PROXY_HOOK.put(id, proxy);
    }

    private static void registerAbove(
            RegisterGuiLayersEvent event,
            ResourceLocation aboveId,
            ResourceLocation id,
            LayeredDraw.Layer layer) {
        if (layer instanceof ConfigurableLayer configurableLayer) {
            CONFIGURABLE_LAYER_LIST.add(configurableLayer);
        }
        event.registerAbove(aboveId, id, layer);
    }

}
