package com.wanderersoftherift.wotr.core.rift;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.entity.portal.RiftPortalExitEntity;
import com.wanderersoftherift.wotr.init.WotrAttachments;
import com.wanderersoftherift.wotr.init.WotrEntities;
import com.wanderersoftherift.wotr.item.riftkey.RiftConfig;
import com.wanderersoftherift.wotr.mixin.AccessorMappedRegistry;
import com.wanderersoftherift.wotr.mixin.AccessorMinecraftServer;
import com.wanderersoftherift.wotr.mixinextension.WotRGameRules;
import com.wanderersoftherift.wotr.network.S2CLevelListUpdatePacket;
import com.wanderersoftherift.wotr.world.level.RiftDimensionType;
import com.wanderersoftherift.wotr.world.level.SingleBlockGenerator;
import com.wanderersoftherift.wotr.world.level.levelgen.theme.LevelRiftThemeData;
import com.wanderersoftherift.wotr.world.level.levelgen.theme.RiftTheme;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

/**
 * Static manager for handing access to, creation, and destruction of a rift
 */
public final class RiftLevelManager {

    private RiftLevelManager() {
    }

    /**
     * @param id
     * @return Whether a level with the given id exists
     */
    public static boolean levelExists(ResourceLocation id) {
        return ServerLifecycleHooks.getCurrentServer()
                .forgeGetWorldMap()
                .containsKey(ResourceKey.create(Registries.DIMENSION, id));
    }

    /**
     * @param level
     * @return Whether the level is a rift
     */
    public static boolean isRift(Level level) {
        Registry<DimensionType> dimTypes = level.registryAccess().lookupOrThrow(Registries.DIMENSION_TYPE);
        Optional<Holder.Reference<DimensionType>> riftType = dimTypes.get(RiftDimensionType.RIFT_DIMENSION_TYPE);
        return riftType.filter(dimensionTypeReference -> dimensionTypeReference.value() == level.dimensionType())
                .isPresent();
    }

    /**
     * @param id
     * @return The rift level with the given id, if it exists
     */
    public static @Nullable ServerLevel getRiftLevel(ResourceLocation id) {
        var server = ServerLifecycleHooks.getCurrentServer();

        ServerLevel serverLevel = server.forgeGetWorldMap().get(ResourceKey.create(Registries.DIMENSION, id));
        if (serverLevel != null && isRift(serverLevel)) {
            return serverLevel;
        }
        return null;
    }

    public static void onPlayerDeath(ServerPlayer player, ServerLevel level) {
        if (!isRift(level)) {
            return;
        }
        RiftData riftData = RiftData.get(player.serverLevel());
        NeoForge.EVENT_BUS.post(new RiftEvent.PlayerDied(player, player.serverLevel(), riftData.getConfig()));
        riftData.removePlayer(player);
        if (riftData.isRiftEmpty()) {
            unregisterAndDeleteLevel(player.serverLevel());
        }
        player.setData(WotrAttachments.DIED_IN_RIFT, true);
    }

    /**
     * @param player The player to remove from a rift level
     * @return Whether the player was successfully removed from a rift
     */
    public static boolean returnPlayerFromRift(ServerPlayer player) {
        ServerLevel riftLevel = player.serverLevel();
        if (!isRift(riftLevel)) {
            return false;
        }

        RiftData riftData = RiftData.get(riftLevel);

        ResourceKey<Level> respawnKey = riftData.getPortalDimension();
        if (respawnKey == riftLevel.dimension()) {
            respawnKey = Level.OVERWORLD;
        }

        ServerLevel respawnDimension = riftLevel.getServer().getLevel(respawnKey);
        if (respawnDimension == null) {
            respawnDimension = riftLevel.getServer().overworld();
        }

        if (!riftData.containsPlayer(player)) {
            return false;
        }

        var respawnPos = riftData.getPortalPos().above();
        riftData.removePlayer(player);
        player.teleportTo(respawnDimension, respawnPos.getCenter().x(), respawnPos.getY(), respawnPos.getCenter().z(),
                Set.of(), player.getRespawnAngle(), 0, true);
        if (riftData.getPlayers().isEmpty()) {
            RiftLevelManager.unregisterAndDeleteLevel(riftLevel);
        }
        return true;
    }

