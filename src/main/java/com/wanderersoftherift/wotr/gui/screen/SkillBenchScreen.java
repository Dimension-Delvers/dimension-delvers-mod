package com.wanderersoftherift.wotr.gui.screen;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.gui.menu.SkillBenchMenu;
import com.wanderersoftherift.wotr.gui.widget.ScrollContainerEntry;
import com.wanderersoftherift.wotr.gui.widget.ScrollContainerWidget;
import com.wanderersoftherift.wotr.item.skillgem.Upgrade;
import com.wanderersoftherift.wotr.item.skillgem.UpgradePool;
import com.wanderersoftherift.wotr.network.SelectSkillUpgradePayload;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * A UI for viewing skill details and manage their upgrades
 */
public class SkillBenchScreen extends AbstractContainerScreen<SkillBenchMenu> {
    private static final ResourceLocation BACKGROUND = WanderersOfTheRift.id("textures/gui/container/skill_bench/background.png");

    private static final int BACKGROUND_WIDTH = 320;
    private static final int BACKGROUND_HEIGHT = 236;
    private static final int UPGRADE_LABEL_X = 202;
    private static final int UPGRADE_LABEL_Y = 20;
    private static final int SELECTION_AREA_X = 202;
    private static final int SELECTION_AREA_Y = 30;
    private static final int SELECTION_AREA_WIDTH = 106;
    private static final int SELECTION_AREA_HEIGHT = 196;
    private static final int UPGRADE_WIDTH = 20;
    private static final int UPGRADE_SPACING = 8;
    private static final int BORDER_X = 24;
    public static final int ICON_SIZE = 16;

    private final Component upgradeLabel = Component.translatable("skill_bench." + WanderersOfTheRift.MODID + ".screen.upgrade");
    private ScrollContainerWidget<ChoiceBar> skillChoices;

    public SkillBenchScreen(SkillBenchMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 320;
        this.imageHeight = 236;
        this.inventoryLabelY = this.imageHeight - 94;
        this.inventoryLabelX += BORDER_X;
        this.titleLabelX += BORDER_X;
    }

