package com.wanderersoftherift.wotr.client;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.gui.layer.ConfigurableLayerProxy;
import com.wanderersoftherift.wotr.gui.screen.ConfigureHUDScreen;
import com.wanderersoftherift.wotr.init.client.ModGuiLayers;
import com.wanderersoftherift.wotr.init.client.ModKeybinds;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import org.joml.Vector2i;

/**
 * Events related to abilities - key activation detection and mana ticking.
 */
@EventBusSubscriber(modid = WanderersOfTheRift.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class ConfigureHUDEvents {

    @SubscribeEvent
    public static void processConfigureHudKey(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();

        Player player = minecraft.player;
        if (player == null) {
            return;
        }
        if (ModKeybinds.CONFIG_HUD.consumeClick()) {
            if (minecraft.screen == null) {
                // TODO: non-literal
                minecraft.pushGuiLayer(new ConfigureHUDScreen(Component.literal("Configure HUD")));
                minecraft.mouseHandler.releaseMouse();
            }
        }
    }

    @SubscribeEvent
    public static void moveLayers(RenderGuiLayerEvent.Pre event) {
        ConfigurableLayerProxy proxy = ModGuiLayers.PROXY_HOOK.get(event.getName());
        if (proxy != null) {
            Vector2i pos = proxy.getTranslation(event.getGuiGraphics().guiWidth(), event.getGuiGraphics().guiHeight());
            event.getGuiGraphics().pose().pushPose();
            event.getGuiGraphics().pose().translate(pos.x, pos.y, 0);
        }
    }

    @SubscribeEvent
    public static void moveLayers(RenderGuiLayerEvent.Post event) {
        if (ModGuiLayers.PROXY_HOOK.containsKey(event.getName())) {
            event.getGuiGraphics().pose().popPose();
        }
    }
}
