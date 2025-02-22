package com.wanderersoftherift.wotr.world.level;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.init.ModBlocks;
import com.wanderersoftherift.wotr.mixin.AccessorMappedRegistry;
import com.wanderersoftherift.wotr.mixin.AccessorMinecraftServer;
import com.wanderersoftherift.wotr.network.S2CLevelListUpdatePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.LevelStem;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TemporaryLevelManager {
    private static final List<TemporaryLevel> levels = new ArrayList<>();

    public static TemporaryLevel createRiftLevel(ResourceKey<Level> portalDimension, BlockPos portalPos) {
        // Blockpos until we store dimension id in the portal
        ResourceLocation id = WanderersOfTheRift.id("rift_" + portalPos.getX() + "_" + portalPos.getY() + "_" + portalPos.getZ()/* UUID.randomUUID()*/);
        return getOrCreateRiftLevel(id, portalDimension, portalPos);
    }

    @SuppressWarnings("deprecation")
    public static TemporaryLevel getOrCreateRiftLevel(ResourceLocation id, ResourceKey<Level> portalDimension, BlockPos portalPos) {
        var server = ServerLifecycleHooks.getCurrentServer();
        var ow = server.overworld();

        var existingRift = levels.stream().filter(level -> level.getId().equals(id)).findAny();
        if (existingRift.isPresent()) {
            return existingRift.get();
        }

        Optional<Registry<Level>> dimensionRegistry = ow.registryAccess().lookup(Registries.DIMENSION);
        if (dimensionRegistry.isEmpty()) {
            return null;
        }

        ChunkGenerator chunkGen = getRiftChunkGenerator();

        var stem = getLevelStem(server, id, chunkGen);
        if (stem == null) {
            return null;
        }

        TemporaryLevel level = TemporaryLevel.create(id, stem, portalDimension, portalPos);

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
        NeoForge.EVENT_BUS.post(new LevelEvent.Load(level));
        PacketDistributor.sendToAllPlayers(new S2CLevelListUpdatePacket(id, false));
        TemporaryLevelManager.levels.add(level);
        level.setBlock(new BlockPos(0, -1, 0), ModBlocks.RIFT_PORTAL_BLOCK.get().defaultBlockState(), 3);
        return level;
    }

    @SuppressWarnings("deprecation")
    private static LevelStem getLevelStem(MinecraftServer server, ResourceLocation id, ChunkGenerator chunkGen) {
        Optional<Registry<LevelStem>> levelStemRegistry = server.overworld().registryAccess().lookup(Registries.LEVEL_STEM);
        if (levelStemRegistry.isEmpty()) {
            return null;
        }

        var riftType = server.registryAccess().lookupOrThrow(Registries.DIMENSION_TYPE)
            .get(RiftDimensionType.RIFT_DIMENSION_TYPE).get();
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

    @SuppressWarnings("deprecation")
    public static void unregisterLevel(TemporaryLevel level) {
        if (!level.getPlayers().isEmpty()) {
            return;
        }
        level.save(null, true, false);
        level.getServer().forgeGetWorldMap().remove(level.dimension());
        NeoForge.EVENT_BUS.post(new LevelEvent.Unload(level));
        ResourceLocation id = level.getId();
        PacketDistributor.sendToAllPlayers(new S2CLevelListUpdatePacket(id, true));
        level.getServer().markWorldsDirty();
        TemporaryLevelManager.levels.remove(level);
    }

    @SuppressWarnings({"unchecked", "deprecation"})
    public static void unregisterAndDeleteLevel(TemporaryLevel level) {
        if (!level.getPlayers().isEmpty()) {
            // multiplayer
            return;
        }

        // unload the level
        level.save(null, true, false);
        level.getServer().forgeGetWorldMap().remove(level.dimension());
        NeoForge.EVENT_BUS.post(new LevelEvent.Unload(level));
        ResourceLocation id = level.getId();
        PacketDistributor.sendToAllPlayers(new S2CLevelListUpdatePacket(id, true));
        level.getServer().markWorldsDirty();
        TemporaryLevelManager.levels.remove(level);

        // Delete level files
        var dimPath = ((AccessorMinecraftServer)level.getServer()).getStorageSource().getDimensionPath(level.dimension());
        WanderersOfTheRift.LOGGER.info("Deleting level {}", dimPath);
        try {
            Files.walkFileTree(dimPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attributes) throws IOException {
                    WanderersOfTheRift.LOGGER.debug("Deleting {}", path);
                    Files.delete(path);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path path, IOException exception) throws IOException {
                    if (exception != null) {
                        throw exception;
                    } else {
                        Files.deleteIfExists(path);
                        return FileVisitResult.CONTINUE;
                    }
                }
            });
        } catch (IOException var7) {
            WanderersOfTheRift.LOGGER.error("Failed to delete level", var7);
        }

        // dimensions are also saved in level.dat
        // this monstrosity deletes them from the registry to prevent reloading them on next server start
        level.getServer().registryAccess().lookupOrThrow(Registries.DIMENSION).get(level.dimension()).ifPresent(dim -> {
            if (level.getServer().registryAccess().lookupOrThrow(Registries.DIMENSION) instanceof MappedRegistry<Level> mr) {
                Holder.Reference<Level> holder = ((AccessorMappedRegistry<Level>)mr).getByLocation().remove(id);
                if (holder == null) {
                    WanderersOfTheRift.LOGGER.error("Failed to remove level from registry (null holder)");
                    return;
                }
                int dimId = mr.getId(level.dimension());
                if (dimId == -1){
                    WanderersOfTheRift.LOGGER.error("Failed to remove level from registry (id -1)");
                    return;
                }
                ((AccessorMappedRegistry<Level>)mr).getToId().remove(holder.value());
                ((AccessorMappedRegistry<Level>)mr).getById().set(dimId, null);
                ((AccessorMappedRegistry<Level>)mr).getByKey().remove(holder);
                ((AccessorMappedRegistry<Level>)mr).getByValue().remove(holder);
                ((AccessorMappedRegistry<Level>)mr).getRegistrationInfos().remove(holder.key());
            }
        });
        level.getServer().overworld().save(null, true, false);
    }

    private static ChunkGenerator getRiftChunkGenerator() {
        var voidBiomeSource =  new FixedBiomeSource(ServerLifecycleHooks.getCurrentServer().overworld().registryAccess().lookupOrThrow(Registries.BIOME).get(Biomes.THE_VOID).get());
        return new PocRiftChunkGenerator(voidBiomeSource, ResourceLocation.withDefaultNamespace("melon"));
    }
}
