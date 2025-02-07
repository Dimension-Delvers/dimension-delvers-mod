package com.dimensiondelvers.dimensiondelvers;

import com.dimensiondelvers.dimensiondelvers.Registries.AbilityRegistry;
import com.dimensiondelvers.dimensiondelvers.Registries.UpgradeRegistry;
import com.dimensiondelvers.dimensiondelvers.abilities.AbilityAttributes;
import com.dimensiondelvers.dimensiondelvers.init.ModBlocks;
import com.dimensiondelvers.dimensiondelvers.init.ModCreativeTabs;
import com.dimensiondelvers.dimensiondelvers.init.ModItems;
import com.dimensiondelvers.dimensiondelvers.init.ModMenuTypes;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

import static com.dimensiondelvers.dimensiondelvers.Registries.AbilityRegistry.ABILITY_REGISTRY_DEF;
import static com.dimensiondelvers.dimensiondelvers.Registries.AbilityRegistry.ATTACHMENT_TYPES;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(DimensionDelvers.MODID)
public class DimensionDelvers {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "dimensiondelvers";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger(); //TODO change back to private when done testing because lazy



    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public DimensionDelvers(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup); // Register the commonSetup method for modloading



        // Register things
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        ModMenuTypes.MENUS.register(modEventBus);





        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (DimensionDelvers) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);


        modEventBus.addListener(this::addCreative); // Register the item to a creative tab

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        //TODO look into moving these, idk how this would handle if other mods want to add their own abilities, probably fine but not POC worth
        AbilityRegistry.ABILITY_REGISTRY_DEF.register(modEventBus);
        UpgradeRegistry.UPGRADE_REGISTRY_DEF.register(modEventBus);

        ATTACHMENT_TYPES.register(modEventBus);

        AbilityAttributes.REGISTRY.register(modEventBus);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock) LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) event.accept(ModBlocks.EXAMPLE_BLOCK);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting"); // Do something when the server starts
    }



    /**
     * Helper method to get a {@code ResourceLocation} with our Mod Id and a passed in name
     *
     * @param name the name to create the {@code ResourceLocation} with
     * @return A {@code ResourceLocation} with the given name
     */
    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name);
    }
}
