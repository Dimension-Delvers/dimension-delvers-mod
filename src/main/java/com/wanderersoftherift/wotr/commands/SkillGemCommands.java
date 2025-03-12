package com.wanderersoftherift.wotr.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.wanderersoftherift.wotr.Registries.AbilityRegistry;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.abilities.AbstractAbility;
import com.wanderersoftherift.wotr.init.ModDataComponentType;
import com.wanderersoftherift.wotr.item.skillgem.Upgrade;
import com.wanderersoftherift.wotr.item.skillgem.UpgradePool;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class SkillGemCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(
                Commands.literal(WanderersOfTheRift.MODID + ":makeSkillItem")
                        .then(Commands.argument("ability", AbilityArgument.ability())
                                .then(Commands.argument("choices", IntegerArgumentType.integer(1))
                                        .executes(
                                                (ctx) -> addSkillToCurrentItem(
                                                        ctx.getSource(),
                                                        AbilityArgument.getAbility(ctx, "ability"),
                                                        IntegerArgumentType.getInteger(ctx, "choices")
                                                )
                                        )))) ;

    }

    private static int addSkillToCurrentItem(CommandSourceStack source, AbstractAbility ability, int choices) {
        try {
            ServerPlayer player = source.getPlayerOrException();
            ItemStack item = player.getInventory().getSelected();
            if (!item.isEmpty()) {
                Registry<Upgrade> registryReference = source.getLevel().registryAccess().lookup(Upgrade.UPGRADE_REGISTRY_KEY).get();

                UpgradePool.Mutable upgradePool = new UpgradePool.Mutable(registryReference.stream().map(registryReference::wrapAsHolder).toList());
                upgradePool.generateChoices(choices, source.getLevel().random, 3);

                Registry<AbstractAbility> abilities = source.getLevel().registryAccess().lookupOrThrow(AbilityRegistry.DATA_PACK_ABILITY_REG_KEY);

                DataComponentPatch patch = DataComponentPatch.builder()
                        .set(ModDataComponentType.ABILITY.get(), abilities.wrapAsHolder(ability))
                        .set(ModDataComponentType.UPGRADE_POOL.get(), upgradePool.toImmutable())
                        .build();
                item.applyComponents(patch);
                source.sendSuccess(() -> Component.literal("Applied skill gem components"), true);
            }
            return 1;
        } catch (CommandSyntaxException e) {
            return 0;
        }
    }
}
