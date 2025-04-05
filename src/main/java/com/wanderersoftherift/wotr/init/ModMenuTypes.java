package com.wanderersoftherift.wotr.init;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.gui.menu.KeyForgeMenu;
import com.wanderersoftherift.wotr.client.gui.menu.TestMenu;
import com.wanderersoftherift.wotr.gui.menu.RuneAnvilMenu;
import com.wanderersoftherift.wotr.gui.menu.SkillBenchMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, WanderersOfTheRift.MODID);

    public static final Supplier<MenuType<RuneAnvilMenu>> RUNE_ANVIL_MENU = MENUS.register("rune_anvil_menu", () -> new MenuType<>(RuneAnvilMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static final Supplier<MenuType<KeyForgeMenu>> KEY_FORGE_MENU = MENUS.register("key_forge_menu", () -> new MenuType<>(KeyForgeMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static final Supplier<MenuType<TestMenu>> TEST_MENU = MENUS.register("test_menu", () -> new MenuType<>(TestMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static final Supplier<MenuType<SkillBenchMenu>> SKILL_BENCH_MENU = MENUS.register("skill_bench_menu", () -> new MenuType<>(SkillBenchMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
