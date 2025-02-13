package com.dimensiondelvers.dimensiondelvers.item.runegem;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemStackedOnOtherEvent;

import static com.dimensiondelvers.dimensiondelvers.DimensionDelvers.MODID;
import static net.neoforged.fml.common.EventBusSubscriber.Bus;

@EventBusSubscriber(bus = Bus.GAME, modid = MODID)
public class RunegemEvents {

    // To Be Deleted???
    @SubscribeEvent
    public static void itemStackedOnOther(ItemStackedOnOtherEvent event) {
        Item item = event.getStackedOnItem().getItem();
        if (item instanceof Runegem runegem) {
            runegem.onStackedOnOther(
                    event.getStackedOnItem(),
                    event.getSlot(),
                    event.getClickAction(),
                    event.getPlayer()
            );
        }
    }
}
