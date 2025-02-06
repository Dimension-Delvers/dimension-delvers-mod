package com.dimensiondelvers.dimensiondelvers.networking;

import com.dimensiondelvers.dimensiondelvers.networking.abilities.ServerPayloadHandler;
import com.dimensiondelvers.dimensiondelvers.networking.data.UseAbility;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModPayloads {

    public static void register(final PayloadRegistrar registrar) {
        registrar.playToServer(
                UseAbility.TYPE,
                UseAbility.STREAM_CODEC,
                ServerPayloadHandler::handleAbilityOnServer
        );
    }
}
