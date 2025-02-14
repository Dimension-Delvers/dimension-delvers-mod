//package com.dimensiondelvers.dimensiondelvers.abilities;
//
//import com.dimensiondelvers.dimensiondelvers.abilities.types.ToggleAbility;
//import com.dimensiondelvers.dimensiondelvers.init.ModAbilities;
//import com.dimensiondelvers.dimensiondelvers.networking.data.ToggleState;
//import com.mojang.serialization.MapCodec;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.entity.player.Player;
//import net.neoforged.neoforge.network.PacketDistributor;
//
//import static com.dimensiondelvers.dimensiondelvers.init.ModAbilities.TOGGLE_ATTACHMENTS;
//
//public class AbstractToggleAbility extends AbstractAbility implements ToggleAbility {
//    @Override
//    public MapCodec<? extends AbstractAbility> getCodec() {
//        return null;
//    }
//
//    public AbstractToggleAbility(ResourceLocation abilityName) {
//        super(abilityName);
//    }
//
//    @Override
//    public void OnActivate(Player p) {
//
//    }
//
//    @Override
//    public void onDeactivate(Player p) {
//
//    }
//
//    @Override
//    public boolean IsToggled(Player p) {
//        return ModAbilities.TOGGLE_ATTACHMENTS.containsKey(this.getName()) && p.getData(ModAbilities.TOGGLE_ATTACHMENTS.get(this.getName()));
//    }
//
//    @Override
//    public void Toggle(Player p)
//    {
//        //Change the toggle to opposite and then tell the player
//        if(TOGGLE_ATTACHMENTS.containsKey(this.getName())) p.setData(TOGGLE_ATTACHMENTS.get(this.getName()), !IsToggled(p));
//        PacketDistributor.sendToPlayer((ServerPlayer) p, new ToggleState(this.getName().toString(), IsToggled(p)));
//    }
//
//}
