package com.wanderersoftherift.wotr.client;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.gui.hud.AbilityBar;
import com.wanderersoftherift.wotr.networking.data.UseAbility;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static com.wanderersoftherift.wotr.client.ModClientEvents.*;

@EventBusSubscriber(modid = WanderersOfTheRift.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class NeoforgeClientEvents {

    //NOTE: Placeholder to activating abilities, we would want a better way handling the control scheme in the future
    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Post event) {
        //TODO Better handling of this for better control scheme based on weapons etc.
        //Also look into not allowing hold down in the future
        while (ABILITY_1_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new UseAbility(0));
        }

        while (ABILITY_2_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new UseAbility(1));
        }

        while (ABILITY_3_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new UseAbility(2));
        }

//        while (ARMOR_STAND_KEY.consumeClick()) {
//            PacketDistributor.sendToServer(new UseAbility(ARMOR_STAND_ABILITY.get().getName().toString()));
//        }

//        while (OPEN_UPGRADE_MENU_KEY.consumeClick()) {
//            PacketDistributor.sendToServer(new OpenUpgradeMenu(""));
//        }

//        while (PRETTY_KEY.consumeClick()) {
//            PacketDistributor.sendToServer(new UseAbility(BE_PRETTY.get().getName().toString()));
//        }

//        while (SMOL_KEY.consumeClick()) {
//            PacketDistributor.sendToServer(new UseAbility(WanderersOfTheRift.id("be_smol").toString()));
//        }
    }

    @SubscribeEvent
    public static void hudRender(RenderGuiEvent.Post event) {
        AbilityBar.render(event.getGuiGraphics(), Minecraft.getInstance().player, Minecraft.getInstance().level, event.getPartialTick());
    }
}