    // TODO: unload the dimensions if all players are disconnected, but still in the dimension
    @SuppressWarnings("deprecation")
    public static ServerLevel getOrCreateRiftLevel(
            ResourceLocation id,
            ResourceKey<Level> portalDimension,
            BlockPos portalPos,
            RiftConfig config) {
        var server = ServerLifecycleHooks.getCurrentServer();
        var ow = server.overworld();

        var existingRift = server.forgeGetWorldMap().get(ResourceKey.create(Registries.DIMENSION, id));
        if (existingRift != null) {
            return existingRift;
        }

        Optional<Registry<Level>> dimensionRegistry = ow.registryAccess().lookup(Registries.DIMENSION);
        if (dimensionRegistry.isEmpty()) {
            return null;
        }

        ChunkGenerator chunkGen = getRiftChunkGenerator(ow);
        if (chunkGen == null) {
            return null;
        }

        var stem = getLevelStem(server, id, chunkGen);
        if (stem == null) {
            return null;
        }

        ServerLevel level = createRift(id, stem, portalDimension, portalPos, config);

        Registry<Level> registry = dimensionRegistry.get();
        if (registry instanceof MappedRegistry<Level> mappedRegistry) {
            mappedRegistry.unfreeze(false);
            if (registry.get(id).isEmpty()) {
                Registry.register(registry, id, level);
            }
            mappedRegistry.freeze();
        }

        level.getServer().forgeGetWorldMap().put(level.dimension(), level);
        level.getServer().markWorldsDirty();

        NeoForge.EVENT_BUS.post(new RiftEvent.Created(level, config));
        NeoForge.EVENT_BUS.post(new LevelEvent.Load(level));

        PacketDistributor.sendToAllPlayers(new S2CLevelListUpdatePacket(id, false));
        spawnRiftExit(level, new BlockPos(0, 0, 0).above().getBottomCenter());
        WanderersOfTheRift.LOGGER.debug("Created rift level {}", id);
        return level;
    }

    /**
     * copy of {@link com.wanderersoftherift.wotr.item.riftkey.RiftKey::spawnRift(Level, Vec3, Direction)}
     */
    // TODO: clean it up (maybe move as static method to the entity or the spawner class)
    private static void spawnRiftExit(Level level, Vec3 pos) {
        RiftPortalExitEntity rift = new RiftPortalExitEntity(WotrEntities.RIFT_EXIT.get(), level);
        rift.setPos(pos);
        rift.setYRot(Direction.UP.toYRot());
        rift.setBillboard(true);
        level.addFreshEntity(rift);
    }

