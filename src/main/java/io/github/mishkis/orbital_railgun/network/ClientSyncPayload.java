package io.github.mishkis.orbital_railgun.network;

import io.github.mishkis.orbital_railgun.OrbitalRailgun;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ClientSyncPayload(BlockPos pos) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientSyncPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(OrbitalRailgun.MOD_ID, "client_synch_packet"));

    public static final StreamCodec<ByteBuf, ClientSyncPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ClientSyncPayload::pos,
            ClientSyncPayload::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
