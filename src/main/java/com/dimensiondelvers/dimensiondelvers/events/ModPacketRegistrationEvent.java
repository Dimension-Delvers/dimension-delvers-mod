package com.dimensiondelvers.dimensiondelvers.events;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.network.S2CDimensionTypesUpdatePacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = DimensionDelvers.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModPacketRegistrationEvent {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                S2CDimensionTypesUpdatePacket.TYPE,
                S2CDimensionTypesUpdatePacket.STREAM_CODEC,
                new S2CDimensionTypesUpdatePacket.S2CDimensionTypesUpdatePacketHandler()
        );
    }
}
