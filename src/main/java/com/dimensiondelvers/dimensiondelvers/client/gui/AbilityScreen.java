package com.dimensiondelvers.dimensiondelvers.client.gui;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class AbilityScreen extends Screen {

    protected AbilityScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();

        // Add widgets and precomputed values
        //TODO make sure of translation string here
        Button.OnPress AbilityPressed = new AbilityPressed();
        this.addRenderableWidget(Button.builder(Component.literal("Test"), AbilityPressed).build());
    }

    public class AbilityPressed implements Button.OnPress {

        @Override
        public void onPress(Button button) {

        }
    }
}
