package com.wanderersoftherift.wotr.block;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.gui.menu.SkillBenchMenu;
import com.wanderersoftherift.wotr.init.ModAttachments;
import com.wanderersoftherift.wotr.item.handler.ChangeAwareItemHandler;
import com.wanderersoftherift.wotr.item.skillgem.AbilitySlots;
import com.wanderersoftherift.wotr.network.AbilitySlotsUpdateMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class SkillBenchBlock extends Block {
    private static final Component CONTAINER_TITLE = Component.translatable("container."+ WanderersOfTheRift.MODID + ".skill_bench");

    public SkillBenchBlock(Properties properties) {
        super(properties);
    }

    protected MenuProvider getMenuProvider(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos) {
        return new SimpleMenuProvider((containerId, playerInventory, player) -> {
            AbilitySlots slots = playerInventory.player.getData(ModAttachments.ABILITY_SLOTS);
            IItemHandler replicatedSlots = new ChangeAwareItemHandler(slots) {
                    @Override
                    public void onSlotChanged(int slot) {
                        PacketDistributor.sendToPlayer((ServerPlayer) player, new AbilitySlotsUpdateMessage(slot, slots.getStackInSlot(slot)));
                    }
                };
            return new SkillBenchMenu(containerId, playerInventory, ContainerLevelAccess.create(level, pos), replicatedSlots);
        }, CONTAINER_TITLE);
    }

    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(level, pos));
            return InteractionResult.CONSUME;
        }
    }
}
