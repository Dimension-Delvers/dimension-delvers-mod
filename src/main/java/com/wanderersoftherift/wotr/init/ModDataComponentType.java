package com.wanderersoftherift.wotr.init;

import com.mojang.serialization.Codec;
import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.abilities.AbstractAbility;
import com.wanderersoftherift.wotr.item.runegem.RunegemData;
import com.wanderersoftherift.wotr.item.skillgem.UpgradePool;
import com.wanderersoftherift.wotr.item.socket.GearSockets;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.UUID;

public class ModDataComponentType {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, WanderersOfTheRift.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GearSockets>> GEAR_SOCKETS  = register("gear_sockets", GearSockets.CODEC, null);//, GearSockets.STREAM_CODEC);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<RunegemData>> RUNEGEM_DATA  = register("runegem_data", RunegemData.CODEC, null);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> INVENTORY_SNAPSHOT_ID = register("inventory_snapshot_id", UUIDUtil.CODEC, null);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UpgradePool>> UPGRADE_POOL = register("upgrade_pool", UpgradePool.CODEC, UpgradePool.STREAM_CODEC);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Holder<AbstractAbility>>> ABILITY = register("ability", AbstractAbility.CODEC, AbstractAbility.STREAM_CODEC);

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, final Codec<T> codec, @Nullable final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        if (streamCodec == null) {
            return DATA_COMPONENTS.register(name, () -> DataComponentType.<T>builder().persistent(codec).build());
        } else {
            return DATA_COMPONENTS.register(name, () -> DataComponentType.<T>builder().persistent(codec).networkSynchronized(streamCodec).build());
        }
    }
}