    @Override
    protected void init() {
        super.init();
        skillChoices = new ScrollContainerWidget<>(leftPos + SELECTION_AREA_X, topPos + SELECTION_AREA_Y, SELECTION_AREA_WIDTH, SELECTION_AREA_HEIGHT);
        addRenderableWidget(skillChoices);
        skillChoices.setFocused(true);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double xOffset, double yOffset) {
        if (!super.mouseScrolled(mouseX, mouseY, xOffset, yOffset)) {
            if (skillChoices.isHovered()) {
                return skillChoices.mouseScrolled(mouseX, mouseY, xOffset, yOffset);
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        skillChoices.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!super.keyPressed(keyCode, scanCode, modifiers)) {
            if (skillChoices.isHoveredOrFocused()) {
                return skillChoices.keyPressed(keyCode, scanCode, modifiers);
            }
            return false;
        }
        return true;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        updateChoices();

        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderSelection(guiGraphics, partialTick, mouseX, mouseY);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void updateChoices() {
        UpgradePool pool = menu.getUpgradePool();
        if (pool != null) {
            int count = pool.getChoiceCount();
            while (skillChoices.children().size() > count) {
                skillChoices.children().removeLast();
            }
            while (skillChoices.children().size() < count) {
                skillChoices.children().add(new ChoiceBar(0,0, SELECTION_AREA_WIDTH - ScrollContainerWidget.SCROLLBAR_SPACE, skillChoices.children().size(), menu::getUpgradePool));
            }
        } else {
            skillChoices.children().clear();
        }
    }

    private void renderSelection(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        if (menu.isSkillItemPresent()) {
            ItemStack skillItem = menu.getSkillItem();
            guiGraphics.drawString(font, skillItem.getDisplayName(), this.leftPos + 52, this.topPos + 21, ChatFormatting.BLACK.getColor(), true);
            guiGraphics.drawString(this.font, this.upgradeLabel, leftPos + UPGRADE_LABEL_X, topPos + UPGRADE_LABEL_Y, ChatFormatting.DARK_GRAY.getColor(), false);
        }
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(RenderType::guiTextured, BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
    }

    /**
     * A bar of N choices
     */
    private static class ChoiceBar extends AbstractContainerWidget implements ScrollContainerEntry {
        private static final ResourceLocation CHOICE_BAR = WanderersOfTheRift.id("textures/gui/container/skill_bench/choice_bar.png");
        private static final int BORDER_Y = 4;

        private final Supplier<UpgradePool> upgradePool;
        private final int choice;
        private final List<ChoiceButton> options = new ArrayList<>();

        ChoiceBar(int x, int y, int width, int choice, Supplier<UpgradePool> upgradePool) {
            super(x, y, width, 28, Component.empty());
            this.choice = choice;
            this.upgradePool = upgradePool;

            UpgradePool pool = upgradePool.get();
            int choices = pool.getChoice(choice).size();
            int offset = (width - choices * UPGRADE_WIDTH - (choices - 1) * UPGRADE_SPACING) / 2;

            for (int i = 0; i < choices; i++) {
                ChoiceButton button = new ChoiceButton(x + offset + i * (UPGRADE_WIDTH + UPGRADE_SPACING), y + BORDER_Y, choice, i, upgradePool);
                options.add(button);
            }
        }

        @Override
        protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            UpgradePool pool = upgradePool.get();
            while (pool.getChoice(choice).size() > options.size()) {
                options.add(new ChoiceButton(0,0, choice, options.size(), upgradePool));
            }
            while (pool.getChoice(choice).size() < options.size()) {
                options.removeLast();
            }
            guiGraphics.blit(RenderType::guiTextured, CHOICE_BAR, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);
            int offset = (width - options.size() * UPGRADE_WIDTH - (options.size() - 1) * UPGRADE_SPACING) / 2;
            for (int i = 0; i < options.size(); i++) {
                ChoiceButton option = options.get(i);
                option.setX(getX() + offset + i * (UPGRADE_WIDTH + UPGRADE_SPACING));
                option.setY(getY() + 4);
                option.render(guiGraphics, mouseX, mouseY, partialTick);
            }
        }

        @Override
        protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }

        @Override
        protected int contentHeight() {
            return height;
        }

        @Override
        protected double scrollRate() {
            return 0;
        }

        @Override
        public @NotNull List<? extends GuiEventListener> children() {
            return options;
        }

        @Override
        public int getHeight(int width) {
            return height;
        }
    }

    /**
     * Button for selecting a choice option
     */
    private static class ChoiceButton extends AbstractButton {
        private static final ResourceLocation CHOICE_BUTTON = WanderersOfTheRift.id("textures/gui/container/skill_bench/choice_button.png");
        private static final ResourceLocation SELECTED_CHOICE_BUTTON = WanderersOfTheRift.id("textures/gui/container/skill_bench/selected_choice_button.png");
        private static final ResourceLocation HOVERED_CHOICE_BUTTON = WanderersOfTheRift.id("textures/gui/container/skill_bench/hovered_choice_button.png");

        private final int choice;
        private final int selection;
        private final Supplier<UpgradePool> upgradePool;

        ChoiceButton(int x, int y, int choice, int selection, Supplier<UpgradePool> upgradePool) {
            super(x, y, 20, 20, Component.empty());
            this.choice = choice;
            this.selection = selection;
            this.upgradePool = upgradePool;
        }

        @Override
        public void onPress() {
            UpgradePool pool = upgradePool.get();
            if (pool != null && pool.getSelectedIndex(choice) != selection) {
                PacketDistributor.sendToServer(new SelectSkillUpgradePayload(choice, selection));
            }
        }

        @Override
        public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            ResourceLocation resourcelocation;
            if (!isActive()) {
                return;
            } else if (isSelected()) {
                resourcelocation = SELECTED_CHOICE_BUTTON;
            } else if (this.isHoveredOrFocused()) {
                resourcelocation = HOVERED_CHOICE_BUTTON;
            } else {
                resourcelocation = CHOICE_BUTTON;
            }

            graphics.blit(RenderType::guiTextured, resourcelocation, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);
            renderIcon(graphics);
            updateTooltip();
        }

        private void updateTooltip() {
            UpgradePool pool = upgradePool.get();
            Upgrade upgrade = pool.getChoice(choice).get(selection).value();
            MutableComponent tooltipComp = Component.empty().append(upgrade.name()).append("\n").append(upgrade.description());
            this.setTooltip(Tooltip.create(tooltipComp));
        }

        private void renderIcon(GuiGraphics guiGraphics) {
            UpgradePool pool = upgradePool.get();
            Upgrade upgrade = pool.getChoice(choice).get(selection).value();
            guiGraphics.blit(RenderType::guiTextured, upgrade.icon(), this.getX() + (width - ICON_SIZE) / 2, this.getY() + (height - ICON_SIZE) / 2, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
        }

        public boolean isSelected() {
            UpgradePool pool = upgradePool.get();
            return pool.getSelectedIndex(choice)  == selection;
        }

        @Override
        public void updateWidgetNarration(@NotNull NarrationElementOutput output) {
            this.defaultButtonNarrationText(output);
        }
    }
}
