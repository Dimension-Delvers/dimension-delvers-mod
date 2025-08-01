package com.wanderersoftherift.wotr.world.level.levelgen.space;

import net.minecraft.core.Direction;

/**
 * checks if corridor is real
 */
public interface CorridorValidator {

    CorridorValidator INVALID = (x, y, z, d) -> false;

    boolean validateCorridor(int x, int y, int z, Direction d);

}
