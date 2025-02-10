package com.dimensiondelvers.dimensiondelvers.item.abilities;

import com.dimensiondelvers.dimensiondelvers.gui.menu.AbilityBagMenu;
import com.dimensiondelvers.dimensiondelvers.gui.menu.ComponentContainer;
import com.dimensiondelvers.dimensiondelvers.init.ModAbilityTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;


public class AbilityBagItem extends Item {


    public AbilityBagItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        level.registryAccess().lookupOrThrow(ModAbilityTypes.ABILITY_KEY);
        if (!level.isClientSide) {

//            int slot = hand == InteractionHand.MAIN_HAND ? player.getInventory().selected : 0;
//            if (stack.get(DataComponents.CONTAINER) == null) {
//                stack.set(DataComponents.CONTAINER,ItemContainerContents.EMPTY);
//            }
//           player.openMenu(new SimpleMenuProvider(
//                   (containerId, playerInventory, _player) ->
//                           new AbilityBagMenu(containerId,playerInventory,new ComponentContainer(stack)),
//                   Component.translatable("item.dimensiondelvers.ability_bag")
//           ), registryFriendlyByteBuf -> {
//                    registryFriendlyByteBuf.writeInt(slot);
//                   });
        }


        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());

    }
    private static ItemContainerContents getStorage(ItemStack stack) {
        ItemContainerContents contents = stack.get(DataComponents.CONTAINER);
        return contents != null ? contents : ItemContainerContents.EMPTY;

    }




}
