package com.wanderersoftherift.wotr.util;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TextureUtils {
    private static final Map<ResourceLocation, Dimension> textureSizeCache = new HashMap<>();

    public static void resetCached() {
        textureSizeCache.clear();
    }

    /**
     * Returns the file image width of a given {@link ResourceLocation}
     *
     * @param resource the {@link ResourceLocation} to read
     * @return The image width as an int
     */
    public static int getTextureWidth(ResourceLocation resource) {
        return getTextureDimension(resource).width;
    }

    /**
     * Returns the file image width of a given {@link ResourceLocation}
     *
     * @param resource the {@link ResourceLocation} to read
     * @return The image height as an int
     */
    public static int getTextureHeight(ResourceLocation resource) {
        return getTextureDimension(resource).height;
    }

    /**
     * Returns the dimensions width of a given {@link ResourceLocation}
     *
     * @param resource the {@link ResourceLocation} to read
     * @return The image dimensions of the resource as a {@link Dimension} object
     */
    private static Dimension getTextureDimension(ResourceLocation resource) {
        if (textureSizeCache.containsKey(resource)) {
            return textureSizeCache.get(resource);
        }

        try {
            Optional<Resource> mcResource = Minecraft.getInstance().getResourceManager().getResource(resource);
            if (mcResource.isPresent()) {
                try (InputStream inputStream = mcResource.get().open()) {
                    BufferedImage image = ImageIO.read(inputStream);
                    Dimension dim = new Dimension(image.getWidth(), image.getHeight());
                    textureSizeCache.put(resource, dim);
                    return dim;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Default fallback
        Dimension defaultDim = new Dimension(-1, -1);
        textureSizeCache.put(resource, defaultDim);
        return defaultDim;
    }
}
