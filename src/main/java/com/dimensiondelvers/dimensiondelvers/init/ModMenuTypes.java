package com.dimensiondelvers.dimensiondelvers.init;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.client.gui.menu.TestMenu;
import com.dimensiondelvers.dimensiondelvers.gui.menu.RuneAnvilMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, DimensionDelvers.MODID);
    public static final Supplier<MenuType<TestMenu>> TEST_MENU = MENUS.register("test_menu", () -> new MenuType<>(TestMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<RuneAnvilMenu>> RUNE_ANVIL_MENU = MENUS.register("rune_anvil_menu", () -> new MenuType<>(RuneAnvilMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
