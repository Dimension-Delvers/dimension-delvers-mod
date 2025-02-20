package com.dimensiondelvers.dimensiondelvers.init;

import com.dimensiondelvers.dimensiondelvers.DimensionDelvers;
import com.dimensiondelvers.dimensiondelvers.entity.RiftEntranceEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntityTypes {
    public static final DeferredRegister.Entities ENTITIES = DeferredRegister.createEntities(DimensionDelvers.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<RiftEntranceEntity>> RIFT_ENTRANCE = ENTITIES.registerEntityType(
            "rift_entrance",
            RiftEntranceEntity::new,
            MobCategory.MISC,
            builder -> builder.sized(0.5f, 3f));

}
