package com.wanderersoftherift.wotr.gui.config;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.init.client.ModConfigurableLayers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Events related to configurable layers
 */
@EventBusSubscriber(modid = WanderersOfTheRift.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class ConfigurableLayerEvents {

    private static final Map<ResourceLocation, ConfigurableLayerProxy> layerProxies = new HashMap<>();
    private static boolean proxiesMapped = false;

    private ConfigurableLayerEvents() {

    }

    @SubscribeEvent
    public static void postInitPauseScreen(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof PauseScreen) {
            event.addListener(
                    Button.builder(Component.translatable(WanderersOfTheRift.translationId("screen", "configure_hud")),
                            button -> {
                                Minecraft minecraft = Minecraft.getInstance();
                                minecraft.setScreen(new HUDConfigScreen(
                                        Component.translatable(
                                                WanderersOfTheRift.translationId("screen", "configure_hud"))));
                                minecraft.mouseHandler.releaseMouse();
                            }).pos(0, 0).size(80, 20).build());
        }
    }

    @SubscribeEvent
    public static void preRenderLayers(RenderGuiLayerEvent.Pre event) {
        if (!proxiesMapped) {
            mapProxies();
        }
        ConfigurableLayerProxy proxy = layerProxies.get(event.getName());
        if (proxy != null) {
            if (proxy.getConfig().isVisible()) {
                proxy.preRender(event.getGuiGraphics());
            } else {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void postRenderLayers(RenderGuiLayerEvent.Post event) {
        ConfigurableLayerProxy proxy = layerProxies.get(event.getName());
        if (proxy != null) {
            proxy.postRender(event.getGuiGraphics());
        }
    }

    private static void mapProxies() {
        ModConfigurableLayers.CONFIGURABLE_LAYER_REGISTRY.stream()
                .filter(x -> x instanceof ConfigurableLayerProxy)
                .map(ConfigurableLayerProxy.class::cast)
                .forEach(x -> {
                    x.targetLayers().forEach(id -> layerProxies.put(id, x));
                });
        proxiesMapped = true;
    }
}
