package com.wanderersoftherift.wotr.gui.config.preset;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class PresetManager implements PreparableReloadListener {
    private static final String PATH = "hud_preset";
    private static final String EXTENSION = ".json";
    public static final PresetManager INSTANCE = new PresetManager();

    private Map<ResourceLocation, HudPreset> presets = ImmutableMap.of();

    private PresetManager() {
    }

    public Map<ResourceLocation, HudPreset> getPresets() {
        return presets;
    }

    @Override
    public @NotNull CompletableFuture<Void> reload(
            @NotNull PreparationBarrier barrier,
            @NotNull ResourceManager manager,
            @NotNull Executor backgroundExecutor,
            @NotNull Executor gameExecutor) {
        return this.load(manager, backgroundExecutor)
                .thenCompose(barrier::wait)
                .thenAcceptAsync(values -> this.presets = values, gameExecutor);
    }

    private CompletableFuture<Map<ResourceLocation, HudPreset>> load(ResourceManager manager, Executor executor) {
        return CompletableFuture.supplyAsync(() -> load(manager), executor);
    }

    private static Map<ResourceLocation, HudPreset> load(ResourceManager manager) {
        Map<ResourceLocation, HudPreset> values = new LinkedHashMap<>();

        for (Map.Entry<ResourceLocation, Resource> hudPreset : manager
                .listResources(PATH, resourceLocation -> resourceLocation.getPath().endsWith(EXTENSION))
                .entrySet()) {
            ResourceLocation location = ResourceLocation.fromNamespaceAndPath(hudPreset.getKey().getNamespace(),
                    hudPreset.getKey()
                            .getPath()
                            .substring(PATH.length() + 1, hudPreset.getKey().getPath().length() - EXTENSION.length()));
            try (Reader reader = hudPreset.getValue().openAsReader()) {
                JsonElement jsonElement = JsonParser.parseReader(reader);
                DataResult<Map<ResourceLocation, ElementPreset>> result = HudPreset.MAP_CODEC.parse(JsonOps.INSTANCE,
                        jsonElement);
                result.ifSuccess((preset) -> values.put(location, new HudPreset(location, preset)))
                        .ifError(error -> WanderersOfTheRift.LOGGER.error("Failed to read hud_preset '{}': {}",
                                location, error.message()));
            } catch (IOException e) {
                WanderersOfTheRift.LOGGER.error("Failed to read hud_preset '{}'", location, e);
            }
        }

        return ImmutableMap.copyOf(values);
    }
}
