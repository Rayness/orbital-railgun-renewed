package io.github.mishkis.orbital_railgun.network;

import io.github.mishkis.orbital_railgun.OrbitalRailgun;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ShootPayload(BlockPos pos) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ShootPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(OrbitalRailgun.MOD_ID, "shoot_packet"));

    public static final StreamCodec<ByteBuf, ShootPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ShootPayload::pos,
            ShootPayload::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