    @SuppressWarnings("deprecation")
    private static LevelStem getLevelStem(MinecraftServer server, ResourceLocation id, ChunkGenerator chunkGen) {
        Optional<Registry<LevelStem>> levelStemRegistry = server.overworld()
                .registryAccess()
                .lookup(Registries.LEVEL_STEM);
        if (levelStemRegistry.isEmpty()) {
            return null;
        }

        var riftType = server.registryAccess()
                .lookupOrThrow(Registries.DIMENSION_TYPE)
                .get(RiftDimensionType.RIFT_DIMENSION_TYPE)
                .orElse(null);
        if (riftType == null) {
            WanderersOfTheRift.LOGGER.error("Failed to get rift dimension type");
            return null;
        }
        var stem = new LevelStem(riftType, chunkGen);
        var stemRegistry = levelStemRegistry.get();
        if (stemRegistry instanceof MappedRegistry<LevelStem> mappedStemRegistry) {
            mappedStemRegistry.unfreeze(false);
            if (stemRegistry.get(id).isEmpty()) {
                Registry.register(stemRegistry, id, stem);
            }
            mappedStemRegistry.freeze();
        }
        return stem;
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    private static void unregisterAndDeleteLevel(ServerLevel level) {
        if (!isRift(level)) {
            return;
        }
        RiftData riftData = RiftData.get(level);
        if (!riftData.getPlayers().isEmpty()) {
            // multiplayer - delete after all players leave
            return;
        }

        NeoForge.EVENT_BUS.post(new RiftEvent.Closing(level, riftData.getConfig()));
        // unload the level
        level.save(null, true, false);
        level.getServer().forgeGetWorldMap().remove(level.dimension());
        NeoForge.EVENT_BUS.post(new LevelEvent.Unload(level));
        ResourceLocation id = level.dimension().location();
        PacketDistributor.sendToAllPlayers(new S2CLevelListUpdatePacket(id, true));
        level.getServer().markWorldsDirty();

        // Delete level files - we might need to move this to end of tick because ticking (block)entities might have
        // references to the level
        var dimPath = ((AccessorMinecraftServer) level.getServer()).getStorageSource()
                .getDimensionPath(level.dimension());
        if (Files.exists(dimPath)) {
            WanderersOfTheRift.LOGGER.info("Deleting level {}", dimPath);
            try {
                FileUtils.deleteDirectory(dimPath.toFile());
            } catch (IOException e) {
                WanderersOfTheRift.LOGGER.error("Failed to delete level", e);
            }
        }

        // dimensions are also saved in level.dat
        // this monstrosity deletes them from the registry to prevent reloading them on next server start
        level.getServer().registryAccess().lookupOrThrow(Registries.DIMENSION).get(level.dimension()).ifPresent(dim -> {
            if (level.getServer()
                    .registryAccess()
                    .lookupOrThrow(Registries.DIMENSION) instanceof MappedRegistry<Level> mr) {
                Holder.Reference<Level> holder = ((AccessorMappedRegistry<Level>) mr).getByLocation().remove(id);
                if (holder == null) {
                    WanderersOfTheRift.LOGGER.error("Failed to remove level from registry (null holder)");
                    return;
                }
                int dimId = mr.getId(level.dimension());
                if (dimId == -1) {
                    WanderersOfTheRift.LOGGER.error("Failed to remove level from registry (id -1)");
                    return;
                }
                ((AccessorMappedRegistry<Level>) mr).getToId().remove(holder.value());
                ((AccessorMappedRegistry<Level>) mr).getById().set(dimId, null);
                ((AccessorMappedRegistry<Level>) mr).getByKey().remove(holder.key());
                ((AccessorMappedRegistry<Level>) mr).getByValue().remove(holder.value());
                ((AccessorMappedRegistry<Level>) mr).getRegistrationInfos().remove(holder.key());
            }
        });
        level.getServer().overworld().save(null, true, false);
    }

    private static ChunkGenerator getRiftChunkGenerator(ServerLevel overworld) {
        var voidBiome = overworld.registryAccess().lookupOrThrow(Registries.BIOME).get(Biomes.THE_VOID).orElse(null);
        if (voidBiome == null) {
            return null;
        }
        return new SingleBlockGenerator(new FixedBiomeSource(voidBiome),
                ResourceLocation.withDefaultNamespace("bedrock"));
    }

    private static ServerLevel createRift(
            ResourceLocation id,
            LevelStem stem,
            ResourceKey<Level> portalDimension,
            BlockPos portalPos,
            RiftConfig config) {
        AccessorMinecraftServer server = (AccessorMinecraftServer) ServerLifecycleHooks.getCurrentServer();
        var chunkProgressListener = server.getProgressListenerFactory().create(0);
        var storageSource = server.getStorageSource();
        var worldData = server.getWorldData();
        var executor = server.getExecutor();

        if (portalDimension == null || portalPos == null) {
            WanderersOfTheRift.LOGGER.warn(
                    "Tried to create rift {} with portal from dimension {} at position {}, using overworld spawnpoint instead.",
                    id, portalDimension, portalPos);
            portalDimension = Level.OVERWORLD;
            portalPos = ServerLifecycleHooks.getCurrentServer().overworld().getSharedSpawnPos();
        }

        int seed = config.seed().orElseGet(() -> new Random().nextInt());
        LevelSettings levelSettings = worldData.getLevelSettings();
        LevelSettings riftLevelSettings = new LevelSettings(
                "rift",
                levelSettings.gameType(),
                levelSettings.hardcore(),
                levelSettings.difficulty(),
                levelSettings.allowCommands(),
                levelSettings.gameRules(),
                levelSettings.getDataConfiguration(),
                levelSettings.getLifecycle()
        );

        PrimaryLevelData primaryLevelData = new PrimaryLevelData(worldData.getLevelSettings(), worldData.worldGenOptions(), PrimaryLevelData.SpecialWorldProperty.NONE, worldData.worldGenSettingsLifecycle());
        ((WotRGameRules)primaryLevelData.getGameRules()).wotrMakeMutable();

        var riftLevel = new ServerLevel(ServerLifecycleHooks.getCurrentServer(), executor, storageSource,
                primaryLevelData,
                ResourceKey.create(Registries.DIMENSION, id), stem, chunkProgressListener, false, 0L, List.of(), false,
                RandomSequences.factory(seed).constructor().get());
        var riftData = RiftData.get(riftLevel);
        riftData.setPortalDimension(portalDimension);
        riftData.setPortalPos(portalPos);
        riftData.setConfig(config);
        var themeData = LevelRiftThemeData.getFromLevel(riftLevel);

        Holder<RiftTheme> riftTheme;
        int maxDepth;
        riftTheme = config.theme().orElse(LevelRiftThemeData.getRandomTheme(riftLevel));
        maxDepth = getRiftSize(config.tier());
        themeData.setTheme(riftTheme);

        placeInitialJigsaw(riftLevel, WanderersOfTheRift.id("rift/room_portal"), WanderersOfTheRift.id("portal"),
                maxDepth, new BlockPos(0, 2, 0));

        ((WotRGameRules) riftLevel.getGameRules()).wotrSetRuleFresh(GameRules.RULE_DOFIRETICK, GameRules.BooleanValue.create(false).createRule()).set(false, riftLevel.getServer());
        return riftLevel;
    }

    private static void placeInitialJigsaw(
            ServerLevel level,
            ResourceLocation templatePoolKey,
            ResourceLocation target,
            int maxDepth,
            BlockPos pos) {
        var templatePool = level.registryAccess()
                .lookupOrThrow(Registries.TEMPLATE_POOL)
                .get(templatePoolKey)
                .orElse(null);
        if (templatePool == null) {
            WanderersOfTheRift.LOGGER.error("Template pool {} not found", templatePoolKey);
            return;
        }
        JigsawPlacement.generateJigsaw(level, templatePool, target, maxDepth, pos, false);
    }

    private static int getRiftSize(int tier) {
        return switch (tier) {
            case 0, 1 -> 5; // no chaos
            case 2 -> 7;
            case 3 -> 9;
            case 4 -> 11;
            case 5 -> 13;
            case 6 -> 15;
            case 7 -> 17;
            default -> 19; // structures are capped at 20 and it has to be odd for POIs to spawn
        };
    }

}
