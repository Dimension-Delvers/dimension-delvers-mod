package com.dimensiondelvers.dimensiondelvers.rift.objective;

import com.dimensiondelvers.dimensiondelvers.rift.objective.types.StealthObjective;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import static com.dimensiondelvers.dimensiondelvers.DimensionDelvers.LOGGER;

@EventBusSubscriber(modid = "dimensiondelvers")
public class ObjectiveTestEvent {

    @SubscribeEvent
    public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        /*if(event.getEntity().level() instanceof ServerLevel serverLevel) {
            LOGGER.info("Start Objective");
            LevelRiftObjectiveData data = LevelRiftObjectiveData.getFromLevel(serverLevel);
            data.setObjective(new StealthObjective());
        }*/

    }

}
