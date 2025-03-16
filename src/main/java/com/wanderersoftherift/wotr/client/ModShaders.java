package com.wanderersoftherift.wotr.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import net.minecraft.client.renderer.ShaderDefines;
import net.minecraft.client.renderer.ShaderProgram;

public class ModShaders {
	public static ShaderProgram SKYBOX = new ShaderProgram(WanderersOfTheRift.id("skybox"), DefaultVertexFormat.BLOCK, ShaderDefines.EMPTY);
}