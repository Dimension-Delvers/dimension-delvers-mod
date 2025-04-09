package com.wanderersoftherift.wotr.network;

import com.wanderersoftherift.wotr.WanderersOfTheRift;
import com.wanderersoftherift.wotr.gui.menu.SkillBenchMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Request to unlock a level of an ability
 * @param level the level to unlock
 */
public record LevelUpAbilityPayload(int level) implements CustomPacketPayload {
    public static final Type<LevelUpAbilityPayload> TYPE = new Type<>(WanderersOfTheRift.id("level_up_ability"));

    public static final StreamCodec<ByteBuf, LevelUpAbilityPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, LevelUpAbilityPayload::level,
            LevelUpAbilityPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handleOnServer(IPayloadContext context) {
        if (context.player().containerMenu instanceof SkillBenchMenu menu && menu.stillValid(context.player())) {
            menu.levelUp(level);
        }
    }
}
