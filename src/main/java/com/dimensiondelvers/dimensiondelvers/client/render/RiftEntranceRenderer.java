package com.dimensiondelvers.dimensiondelvers.client.render;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.client.ModShaders;
import com.dimensiondelvers.dimensiondelvers.entity.RiftEntranceEntity;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.function.BiFunction;

public class RiftEntranceRenderer extends EntityRenderer<RiftEntranceEntity, EntityRenderState> {
    public static final RenderStateShard.ShaderStateShard PARALLAX_SHADER = new RenderStateShard.ShaderStateShard(
            ModShaders.ENTITY_PARALLAX_SHADER
    );

    public static final BiFunction<ResourceLocation, ResourceLocation, RenderType> ENTITY_CUTOUT_PARALLAX = Util.memoize(
            (tex1, tex2) -> {
                RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                        .setShaderState(PARALLAX_SHADER)
                        .setTextureState(new RenderStateShard.MultiTextureStateShard.Builder()
                                .add(tex1, false, false)
                                .add(tex2, false, false).build())
                        .setTransparencyState(RenderStateShard.NO_TRANSPARENCY)
                        .setCullState(RenderStateShard.NO_CULL)
                        .setLightmapState(RenderStateShard.LIGHTMAP)
                        .setOverlayState(RenderStateShard.NO_OVERLAY).createCompositeState(false);
                return RenderType.create("entity_cutout_parallax", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 1536, true, false, rendertype$compositestate);
            }
    );

    public static RenderType parallaxCutout(ResourceLocation outerTexture, ResourceLocation innerTexture) {
        return ENTITY_CUTOUT_PARALLAX.apply(outerTexture, innerTexture);
    }

    private static final ResourceLocation OUTER_RIFT_LOCATION = DimensionDelvers.id("textures/entity/outer_rift.png");
    private static final ResourceLocation INNER_RIFT_LOCATION = DimensionDelvers.id("textures/entity/inner_rift.png");
    private static final RenderType RENDER_TYPE = parallaxCutout(OUTER_RIFT_LOCATION, INNER_RIFT_LOCATION);

    public RiftEntranceRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EntityRenderState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(2.0F, 2.0F, 2.0F);

        Vector3f cameraPos = this.entityRenderDispatcher.camera.getPosition().toVector3f();
        Vector3f dir = new Vector3f((float)state.x, (float)state.y, (float)state.z).sub(cameraPos);
        dir.y = 0;

        poseStack.mulPose(new Quaternionf().lookAlong(dir, new Vector3f(0,-1,0)));

        PoseStack.Pose posestack$pose = poseStack.last();
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RENDER_TYPE);
        vertex(vertexconsumer, posestack$pose, packedLight, -0.5f, -0.5f, 0.0f, 0, 1);
        vertex(vertexconsumer, posestack$pose, packedLight, 0.5f, -0.5f, 0.0f, 1, 1);
        vertex(vertexconsumer, posestack$pose, packedLight, 0.5f, 0.5f, 0.0f, 1, 0);
        vertex(vertexconsumer, posestack$pose, packedLight, -0.5f, 0.5f, 0.0f, 0, 0);

//        vertex(vertexconsumer, posestack$pose, packedLight, 0.0f, -0.5f, -0.5f, 0, 1);
//        vertex(vertexconsumer, posestack$pose, packedLight, 0.0f, -0.5f, 0.5f,1, 1);
//        vertex(vertexconsumer, posestack$pose, packedLight, 0.0f, 0.5f, 0.5f,1, 0);
//        vertex(vertexconsumer, posestack$pose, packedLight, 0.0f, 0.5f, -0.5f,0, 0);

        poseStack.popPose();
        super.render(state, poseStack, bufferSource, packedLight);
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, int packedLight, float x, float y, float z, int u, int v) {
        consumer.addVertex(pose, x, (float) y, z)
                .setColor(-1)
                .setUv((float) u, (float) v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}
