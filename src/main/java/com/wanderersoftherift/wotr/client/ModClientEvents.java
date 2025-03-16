package com.wanderersoftherift.wotr.client;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.InputConstants;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.client.gui.screen.AbilityScreen;
import com.wanderersoftherift.wotr.client.render.item.ability.AbilitySpecialRenderer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

import static com.wanderersoftherift.wotr.init.ModMenuTypes.TEST_MENU;

@EventBusSubscriber(modid = WanderersOfTheRift.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {

    public static final String ABILITY_CATEGORY = WanderersOfTheRift.translationId("key", "categories.ability");

    public static final KeyMapping ABILITY_1_KEY = new KeyMapping(
            WanderersOfTheRift.translationId("key","ability.1"),
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            ABILITY_CATEGORY
    );

    public static final KeyMapping ABILITY_2_KEY = new KeyMapping(
            WanderersOfTheRift.translationId("key","ability.2"),
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            ABILITY_CATEGORY
    );

    public static final KeyMapping ABILITY_3_KEY = new KeyMapping(
            WanderersOfTheRift.translationId("key","ability.3"),
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            ABILITY_CATEGORY
    );

    public static final KeyMapping ABILITY_4_KEY = new KeyMapping(
            WanderersOfTheRift.translationId("key","ability.4"),
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            ABILITY_CATEGORY
    );

    public static final KeyMapping ABILITY_5_KEY = new KeyMapping(
            WanderersOfTheRift.translationId("key","ability.5"),
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            ABILITY_CATEGORY
    );

    public static final KeyMapping ABILITY_6_KEY = new KeyMapping(
            WanderersOfTheRift.translationId("key","ability.6"),
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            ABILITY_CATEGORY
    );

    public static final KeyMapping ABILITY_7_KEY = new KeyMapping(
            WanderersOfTheRift.translationId("key","ability.7"),
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            ABILITY_CATEGORY
    );

    public static final KeyMapping ABILITY_8_KEY = new KeyMapping(
            WanderersOfTheRift.translationId("key","ability.8"),
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            ABILITY_CATEGORY
    );

    public static final KeyMapping ABILITY_9_KEY = new KeyMapping(
            WanderersOfTheRift.translationId("key","ability.9"),
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            ABILITY_CATEGORY
    );

    public static final ImmutableList<KeyMapping> ABILITY_SLOT_KEYS = ImmutableList.<KeyMapping>builder().add(
            ABILITY_1_KEY, ABILITY_2_KEY, ABILITY_3_KEY, ABILITY_4_KEY, ABILITY_5_KEY,
            ABILITY_6_KEY, ABILITY_7_KEY, ABILITY_8_KEY, ABILITY_9_KEY).build();

    public static final KeyMapping NEXT_ABILITY_KEY = new KeyMapping(
            WanderersOfTheRift.translationId("key","ability.next"),
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            ABILITY_CATEGORY
    );

    public static final KeyMapping PREV_ABILITY_KEY = new KeyMapping(
            WanderersOfTheRift.translationId("key","ability.previous"),
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            ABILITY_CATEGORY
    );

    public static final KeyMapping USE_ABILITY_KEY = new KeyMapping(
            WanderersOfTheRift.translationId("key","ability.use_selected"),
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            ABILITY_CATEGORY
    );

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        WanderersOfTheRift.LOGGER.info("HELLO FROM CLIENT SETUP");
        WanderersOfTheRift.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        for (KeyMapping key : ABILITY_SLOT_KEYS) {
            event.register(key);
        }
        event.register(PREV_ABILITY_KEY);
        event.register(NEXT_ABILITY_KEY);
        event.register(USE_ABILITY_KEY);
    }

    @SubscribeEvent
    private static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(TEST_MENU.get(), AbilityScreen::new);
    }

    @SubscribeEvent
    public static void registerSpecialRenderers(RegisterSpecialModelRendererEvent event) {
        event.register(
                // The name to reference as the type
                WanderersOfTheRift.id("ability_item"),
                // The map codec
                AbilitySpecialRenderer.Unbaked.MAP_CODEC
        );
    }

}
