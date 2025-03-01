package com.dimensiondelvers.dimensiondelvers.client;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderDefines;
import net.minecraft.client.renderer.ShaderProgram;

public class ModShaders {
    public static ShaderProgram RIFT_PORTAL = new ShaderProgram(DimensionDelvers.id("rift_portal"), DefaultVertexFormat.NEW_ENTITY, ShaderDefines.EMPTY);
}
