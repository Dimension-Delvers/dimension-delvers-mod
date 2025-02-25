package com.dimensiondelvers.dimensiondelvers.gui.screen;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.gui.menu.EssenceExtractorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class EssenceExtractorScreen extends AbstractContainerScreen<EssenceExtractorMenu> {
    private static final ResourceLocation BACKGROUND = DimensionDelvers.id("textures/gui/container/essence_extractor/essence_extractor.png");
    private static final int BACKGROUND_WIDTH = 256;
    private static final int BACKGROUND_HEIGHT = 256;
    private static final int PROGRESS_WIDTH = 71;
    private static final int PROGRESS_HEIGHT = 15;
    private static final int PROGRESS_X = 50;
    private static final int PROGRESS_Y = 37;

    public EssenceExtractorScreen(EssenceExtractorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(RenderType::guiTextured, BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);

        int completion = 64;
        int progressWidth = PROGRESS_WIDTH * completion / 100;
        guiGraphics.blit(RenderType::guiTextured, BACKGROUND, this.leftPos + PROGRESS_X, this.topPos + PROGRESS_Y, 0, imageHeight, progressWidth, PROGRESS_HEIGHT, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);

    }
}
