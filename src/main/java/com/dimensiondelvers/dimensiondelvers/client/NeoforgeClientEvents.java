package com.dimensiondelvers.dimensiondelvers.client;


import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.networking.data.UseAbility;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static com.dimensiondelvers.dimensiondelvers.client.ModClientEvents.*;

@EventBusSubscriber(modid = DimensionDelvers.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class NeoforgeClientEvents {

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Post event)
    {
        //TODO static register abilities and use their names here
        while (ARROW_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new UseAbility(DimensionDelvers.id("summon_arrow").toString()));
        }

        if (HEAL_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new UseAbility(DimensionDelvers.id("heal").toString()));
        }

        if (BOOST_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new UseAbility(DimensionDelvers.id("boost").toString()));
        }
    }
}
