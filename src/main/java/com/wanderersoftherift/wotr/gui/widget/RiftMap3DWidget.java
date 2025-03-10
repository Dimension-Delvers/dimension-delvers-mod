package com.wanderersoftherift.wotr.gui.widget;

import com.wanderersoftherift.wotr.client.render.MapRenderer3D;
import com.wanderersoftherift.wotr.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class RiftMap3DWidget extends AbstractWidget {
    private static final double MIN_SPEED = 0.5;
    private static final double MAX_SPEED = 5.0;
    private static final float MIN_DISTANCE = 1.0f;
    private static final float MAX_DISTANCE = 50.0f;

    private float targetDistance;
    private float targetPitch;
    private float targetYaw;
    private Vector3f targetPos = new Vector3f(0.0f, 0.0f, 0.0f);

    private boolean pressingButton = false;
    private boolean justPressed = false;

    private MapRenderer3D mapRenderer;

    public RiftMap3DWidget(int x, int y, int width, int height, float renderDistance) {
        super(x, y, width, height, Component.literal("mapRenderer"));
        mapRenderer = new MapRenderer3D(x, y, width, height, renderDistance);
        targetPitch = mapRenderer.camPitch;
        targetYaw = mapRenderer.camYaw;
        targetPos = mapRenderer.camPos;
        targetDistance = mapRenderer.distance;
    }

    public void resetCam() {
        mapRenderer.resetCam();
        targetPitch = mapRenderer.camPitch;
        targetYaw = mapRenderer.camYaw;
        targetPos = mapRenderer.camPos;
        targetDistance = mapRenderer.distance;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int i, int i1, float v) {
        guiGraphics.renderOutline(this.getX() - 1, this.getY(), this.getWidth() + 1, this.getHeight(), 0xFFFFFFFF);
        guiGraphics.drawString(Minecraft.getInstance().font, Minecraft.getInstance().fpsString, this.getX(), this.getY(), 0xFFFFFFFF);

        double lerpSpeed = ClientConfig.LERP_SPEED.get();
        if (lerpSpeed > 0) {
            mapRenderer.camPitch = Mth.lerp((float)lerpSpeed * v, mapRenderer.camPitch, targetPitch);
            mapRenderer.camYaw = Mth.lerp((float)lerpSpeed * v, mapRenderer.camYaw, targetYaw);
            mapRenderer.camPos.x = Mth.lerp((float)lerpSpeed * v, mapRenderer.camPos.x, targetPos.x);
            mapRenderer.camPos.z = Mth.lerp((float)lerpSpeed * v, mapRenderer.camPos.z, targetPos.z);
            mapRenderer.distance = Mth.lerp((float)lerpSpeed * v, mapRenderer.distance, targetDistance);
        } else {
            mapRenderer.camPitch = targetPitch;
            mapRenderer.camYaw = targetYaw;
            mapRenderer.camPos = targetPos;
            mapRenderer.distance = targetDistance;
        }
        
        mapRenderer.renderMap();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (ClientConfig.MOUSE_MODE.get()) {
            if (justPressed) {
                justPressed = false;
                return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
            }
            int invertY = Minecraft.getInstance().options.invertYMouse().get() ? -1 : 1;

            if (button == 0) {
                targetPitch += (float) dragY * invertY;
                targetPitch = Math.clamp(targetPitch, -90, 90);
                targetYaw += (float) dragX;
            } else if (button == 1) {
                float yawRad = (float) Math.toRadians(targetYaw);
                float speed = (float) Mth.map(mapRenderer.distance, MIN_DISTANCE, MAX_DISTANCE, MIN_SPEED, MAX_SPEED);

                targetPos.z += (float) (-dragY * speed * Math.cos(yawRad) - dragX * -1 * speed * Math.sin(yawRad)) / 20;
                targetPos.x += (float) (-dragY * speed * Math.sin(yawRad) + dragX * -1 * speed * Math.cos(yawRad)) / 20;
            }

            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        } else {

            if (button == 0) {
                targetPitch += (float) dragY;
                targetPitch = Math.clamp(targetPitch, -90, 90);
                targetYaw += (float) dragX;
            } else if (button == 1) {
                float yawRad = (float) Math.toRadians(mapRenderer.camYaw);

                targetPos.z += (float) (-dragY * Math.cos(yawRad) - dragX * Math.sin(yawRad))/20;
                targetPos.x += (float) (-dragY * Math.sin(yawRad) + dragX * Math.cos(yawRad))/20;
            }
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (ClientConfig.MOUSE_MODE.get()) {
            if (!pressingButton) {
                GLFW.glfwSetInputMode(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                justPressed = true;
            }

            pressingButton = true;
        }
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (ClientConfig.MOUSE_MODE.get()) {
            pressingButton = false;

            GLFW.glfwSetInputMode(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double a, double b, double c , double d) {
        targetDistance -= (float) d;
        targetDistance = Math.clamp(targetDistance, MIN_DISTANCE, MAX_DISTANCE);

        return super.mouseScrolled(a, b, c, d);
    }
}
