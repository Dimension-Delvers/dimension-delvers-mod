package com.wanderersoftherift.wotr.networking;

import com.wanderersoftherift.wotr.networking.abilities.ClientPayloadHandler;
import com.wanderersoftherift.wotr.networking.abilities.ServerPayloadHandler;
import com.wanderersoftherift.wotr.networking.data.*;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModPayloads {

    public static void register(final PayloadRegistrar registrar) {
        registrar.playToServer(
                UseAbility.TYPE,
                UseAbility.STREAM_CODEC,
                ServerPayloadHandler::handleAbilityOnServer
        );

        registrar.playToClient(
                CooldownActivated.TYPE,
                CooldownActivated.STREAM_CODEC,
                ClientPayloadHandler::handleCooldownOnClient
        );

        registrar.playToClient(
                ToggleState.TYPE,
                ToggleState.STREAM_CODEC,
                ClientPayloadHandler::handleToggleOnClient
        );

        registrar.playToServer(
                ClaimUpgrade.TYPE,
                ClaimUpgrade.STREAM_CODEC,
                ServerPayloadHandler::handleUpgradeOnServer
        );

        registrar.playToServer(
                OpenUpgradeMenu.TYPE,
                OpenUpgradeMenu.STREAM_CODEC,
                ServerPayloadHandler::handleUpgradeMenuOnServer
        );
    }
}
