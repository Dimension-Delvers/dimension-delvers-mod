package com.dimensiondelvers.dimensiondelvers.gui.layer.objective;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ObjectiveRenderers {

    public static Map<ResourceLocation, ObjectiveRenderer> RENDERERS = new HashMap<>();

    public static void register(ResourceLocation id, ObjectiveRenderer renderer) {
        RENDERERS.put(id, renderer);
    }

    public static ObjectiveRenderer get(ResourceLocation id) {
        return RENDERERS.get(id);
    }
}
