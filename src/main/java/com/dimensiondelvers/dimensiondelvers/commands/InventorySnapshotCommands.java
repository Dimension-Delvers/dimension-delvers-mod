package com.dimensiondelvers.dimensiondelvers.commands;

import com.dimensiondelvers.dimensiondelvers.server.inventorySnapshot.InventorySnapshotSystem;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * Debug commands for testing the inventory snapshot system
 */
public class InventorySnapshotCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(
                Commands.literal("dimensiondelvers:createInventorySnapshot").executes(
                    (ctx) -> createInventorySnapshot(ctx.getSource()
                )
        ));

    }

    private static int createInventorySnapshot(CommandSourceStack source) {
        try {
            ServerPlayer player = source.getPlayerOrException();
            InventorySnapshotSystem.getInstance().captureSnapshot(player);
            source.sendSuccess(() -> Component.literal("Created inventory snapshot"), true);
            return 1;
        } catch (CommandSyntaxException e) {
            return 0;
        }
    }

}
