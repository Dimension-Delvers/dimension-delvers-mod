package com.dimensiondelvers.dimensiondelvers.block;

import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

import static net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion.MOD_ID;

public class BlockFamilyHelper {
    public static final String SLAB_SUFFIX = "_slab";
    public static final String STAIRS_SUFFIX = "_stairs";
    public static final String BUTTON_SUFFIX = "_button";
    public static final String PLATE_SUFFIX = "_pressure_plate";
    public static final String WALL_SUFFIX = "_wall";
    public static final String FENCE_SUFFIX = "_fence";
    public static final String TRAPDOOR_SUFFIX = "_trapdoor";

    private final String blockId;
    private final Supplier<Block> block;
    private final Supplier<Block> slab;
    private final Supplier<Block> stairs;
    private final Supplier<Block> button;
    private final Supplier<Block> pressurePlate;
    private final Supplier<Block> wall;
    private final Supplier<Block> fence;
    private final Supplier<Block> trapdoor;
    private BlockFamily blockFamily;

    public BlockFamilyHelper(String blockId, Supplier<Block> block, Supplier<Block> slab, Supplier<Block> stairs, Supplier<Block> button, Supplier<Block> pressurePlate, Supplier<Block> wall, Supplier<Block> fence, Supplier<Block> trapdoor) {
        this.blockId = blockId;
        this.block = block;
        this.slab = slab;
        this.stairs = stairs;
        this.button = button;
        this.pressurePlate = pressurePlate;
        this.wall = wall;
        this.fence = fence;
        this.trapdoor = trapdoor;
    }

    public String getBlockId() {
        return blockId;
    }

    public String getId() {
        return blockId;
    }

    public Supplier<Block> getBlock() {
        return block;
    }

    public Supplier<Block> getSlab() {
        return slab;
    }

    public Supplier<Block> getStairs() {
        return stairs;
    }

    public Supplier<Block> getButton() {
        return button;
    }

    public Supplier<Block> getPressurePlate() {
        return pressurePlate;
    }

    public Supplier<Block> getFence() {
        return fence;
    }

    public Supplier<Block> getWall() {
        return wall;
    }

    public Supplier<Block> getTrapdoor() {
        return trapdoor;
    }

    public BlockFamily getFamily() {
        if (blockFamily == null) {
            blockFamily = new BlockFamily.Builder(block.get())
                    .slab(slab.get())
                    .stairs(stairs.get())
                    .button(button.get())
                    .pressurePlate(pressurePlate.get())
                    .wall(wall.get())
                    .fence(fence.get())
                    .trapdoor(trapdoor.get())
                    .getFamily();
        }
        return blockFamily;
    }

    public ResourceLocation getBaseResourceLocation() {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, blockId);
    }

    public static class Builder {

        private String blockId;
        private Supplier<Block> block;
        private Supplier<Block> slab;
        private Supplier<Block> stairs;
        private Supplier<Block> button;
        private Supplier<Block> pressurePlate;
        private Supplier<Block> wall;
        private Supplier<Block> fence;
        private Supplier<Block> trapdoor;

        public Builder withBlockId(String blockId) {
            this.blockId = blockId;
            return this;
        }

        public Builder withBlock(Supplier<Block> block) {
            this.block = block;
            return this;
        }

        public Builder withSlab(Supplier<Block> slab) {
            this.slab = slab;
            return this;
        }

        public Builder withStairs(Supplier<Block> stairs) {
            this.stairs = stairs;
            return this;
        }

        public Builder withButton(Supplier<Block> button) {
            this.button = button;
            return this;
        }

        public Builder withPressurePlate(Supplier<Block> pressurePlate) {
            this.pressurePlate = pressurePlate;
            return this;
        }

        public Builder withFence(Supplier<Block> fence) {
            this.fence = fence;
            return this;
        }

        public Builder withWall(Supplier<Block> wall) {
            this.wall = wall;
            return this;
        }

        public Builder withTrapdoor(Supplier<Block> trapdoor) {
            this.trapdoor = trapdoor;
            return this;
        }

        public BlockFamilyHelper createBuildBlockHelper() {
            return new BlockFamilyHelper(blockId, block, slab, stairs, button, pressurePlate, wall, fence, trapdoor);
        }
    }


}
