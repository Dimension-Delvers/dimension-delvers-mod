package com.dimensiondelvers.dimensiondelvers.common;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.init.ModAbilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import static com.dimensiondelvers.dimensiondelvers.init.ModAbilities.COOL_DOWN_ATTACHMENTS;

@EventBusSubscriber(modid = DimensionDelvers.MODID, bus = EventBusSubscriber.Bus.GAME)
public class GameEvents {

    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Post event)
    {
        Player p = event.getEntity();
        for(ResourceLocation loc: ModAbilities.COOLDOWN_MAP.keySet())
        {
            if(p.hasData(COOL_DOWN_ATTACHMENTS.get(loc)))
            {
                p.setData(COOL_DOWN_ATTACHMENTS.get(loc), Math.max(p.getData(COOL_DOWN_ATTACHMENTS.get(loc)) - 1, 0)); //Decrease to the lowest value 0;
            }

        }

        //TODO send cooldown packet to player to sync their GUI
    }
}
