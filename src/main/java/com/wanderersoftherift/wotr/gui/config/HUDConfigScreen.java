package com.wanderersoftherift.wotr.gui.config;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.config.ClientConfig;
import com.wanderersoftherift.wotr.init.client.ModConfigurableLayers;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

/**
 * A screen to enable configuration of HUD element positioning
 */
public class HUDConfigScreen extends Screen {

    private static final int SNAP_DIST = 10;

    private ConfigurableLayer focusedLayer = null;
    private int screenWidth = 0;
    private int screenHeight = 0;

    private double residualDragX;
    private double residualDragY;

    private final Button resetButton;
    private final Button closeButton;

    public HUDConfigScreen(Component title) {
        super(title);
        resetButton = Button
                .builder(Component.translatable(WanderersOfTheRift.translationId("button", "reset")),
                        button -> ModConfigurableLayers.CONFIGURABLE_LAYER_REGISTRY.stream()
                                .forEach(layer -> layer.getConfig().reset()))
                .size(40, 20)
                .build();
        closeButton = Button
                .builder(Component.translatable(WanderersOfTheRift.translationId("button", "close")),
                        button -> onClose())
                .size(40, 20)
                .build();
    }

    @Override
    protected void init() {
        addRenderableWidget(resetButton);
        addRenderableWidget(closeButton);
    }

    @Override
    public void onClose() {
        ClientConfig.SPEC.save();
        super.onClose();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        resetButton.setPosition(guiGraphics.guiWidth() / 2 - resetButton.getWidth() - 2,
                (guiGraphics.guiHeight() - resetButton.getHeight()) / 2);
        closeButton.setPosition(guiGraphics.guiWidth() / 2 + 2,
                (guiGraphics.guiHeight() - closeButton.getHeight()) / 2);
        renderConfigurableElements(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (isDragging()) {
            return;
        }
        findMouseOver(mouseX, mouseY)
                .ifPresent(layer -> guiGraphics.renderTooltip(minecraft.font, layer.getName(), mouseX, mouseY));
    }

    private Optional<ConfigurableLayer> findMouseOver(int mouseX, int mouseY) {
        return ModConfigurableLayers.CONFIGURABLE_LAYER_REGISTRY.stream().filter(layer -> {
            int width = layer.getConfigWidth();
            int height = layer.getConfigHeight();
            Vector2i pos = layer.getConfig().getPosition(width, height, screenWidth, screenHeight);
            return mouseX >= pos.x && mouseY >= pos.y && mouseX <= pos.x + width && mouseY <= pos.y + height;
        }).findFirst();
    }

    private void renderConfigurableElements(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        screenWidth = guiGraphics.guiWidth();
        screenHeight = guiGraphics.guiHeight();
        ModConfigurableLayers.CONFIGURABLE_LAYER_REGISTRY.stream().forEach(layer -> {
            int width = layer.getConfigWidth();
            int height = layer.getConfigHeight();
            Vector2i pos = layer.getConfig()
                    .getPosition(width, height, guiGraphics.guiWidth(), guiGraphics.guiHeight());
            boolean focused = focusedLayer == layer;
            guiGraphics.fill(RenderType.gui(), pos.x + 1, pos.y + 1, pos.x + width - 1, pos.y + height - 1,
                    focused ? 0x66FFFFFF : 0x66999999);
            guiGraphics.renderOutline(pos.x, pos.y, width, height, focused ? 0xFFFFFFFF : 0xFFBBBBBB);
        });
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        Optional<ConfigurableLayer> overLayer = findMouseOver((int) mouseX, (int) mouseY);
        if (overLayer.isPresent()) {
            focusedLayer = overLayer.get();
            setDragging(true);
            residualDragX = 0;
            residualDragY = 0;
            return true;
        }
        focusedLayer = null;
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (focusedLayer != null) {
            HudElementConfig config = focusedLayer.getConfig();
            dragX += residualDragX;
            dragY += residualDragY;
            Vector2i newRelPos = new Vector2i(config.getX() + (int) dragX, config.getY() + (int) dragY);
            snap(newRelPos);

            boolean changed = false;
            if (newRelPos.x != config.getX()) {
                residualDragX = dragX - (newRelPos.x - config.getX());
                config.setX(newRelPos.x);
                changed = true;
            } else {
                residualDragX = dragX;
            }
            if (newRelPos.y != config.getY()) {
                residualDragY = dragY - (newRelPos.y - config.getY());
                config.setY(newRelPos.y);
                changed = true;
            } else {
                residualDragY = dragY;
            }

            if (changed) {
                config.reanchor(focusedLayer.getConfigWidth(), focusedLayer.getConfigHeight(), screenWidth,
                        screenHeight);
            }
            return true;
        }
        return false;
    }

    private void snap(Vector2i newRelPos) {
        if (Math.abs(newRelPos.x) < SNAP_DIST) {
            newRelPos.x = 0;
        }
        if (Math.abs(newRelPos.y) < SNAP_DIST) {
            newRelPos.y = 0;
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (focusedLayer != null) {
            boolean unused = false;
            switch (keyCode) {
                case GLFW.GLFW_KEY_LEFT -> focusedLayer.getConfig().setX(focusedLayer.getConfig().getX() - 1);
                case GLFW.GLFW_KEY_RIGHT -> focusedLayer.getConfig().setX(focusedLayer.getConfig().getX() + 1);
                case GLFW.GLFW_KEY_UP -> focusedLayer.getConfig().setY(focusedLayer.getConfig().getY() - 1);
                case GLFW.GLFW_KEY_DOWN -> focusedLayer.getConfig().setY(focusedLayer.getConfig().getY() + 1);
                default -> unused = true;
            }
            if (!unused) {
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected void renderBlurredBackground() {
    }
}
