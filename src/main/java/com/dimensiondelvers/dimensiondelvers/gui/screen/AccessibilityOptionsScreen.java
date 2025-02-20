package com.dimensiondelvers.dimensiondelvers.gui.screen;

import com.dimensiondelvers.dimensiondelvers.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.ModConfigSpec;

import javax.annotation.Nonnull;

@SuppressWarnings("CodeBlock2Expr")
public class AccessibilityOptionsScreen extends Screen {
    private static final Component TITLE = Component.translatable("accessibility.dimensiondelvers.screen.title");
    private final LogoRenderer logoRenderer;
    private FocusableTextWidget textWidget;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 90, 33);
    private final Screen lastScreen;

    public AccessibilityOptionsScreen(Screen lastScreen) {
        super(TITLE);

        this.lastScreen = lastScreen;
        this.logoRenderer = new LogoRenderer(true);
    }

    @Override
    protected void init() {
        LinearLayout linearLayout1 = this.layout.addToContents(LinearLayout.vertical());
        linearLayout1.defaultCellSetting().alignHorizontallyCenter().padding(4);

        this.textWidget = linearLayout1.addChild(new FocusableTextWidget(this.width, this.title, this.font));
        linearLayout1.addChild(new SpacerElement(0, 100));

        GridLayout gridlayout = new GridLayout();
        gridlayout.defaultCellSetting().paddingHorizontal(4).paddingBottom(4).alignHorizontallyCenter();
        GridLayout.RowHelper gridLayout$rowHelper = gridlayout.createRowHelper(2);

        gridLayout$rowHelper.addChild(new SpacerElement(0, 25), 2); // Seperating the textWidget with the ConfigButtons


        gridLayout$rowHelper.addChild(this.createConfigButton(
                Component.translatable("accessibility.dimensiondelvers.screen.trypophobia"),
                Component.translatable("accessibility.dimensiondelvers.screen.tooltip.trypophobia"),
                ClientConfig.ACCESSIBILITY_TRYPOPHOBIA
        ));

        gridLayout$rowHelper.addChild(this.createConfigButton(
                Component.translatable("accessibility.dimensiondelvers.screen.arachnophobia"),
                Component.translatable("accessibility.dimensiondelvers.screen.tooltip.arachnophobia"),
                ClientConfig.ACCESSIBILITY_ARACHNOPHOBIA
        ));

        gridLayout$rowHelper.addChild(this.createConfigButton(
                Component.translatable("accessibility.dimensiondelvers.screen.flashing_lights"),
                Component.translatable("accessibility.dimensiondelvers.screen.tooltip.flashing_lights"),
                ClientConfig.ACCESSIBILITY_FLASHING_LIGHTS
        ));

        gridLayout$rowHelper.addChild(this.createConfigButton(
                Component.translatable("accessibility.dimensiondelvers.screen.misophonia"),
                Component.translatable("accessibility.dimensiondelvers.screen.tooltip.misophonia"),
                ClientConfig.ACCESSIBILITY_MISOPHONIA
        ));

        gridLayout$rowHelper.addChild(this.createConfigButton(
                Component.translatable("accessibility.dimensiondelvers.screen.high_contrast"),
                Component.translatable("accessibility.dimensiondelvers.screen.tooltip.high_contrast"),
                ClientConfig.ACCESSIBILITY_HIGH_CONTRAST
        ));

        gridLayout$rowHelper.addChild(this.createConfigButton(
                Component.translatable("accessibility.dimensiondelvers.screen.hard_of_hearing"),
                Component.translatable("accessibility.dimensiondelvers.screen.tooltip.hard_of_hearing"),
                ClientConfig.ACCESSIBILITY_HARD_OF_HEARING
        ));

        gridLayout$rowHelper.addChild(this.createConfigButton(
                Component.translatable("accessibility.dimensiondelvers.screen.reduced_motion"),
                Component.translatable("accessibility.dimensiondelvers.screen.tooltip.reduced_motion"),
                ClientConfig.ACCESSIBILITY_REDUCED_MOTION
        ));


        this.layout.addToContents(gridlayout);
        this.layout.addToFooter(Button.builder(lastScreen instanceof TitleScreen ? CommonComponents.GUI_CONTINUE : CommonComponents.GUI_BACK, (button) -> this.onClose()).build());
        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();

    }


    @Override
    protected void repositionElements() {
        if (this.textWidget != null) {
            this.textWidget.containWithin(this.width);
        }

        this.layout.arrangeElements();
    }

    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.logoRenderer.renderLogo(guiGraphics, this.width, 1.0F);
    }

    @Override
    protected void renderPanorama(@Nonnull GuiGraphics guiGraphics, float partialTick) {
        PANORAMA.render(guiGraphics, this.width, this.height, 1.0F, 0.0F);
    }

    // Helper for creating a cycle-button based off of a boolean
    private CycleButton<Boolean> createBooleanButton(Component name, Component tooltip, CycleButton.OnValueChange<Boolean> onChange, boolean initialValue) {
        return CycleButton.booleanBuilder(
                Component.translatable("options.off"),
                Component.translatable("options.on"))
                .withInitialValue(initialValue)
                .withTooltip(p_321371_ -> Tooltip.create(tooltip)).create(name, onChange);
    }

    // Helper for creating a cycle-button based off of a config boolean value, which is modified upon click
    private CycleButton<Boolean> createConfigButton(Component name, Component tooltip, ModConfigSpec.BooleanValue booleanValue) {
        return createBooleanButton(name, tooltip, ((cycleButton, aBoolean) -> {
            booleanValue.set(!booleanValue.getAsBoolean());
        }), booleanValue.getAsBoolean());
    }

    @Override
    public void onClose() {
        super.onClose();
        Minecraft.getInstance().setScreen(lastScreen);
        ClientConfig.SPEC.save(); // We notify the config watcher that our config was updated
    }
}
