package com.dimensiondelvers.dimensiondelvers.rift.objective;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber(modid = DimensionDelvers.MODID)
public class ObjectiveEventWrapper {

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        if(event.getEntity().level() instanceof ServerLevel serverLevel){
            LevelRiftObjectiveData data = LevelRiftObjectiveData.getFromLevel(serverLevel);
            if(data != null && data.getObjective() != null){
                boolean dirty = data.getObjective().onLivingDeath(event, serverLevel, data);
                data.setDirty(dirty);
            }
        }
    }
}
