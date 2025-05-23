package com.wanderersoftherift.wotr.init.client;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.gui.screen.AbilityBenchScreen;
import com.wanderersoftherift.wotr.gui.screen.KeyForgeScreen;
import com.wanderersoftherift.wotr.gui.screen.RiftCompleteScreen;
import com.wanderersoftherift.wotr.gui.screen.RuneAnvilScreen;
import com.wanderersoftherift.wotr.init.WotrMenuTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = WanderersOfTheRift.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class WotrScreens {

    @SubscribeEvent
    private static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(WotrMenuTypes.RUNE_ANVIL_MENU.get(), RuneAnvilScreen::new);
        event.register(WotrMenuTypes.KEY_FORGE_MENU.get(), KeyForgeScreen::new);
        event.register(WotrMenuTypes.ABILITY_BENCH_MENU.get(), AbilityBenchScreen::new);
        event.register(WotrMenuTypes.RIFT_COMPLETE_MENU.get(), RiftCompleteScreen::new);
    }
}
