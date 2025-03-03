package com.dimensiondelvers.dimensiondelvers.gui.layer.objective;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.network.S2CRiftObjectiveStatusPacket;
import com.dimensiondelvers.dimensiondelvers.rift.objective.AbstractObjective;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.dimensiondelvers.dimensiondelvers.init.ModOngoingObjectiveTypes.ONGOING_OBJECTIVE_TYPE_REGISTRY;

public class ObjectiveRenderers {

    public static Map<ResourceLocation, Function<AbstractObjective, ObjectiveRenderer>> RENDERERS = new HashMap<>();

    public static final Function<AbstractObjective, ObjectiveRenderer> STEALTH = register(DimensionDelvers.id("stealth"), StealthObjectiveRenderer::create);

    public static Function<AbstractObjective, ObjectiveRenderer> register(ResourceLocation id, Function<AbstractObjective, ObjectiveRenderer> renderer) {
        RENDERERS.put(id, renderer);
        return renderer;
    }

    public static Function<AbstractObjective, ObjectiveRenderer> get(ResourceLocation id) {
        return RENDERERS.get(id);
    }


    public static void handleObjectiveStatus(S2CRiftObjectiveStatusPacket packet) {
        if(packet.objective().isPresent()) {
            AbstractObjective objective = packet.objective().get();
            ResourceLocation key = ONGOING_OBJECTIVE_TYPE_REGISTRY.getKey(objective.getCodec());
            Function<AbstractObjective, ObjectiveRenderer> renderer = get(key);
            if(renderer != null) {
                ObjectiveRenderer.CURRENT = renderer.apply(objective);
            }else{
                ObjectiveRenderer.CURRENT = null;
            }
        }else{
            ObjectiveRenderer.CURRENT = null;
        }
    }
}
