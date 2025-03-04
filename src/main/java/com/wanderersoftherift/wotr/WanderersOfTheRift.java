package com.wanderersoftherift.wotr;

import com.wanderersoftherift.wotr.commands.InventorySnapshotCommands;
import com.wanderersoftherift.wotr.Registries.AbilityRegistry;
import com.wanderersoftherift.wotr.Registries.UpgradeRegistry;
import com.wanderersoftherift.wotr.commands.SkillGemCommands;
import com.wanderersoftherift.wotr.config.ClientConfig;
import com.wanderersoftherift.wotr.gui.menu.SkillBenchMenu;
import com.wanderersoftherift.wotr.gui.screen.RuneAnvilScreen;
import com.wanderersoftherift.wotr.abilities.AbilityAttributes;
import com.wanderersoftherift.wotr.gui.screen.SkillBenchScreen;
import com.wanderersoftherift.wotr.init.*;
import com.wanderersoftherift.wotr.network.SelectSkillUpgradeRequest;
import com.wanderersoftherift.wotr.server.inventorySnapshot.InventorySnapshotSystem;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;

@Mod(WanderersOfTheRift.MODID)
public class WanderersOfTheRift {
    public static final String MODID = "wotr";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String PROTOCOL_VERSION = "1";

    public WanderersOfTheRift(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        // Register things
        ModDataComponentType.DATA_COMPONENTS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModMenuTypes.MENUS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        ModAttachments.ATTACHMENT_TYPES.register(modEventBus);
        ModLootModifiers.GLOBAL_LOOT_MODIFIER_SERIALIZERS.register(modEventBus);
        ModModifierEffects.MODIFIER_EFFECT_TYPES.register(modEventBus);
        ModEffects.EFFECTS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (Wotr) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative); // Register the item to a creative tab
        modEventBus.addListener(this::registerPayloadHandler);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        AbilityRegistry.ABILITY_TYPES.register(modEventBus);
        AbilityRegistry.EFFECTS.register(modEventBus);
        AbilityRegistry.EFFECT_TARGETING.register(modEventBus);

        UpgradeRegistry.UPGRADE_REGISTRY_DEF.register(modEventBus);

        AbilityAttributes.REGISTRY.register(modEventBus);

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

    /**
     * Helper method to get a {@code TagKey} with our Mod Id and a passed in name
     *
     * @param name the name to create the {@code TagKey} with
     * @return A {@code TagKey} with the given name
     */
    public static <T> TagKey<T> tagId(ResourceKey<? extends Registry<T>> registry, String name) {
        return TagKey.create(registry, id(name));
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock) LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info("{} {}", Config.magicNumberIntroduction, Config.magicNumber);

        // Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    @SubscribeEvent
    private void registerCommands(RegisterCommandsEvent event) {
        InventorySnapshotCommands.register(event.getDispatcher(), event.getBuildContext());
        SkillGemCommands.register(event.getDispatcher(), event.getBuildContext());
    }

    private void registerPayloadHandler(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(MODID).versioned(PROTOCOL_VERSION);
        registrar.playToServer(SelectSkillUpgradeRequest.ID, SelectSkillUpgradeRequest.STREAM_CODEC, ((payload, context) -> {
            if (context.player().containerMenu instanceof SkillBenchMenu menu && menu.stillValid(context.player())) {
                menu.selectSkill(payload.choice(), payload.selection());
            }
        }));
    }

    @SubscribeEvent
    private void onDropsFromDeath(LivingDropsEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            InventorySnapshotSystem.getInstance().retainSnapshotItemsOnDeath(player, event);
        }
    }

    @SubscribeEvent
    private void onPlayerDeath(PlayerEvent.PlayerRespawnEvent event) {
        if (!event.isEndConquered() && event.getEntity() instanceof ServerPlayer player) {
            InventorySnapshotSystem.getInstance().restoreItemsOnRespawn(player);
        }
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        //if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) event.accept(ModBlocks.EXAMPLE_BLOCK);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting"); // Do something when the server starts
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }

        @SubscribeEvent
        private static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.RUNE_ANVIL_MENU.get(), RuneAnvilScreen::new);
            event.register(ModMenuTypes.SKILL_BENCH_MENU.get(), SkillBenchScreen::new);
        }
    }
}
