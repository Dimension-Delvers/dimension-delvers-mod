package com.dimensiondelvers.dimensiondelvers.client;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.client.gui.screen.AbilityScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.lwjgl.glfw.GLFW;

import static com.dimensiondelvers.dimensiondelvers.init.ModMenuTypes.TEST_MENU;

@EventBusSubscriber(modid = DimensionDelvers.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {

    //TODO: Replace these with finalized skill keybindings
    public static final KeyMapping ABILITY_1_KEY = new KeyMapping(
            "key." + DimensionDelvers.id("ability1"), // Will be localized using this translation key //arrow
            InputConstants.Type.KEYSYM, // Default mapping is on the keyboard
            GLFW.GLFW_KEY_O, // Default key is P
            "key.categories.misc" // Mapping will be in the misc category
    );

    public static final KeyMapping ABILITY_2_KEY = new KeyMapping(
            "key." + DimensionDelvers.id("ability2"), // Will be localized using this translation key //heal
            InputConstants.Type.KEYSYM, // Default mapping is on the keyboard
            GLFW.GLFW_KEY_U, // Default key is P
            "key.categories.misc" // Mapping will be in the misc category
    );

    public static final KeyMapping ABILITY_3_KEY = new KeyMapping(
            "key." + DimensionDelvers.id("ability3"), // Will be localized using this translation key //boost
            InputConstants.Type.KEYSYM, // Default mapping is on the keyboard
            GLFW.GLFW_KEY_I, // Default key is P
            "key.categories.misc" // Mapping will be in the misc category
    );


//    public static final KeyMapping ARMOR_STAND_KEY = new KeyMapping(
//            "key." + DimensionDelvers.id("armor_stand"), // Will be localized using this translation key
//            InputConstants.Type.KEYSYM, // Default mapping is on the keyboard
//            GLFW.GLFW_KEY_Y, // Default key is P
//            "key.categories.misc" // Mapping will be in the misc category
//    );

//    public static final KeyMapping OPEN_UPGRADE_MENU_KEY = new KeyMapping(
//            "key." + DimensionDelvers.id("unlock_all"), // Will be localized using this translation key
//            InputConstants.Type.KEYSYM, // Default mapping is on the keyboard
//            GLFW.GLFW_KEY_J, // Default key is P
//            "key.categories.misc" // Mapping will be in the misc category
//    );
//
//    public static final KeyMapping PRETTY_KEY = new KeyMapping(
//            "key." + DimensionDelvers.id("be_pretty"), // Will be localized using this translation key
//            InputConstants.Type.KEYSYM, // Default mapping is on the keyboard
//            GLFW.GLFW_KEY_H, // Default key is P
//            "key.categories.misc" // Mapping will be in the misc category
//    );
//
//    public static final KeyMapping SMOL_KEY = new KeyMapping(
//            "key." + DimensionDelvers.id("be_smol"), // Will be localized using this translation key
//            InputConstants.Type.KEYSYM, // Default mapping is on the keyboard
//            GLFW.GLFW_KEY_L, // Default key is P
//            "key.categories.misc" // Mapping will be in the misc category
//    );

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        DimensionDelvers.LOGGER.info("HELLO FROM CLIENT SETUP");
        DimensionDelvers.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event)
    {
        event.register(ABILITY_2_KEY);
        event.register(ABILITY_1_KEY);
        event.register(ABILITY_3_KEY);
//        event.register(ARMOR_STAND_KEY);
//        event.register(OPEN_UPGRADE_MENU_KEY);
//        event.register(PRETTY_KEY);
//        event.register(SMOL_KEY);
    }

    @SubscribeEvent
    private static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(TEST_MENU.get(), AbilityScreen::new);
    }




}
