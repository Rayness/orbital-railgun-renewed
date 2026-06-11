package io.github.mishkis.orbital_railgun.network;

import io.github.mishkis.orbital_railgun.client.OrbitalRailgunClientHooks;
import io.github.mishkis.orbital_railgun.item.OrbitalRailgunItems;
import io.github.mishkis.orbital_railgun.sound.OrbitalRailgunSounds;
import io.github.mishkis.orbital_railgun.util.OrbitalRailgunStrikeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.List;

public class OrbitalRailgunNetwork {
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(ShootPayload.TYPE, ShootPayload.STREAM_CODEC, OrbitalRailgunNetwork::handleShoot);
        registrar.playToClient(ClientSyncPayload.TYPE, ClientSyncPayload.STREAM_CODEC, OrbitalRailgunNetwork::handleClientSync);
    }

    private static void handleShoot(ShootPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer serverPlayer)) {
                return;
            }

            BlockPos blockPos = payload.pos();

            OrbitalRailgunItems.ORBITAL_RAILGUN.get().shoot(serverPlayer);

            serverPlayer.level().playSound(null, blockPos, OrbitalRailgunSounds.RAILGUN_SHOOT.get(), SoundSource.PLAYERS, 1.0f, 1.0f);

            List<Entity> nearby = serverPlayer.level().getEntities(serverPlayer, AABB.ofSize(blockPos.getCenter(), 500., 500., 500.));
            nearby.add(serverPlayer);
            OrbitalRailgunStrikeManager.activeStrikes.add(new OrbitalRailgunStrikeManager.Strike(blockPos, nearby, serverPlayer.server.getTickCount(), serverPlayer.level().dimension()));

            nearby.forEach(entity -> {
                if (entity instanceof ServerPlayer otherPlayer) {
                    PacketDistributor.sendToPlayer(otherPlayer, new ClientSyncPayload(blockPos));
                }
            });
        });
    }

    private static void handleClientSync(ClientSyncPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> OrbitalRailgunClientHooks.onStrikeSync(payload.pos()));
    }
}
