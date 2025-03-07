package com.dimensiondelvers.dimensiondelvers.commands;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

/**
 * Abstract base class for all commands in the mod.
 * Provides common functionality for command registration and permission handling.
 */
public abstract class BaseCommand {
    private final String name;
    private final int permissionLevel;

    public BaseCommand(String name, int permissionLevel) {
        this.name = name;
        this.permissionLevel = permissionLevel;
    }
    /**
     * Registers this command with the provided dispatcher.
     * The command is automatically added under the mod's namespace.
     *
     * @param dispatcher The command dispatcher handling command registration.
     * @param context    The command build context (used for parameter parsing).
     */
    public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        // Prevent registration if it's a client-only command running on the server
        if (isClientSideOnly() && FMLEnvironment.dist != Dist.CLIENT) {
            return;
        }

        // Define the base command with permission level
        LiteralArgumentBuilder<CommandSourceStack> argumentBuilder = Commands.literal(name)
                .requires(sender -> sender.hasPermission(permissionLevel));

        this.buildCommand(argumentBuilder, context); // Build subcommands
        dispatcher.register(Commands.literal(DimensionDelvers.MODID).then(argumentBuilder)); // Register under the mod's namespace
    }

    /**
     * Determines whether this command should be registered only on the client.<br>
     * If this returns {@code true}, the command won't be available on a dedicated server.
     *
     * @return {@code true} if this command should be client-side only, {@code false} if both sides should have it.
     */
    protected boolean isClientSideOnly() {
        return false; // Default is both client & server
    }

    /**
     * Allows subclasses to define subcommands or additional arguments.
     *
     * @param builder The command builder to which subcommands should be added.
     */
    protected abstract void buildCommand(LiteralArgumentBuilder<CommandSourceStack> builder, CommandBuildContext context);


    /**
     * Possible permission levels to use with the creation of commands<br><br>
     *
     * Permission Levels:<br>
     * - `0` → All players<br>
     * - `1` → Moderator<br>
     * - `2` → Game Master (Recommended for most mod-related commands)<br>
     * - `3` → Admin<br>
     * - `4` → Server Owner<br>
     * @see <a href="https://minecraft.wiki/w/Permission_level#Java_Edition">Java Edition: Permission Levels</a>
     */
    public static class PermissionLevels {
        public static final int ALL = 0; // All Players
        public static final int MODERATOR = 1; // Moderatorss
        public static final int GAME_MASTER = 2;
        public static final int ADMIN = 3;
        public static final int OWNER = 4;
    }
}
