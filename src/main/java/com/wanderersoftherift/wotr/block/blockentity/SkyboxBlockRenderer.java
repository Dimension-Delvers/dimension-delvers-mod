package com.wanderersoftherift.wotr.block.blockentity;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.platform.GlConst;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.sun.jna.platform.win32.OpenGL32;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.client.ModShaders;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForgeConfig;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;

@OnlyIn(Dist.CLIENT)
public class SkyboxBlockRenderer<T extends SkyboxBlockEntity> implements BlockEntityRenderer<T> {
	public static final ResourceLocation END_SKY_LOCATION = ResourceLocation.fromNamespaceAndPath(WanderersOfTheRift.MODID, "textures/environment/skybox.png");

	private static final RenderStateShard.ShaderStateShard SKYBOX_SHADER = new RenderStateShard.ShaderStateShard(ModShaders.SKYBOX);

	public static final RenderType SKYBOX_RENDERTYPE = RenderType.create(
			WanderersOfTheRift.MODID + "_skybox",
			DefaultVertexFormat.POSITION,
			VertexFormat.Mode.QUADS,
			1536,
			false,
			false,
			RenderType.CompositeState.builder()
					.setShaderState(SKYBOX_SHADER)
					.setTextureState(RenderStateShard.MultiTextureStateShard.builder()
							.add(END_SKY_LOCATION, true, false)
							.build()
					).createCompositeState(false)
	);


	public SkyboxBlockRenderer(BlockEntityRendererProvider.Context context) {
	}

	public void render(T p_112650_, float p_112651_, PoseStack p_112652_, MultiBufferSource p_112653_, int p_112654_, int p_112655_) {
		Matrix4f matrix4f = p_112652_.last().pose();

		CompiledShaderProgram shader = RenderSystem.setShader(ModShaders.SKYBOX);
		if (shader != null) {
			Uniform screenSize = shader.getUniform("ScreenSize");
			if (screenSize != null) {
				screenSize.set((float)Minecraft.getInstance().getWindow().getWidth(), (float)Minecraft.getInstance().getWindow().getHeight());
			}

			Uniform view = shader.getUniform("View");
			if (view != null) {
				float x = Minecraft.getInstance().cameraEntity.getXRot();
				if (Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_FRONT) {
					x *= -1;
				}
				float y = Mth.wrapDegrees(Minecraft.getInstance().cameraEntity.getYRot());
				view.set(x, y);
			}

			shader.apply();
		}
		this.renderCube(p_112650_, matrix4f, p_112653_.getBuffer(this.renderType()));
	}

	private void renderCube(T blockEntity, Matrix4f pose, VertexConsumer consumer) {
		float f = this.getOffsetDown();
		float f1 = this.getOffsetUp();
		this.renderFace(blockEntity, pose, consumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, Direction.SOUTH);
		this.renderFace(blockEntity, pose, consumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Direction.NORTH);
		this.renderFace(blockEntity, pose, consumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.EAST);
		this.renderFace(blockEntity, pose, consumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.WEST);
		this.renderFace(blockEntity, pose, consumer, 0.0F, 1.0F, f, f, 0.0F, 0.0F, 1.0F, 1.0F, Direction.DOWN);
		this.renderFace(blockEntity, pose, consumer, 0.0F, 1.0F, f1, f1, 1.0F, 1.0F, 0.0F, 0.0F, Direction.UP);
	}

	private void renderFace(
			T blockEntity,
			Matrix4f pose,
			VertexConsumer consumer,
			float x0,
			float x1,
			float y0,
			float y1,
			float z0,
			float z1,
			float z2,
			float z3,
			Direction direction
	) {
		consumer.addVertex(pose, x0, y0, z0);
		consumer.addVertex(pose, x1, y0, z1);
		consumer.addVertex(pose, x1, y1, z2);
		consumer.addVertex(pose, x0, y1, z3);
	}

	protected float getOffsetUp() {
		return 1.0F;
	}

	protected float getOffsetDown() {
		return 0.0F;
	}

	protected RenderType renderType() {
		return SKYBOX_RENDERTYPE;
	}
}
