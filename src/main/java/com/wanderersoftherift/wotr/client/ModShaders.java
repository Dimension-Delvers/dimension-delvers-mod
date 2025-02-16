package com.wanderersoftherift.wotr.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import net.minecraft.client.renderer.ShaderDefines;
import net.minecraft.client.renderer.ShaderProgram;

public class ModShaders {
    public static ShaderProgram RIFT_PORTAL = new ShaderProgram(WanderersOfTheRift.id("rift_portal"), DefaultVertexFormat.NEW_ENTITY, ShaderDefines.EMPTY);
}
