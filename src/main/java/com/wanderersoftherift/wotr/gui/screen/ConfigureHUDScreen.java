package com.wanderersoftherift.wotr.gui.screen;

import com.wanderersoftherift.wotr.config.ClientConfig;
import com.wanderersoftherift.wotr.gui.layer.ConfigurableLayer;
import com.wanderersoftherift.wotr.init.client.ModGuiLayers;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import org.joml.Vector2i;

public class ConfigureHUDScreen extends Screen {

    private ConfigurableLayer focusedLayer = null;
    private int screenWidth = 0;
    private int screenHeight = 0;

    private double residualDragX;
    private double residualDragY;

    public ConfigureHUDScreen(Component title) {
        super(title);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    @Override
    public void onClose() {
        super.onClose();
        ClientConfig.SPEC.save();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderConfigurableElements(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void renderConfigurableElements(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        screenWidth = guiGraphics.guiWidth();
        screenHeight = guiGraphics.guiHeight();
        for (ConfigurableLayer configurableLayer : ModGuiLayers.CONFIGURABLE_LAYER_LIST) {
            int width = configurableLayer.getWidthForConfiguration();
            int height = configurableLayer.getHeightForConfiguration();
            Vector2i pos = configurableLayer.getConfig().getPosition(width, height, guiGraphics.guiWidth(), guiGraphics.guiHeight());
            boolean focused = focusedLayer == configurableLayer;
            guiGraphics.fill(RenderType.gui(), pos.x + 1, pos.y + 1, pos.x + width - 1, pos.y + height - 1, focused ? 0x66FFFFFF : 0x66999999);
            guiGraphics.renderOutline(pos.x, pos.y, width, height, focused ? 0xFFFFFFFF : 0xFFBBBBBB);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (ConfigurableLayer configurableLayer : ModGuiLayers.CONFIGURABLE_LAYER_LIST) {
            int width = configurableLayer.getWidthForConfiguration();
            int height = configurableLayer.getHeightForConfiguration();
            Vector2i pos = configurableLayer.getConfig().getPosition(width, height, screenWidth, screenHeight);
            if (mouseX >= pos.x && mouseY >= pos.y && mouseX <= pos.x + width && mouseY <= pos.y + height) {
                focusedLayer = configurableLayer;
                setDragging(true);
                residualDragX = 0;
                residualDragY = 0;
                return true;
            }
        }
        focusedLayer = null;
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (focusedLayer != null) {
            dragX += residualDragX;
            dragY += residualDragY;
            focusedLayer.getConfig().setX(focusedLayer.getConfig().getX() + (int) dragX);
            focusedLayer.getConfig().setY(focusedLayer.getConfig().getY() + (int) dragY);
            residualDragX = dragX - (int) dragX;
            residualDragY = dragY - (int) dragY;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
//        if (focusedLayer != null) {
//            if (keyCode == GLFW.GLFW_KEY_LEFT) {
//                focusedLayer.getConfig().setX(focusedLayer.getConfig().getX() - 1);
//                return true;
//            }
//        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected void renderBlurredBackground() {
    }
}
