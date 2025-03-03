package com.dimensiondelvers.dimensiondelvers.rift.objective;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.network.S2CRiftObjectiveStatusPacket;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Optional;

@EventBusSubscriber(modid = DimensionDelvers.MODID)
public class ObjectiveEventWrapper {

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        if (event.getEntity().level() instanceof ServerLevel serverLevel) {
            LevelRiftObjectiveData data = LevelRiftObjectiveData.getFromLevel(serverLevel);
            if (data != null && data.getObjective() != null) {
                boolean dirty = data.getObjective().onLivingDeath(event, serverLevel, data);
                if (dirty) {
                    data.setDirty();
                    broadcastObjectiveStatus(serverLevel, data);
                }
            }
        }
    }

    private static void broadcastObjectiveStatus(ServerLevel serverLevel, LevelRiftObjectiveData data) {
        PacketDistributor.sendToPlayersInDimension(serverLevel, new S2CRiftObjectiveStatusPacket(Optional.ofNullable(data.getObjective())));
    }
}
