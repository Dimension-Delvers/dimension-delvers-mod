package com.wanderersoftherift.wotr.config;

import com.wanderersoftherift.wotr.gui.config.HudElementConfig;
import com.wanderersoftherift.wotr.gui.config.ScreenAnchor;
import com.wanderersoftherift.wotr.gui.config.UIOrientation;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    public static final ModConfigSpec SPEC;

    // Accessibility Settings
    public static final ModConfigSpec.BooleanValue ACCESSIBILITY_FLASHING_LIGHTS;
    public static final ModConfigSpec.BooleanValue ACCESSIBILITY_ARACHNOPHOBIA;
    public static final ModConfigSpec.BooleanValue ACCESSIBILITY_MISOPHONIA;
    // To-be-determined if this will be used at all
    public static final ModConfigSpec.BooleanValue ACCESSIBILITY_TRYPOPHOBIA;
    public static final ModConfigSpec.BooleanValue ACCESSIBILITY_HIGH_CONTRAST;
    public static final ModConfigSpec.BooleanValue ACCESSIBILITY_REDUCED_MOTION;
    // I am unsure what this should change currently
    public static final ModConfigSpec.BooleanValue ACCESSIBILITY_HARD_OF_HEARING;

    // Rift Map Settings
    public static final ModConfigSpec.BooleanValue MOUSE_MODE;
    public static final ModConfigSpec.DoubleValue LERP_SPEED;

    // WotR HUD
    public static final HudElementConfig ABILITY_BAR;
    public static final HudElementConfig MANA_BAR;
    public static final HudElementConfig EFFECT_DISPLAY;
    public static final HudElementConfig OBJECTIVE;

    // Vanilla HUD
    public static final HudElementConfig HOT_BAR;
    public static final HudElementConfig EXPERIENCE_BAR;
    public static final HudElementConfig HEALTH_ARMOR;
    public static final HudElementConfig FOOD_LEVEL;
    public static final HudElementConfig AIR_LEVEL;
    public static final HudElementConfig EXPERIENCE_LEVEL;
    public static final HudElementConfig VANILLA_EFFECTS;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment(" == Wotr Client Configs ==");

        // Pushing Client configs related to accessibility
        builder.push(" == Accessibility == ");
        ACCESSIBILITY_FLASHING_LIGHTS = builder.comment(" Whether flashing-light effects should be disabled")
                .define("accessibilityFlashingLights", false);
        ACCESSIBILITY_ARACHNOPHOBIA = builder.comment(" Whether Spiders should be replaced with something else")
                .define("accessibilityArachnophobia", false);
        ACCESSIBILITY_MISOPHONIA = builder.comment(" Whether certain sounds should be replaced with something else")
                .define("accessibilityMisophonia", false);
        ACCESSIBILITY_TRYPOPHOBIA = builder.comment(" Whether certain textures should be replaced with something else")
                .define("accessibilityTrypophobia", false);
        ACCESSIBILITY_HIGH_CONTRAST = builder
                .comment(" Whether GUI & HUD elements should be replaced with higher contrast for better visibility")
                .define("accessibilityHighContrast", false);
        ACCESSIBILITY_REDUCED_MOTION = builder.comment(" Whether motion should be reduced")
                .define("accessibilityReducedMotion", false);
        ACCESSIBILITY_HARD_OF_HEARING = builder.comment(" Whether certain sounds should be upped")
                .define("accessibilityHardOfHearing", false);
        builder.pop();

        builder.push(" == Rift Map == ");
        MOUSE_MODE = builder.comment(" Whether to use the Whale mouse mode").define("mouseMode", false);
        LERP_SPEED = builder.comment(" What speed the map should lerp at. 0 = Off")
                .defineInRange("lerpSpeed", 1.0, 0.0, 2.0);
        builder.pop();

        ABILITY_BAR = new HudElementConfig(builder, "Ability Bar", "abilityBar", ScreenAnchor.TOP_LEFT, 0, 0,
                UIOrientation.VERTICAL);
        MANA_BAR = new HudElementConfig(builder, "Mana Bar", "manaBar", ScreenAnchor.TOP_LEFT, 25, 0,
                UIOrientation.VERTICAL);
        EFFECT_DISPLAY = new HudElementConfig(builder, "Effect Display", "effectDisplay", ScreenAnchor.TOP_LEFT, 31, 0,
                UIOrientation.VERTICAL);
        OBJECTIVE = new HudElementConfig(builder, "Objective", "objective", ScreenAnchor.TOP_CENTER, 0, 25);

        HOT_BAR = new HudElementConfig(builder, "Vanilla Hot Bar", "hotBar", ScreenAnchor.BOTTOM_CENTER, 0, 0);
        EXPERIENCE_BAR = new HudElementConfig(builder, "Vanilla Experience Bar", "xpBar", ScreenAnchor.BOTTOM_CENTER, 0,
                -24, UIOrientation.HORIZONTAL);
        HEALTH_ARMOR = new HudElementConfig(builder, "Vanilla Health and Armor", "healthArmor",
                ScreenAnchor.BOTTOM_CENTER, -51, -30);
        FOOD_LEVEL = new HudElementConfig(builder, "Vanilla Food", "food", ScreenAnchor.BOTTOM_CENTER, 50, -30);
        AIR_LEVEL = new HudElementConfig(builder, "Vanilla Air", "air", ScreenAnchor.BOTTOM_CENTER, 0, -24);
        EXPERIENCE_LEVEL = new HudElementConfig(builder, "Vanilla Experience Level", "xpLevel",
                ScreenAnchor.BOTTOM_CENTER, 0, -24);
        VANILLA_EFFECTS = new HudElementConfig(builder, "Vanilla Effects", "effects", ScreenAnchor.BOTTOM_CENTER, 0,
                -24);

        SPEC = builder.build();
    }
}
