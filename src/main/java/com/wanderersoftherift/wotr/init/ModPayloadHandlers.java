package com.wanderersoftherift.wotr.init;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.item.skillgem.AbilitySlots;
import com.wanderersoftherift.wotr.network.AbilitySlotsContentMessage;
import com.wanderersoftherift.wotr.network.AbilitySlotsUpdateMessage;
import com.wanderersoftherift.wotr.network.SelectSkillUpgradeRequest;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = WanderersOfTheRift.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ModPayloadHandlers {
    public static final String PROTOCOL_VERSION = "0.0.1";

    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(WanderersOfTheRift.MODID).versioned(PROTOCOL_VERSION);

        registrar.playToServer(SelectSkillUpgradeRequest.ID, SelectSkillUpgradeRequest.STREAM_CODEC, SelectSkillUpgradeRequest::handleOnServer);
        registrar.playToClient(AbilitySlotsContentMessage.ID, AbilitySlotsContentMessage.STREAM_CODEC, AbilitySlotsContentMessage::handleOnClient);
        registrar.playToClient(AbilitySlotsUpdateMessage.ID, AbilitySlotsUpdateMessage.STREAM_CODEC, AbilitySlotsUpdateMessage::handleOnClient);
    }

    @SubscribeEvent
    public static void onPlayerJoinedEvent(PlayerEvent.PlayerLoggedInEvent loggedInEvent) {
        if (!(loggedInEvent.getEntity() instanceof ServerPlayer serverPlayer)) {
            return;
        }

        AbilitySlots abilitySlots = loggedInEvent.getEntity().getData(ModAttachments.ABILITY_SLOTS);
        PacketDistributor.sendToPlayer(serverPlayer, new AbilitySlotsContentMessage(abilitySlots.abilities));
    }


}
