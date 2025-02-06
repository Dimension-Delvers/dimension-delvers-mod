package com.dimensiondelvers.dimensiondelvers.client;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = DimensionDelvers.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {

    //TODO DATA GEN LANG
    public static final KeyMapping ARROW_KEY  = new KeyMapping(
            "key." + DimensionDelvers.id("summon_arrow"), // Will be localized using this translation key
            InputConstants.Type.KEYSYM, // Default mapping is on the keyboard
            GLFW.GLFW_KEY_O, // Default key is P
            "key.categories.misc" // Mapping will be in the misc category
    );
    public static final KeyMapping BOOST_KEY = new KeyMapping(
            "key." + DimensionDelvers.id("boost"), // Will be localized using this translation key
            InputConstants.Type.KEYSYM, // Default mapping is on the keyboard
            GLFW.GLFW_KEY_I, // Default key is P
            "key.categories.misc" // Mapping will be in the misc category
    );

    public static final KeyMapping HEAL_KEY = new KeyMapping(
            "key." + DimensionDelvers.id("heal"), // Will be localized using this translation key
            InputConstants.Type.KEYSYM, // Default mapping is on the keyboard
            GLFW.GLFW_KEY_U, // Default key is P
            "key.categories.misc" // Mapping will be in the misc category
    );

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        DimensionDelvers.LOGGER.info("HELLO FROM CLIENT SETUP");
        DimensionDelvers.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event)
    {
        event.register(HEAL_KEY);
        event.register(ARROW_KEY);
        event.register(BOOST_KEY);
    }


}
