package com.wanderersoftherift.wotr.client.gui.screen;

import com.wanderersoftherift.wotr.Registries.UpgradeRegistry;
import com.wanderersoftherift.wotr.client.gui.menu.TestMenu;
import com.wanderersoftherift.wotr.networking.data.ClaimUpgrade;
import com.wanderersoftherift.wotr.upgrades.AbstractUpgrade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class AbilityScreen extends AbstractContainerScreen<TestMenu>{
    private Inventory inventory;
    public AbilityScreen(TestMenu menu, Inventory playerInv, Component title)
    {
        super(menu, playerInv, title);
        this.inventory = playerInv;
        this.titleLabelX = 2;
        this.titleLabelY = 2;
//        this.inventoryLabelX = 10;
    }

    @Override
    protected void init() {
        super.init();


        int width = 72;
        int height = 24;
        int x = 0;
        int y = height;
        for(AbstractUpgrade upgrade: UpgradeRegistry.UPGRADE_REGISTRY.stream().toList())
        {
            x += width + 1;
            if(x + width > Minecraft.getInstance().screen.width) {
                x = width + 1;
                y += height + 1;
            }

            this.addRenderableWidget(Button.builder(Component.translatable(upgrade.getTranslationString()), new Button.OnPress(){
                @Override
                public void onPress(Button button) {
                    PacketDistributor.sendToServer(new ClaimUpgrade(upgrade.GetName().toString()));
                }

            }).size(width,height).pos(x,y).bounds(x,y,width,height).tooltip(Tooltip.create(Component.translatable("tooltip."+upgrade.getTranslationString()))).build());
        }

    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Background is typically rendered first
        this.renderBackground(graphics, mouseX, mouseY, partialTick);

        // Render things here before widgets (background textures)

        // Then the widgets if this is a direct child of the Screen
        super.render(graphics, mouseX, mouseY, partialTick);

        // Render things after widgets (tooltips)
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {}

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {

    }

    @Override
    public void onClose() {
        // Stop any handlers here

        // Call last in case it interferes with the override
        super.onClose();
    }

    @Override
    public void removed() {
        // Reset initial states here

        // Call last in case it interferes with the override
        super.removed()
        ;}

}
