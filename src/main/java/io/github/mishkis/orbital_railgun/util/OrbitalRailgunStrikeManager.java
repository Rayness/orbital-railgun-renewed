package io.github.mishkis.orbital_railgun.util;

import io.github.mishkis.orbital_railgun.OrbitalRailgun;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2i;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OrbitalRailgunStrikeManager {
    public record Strike(BlockPos pos, List<Entity> entities, int startTick, ResourceKey<Level> dimension) {}

    public static final Queue<Strike> activeStrikes = new ConcurrentLinkedQueue<>();
    private static final ResourceKey<DamageType> STRIKE_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(OrbitalRailgun.MOD_ID, "strike"));
    private static final int RADIUS = 24;
    private static final int RADIUS_SQUARED = RADIUS * RADIUS;
    private static final boolean[][] mask = new boolean[RADIUS * 2 + 1][RADIUS * 2 + 1];

    public static void tick(MinecraftServer server) {
        Iterator<Strike> iterator = activeStrikes.iterator();
        while (iterator.hasNext()) {
            Strike strike = iterator.next();
            float age = server.getTickCount() - strike.startTick();
            BlockPos blockPos = strike.pos();
            ResourceKey<Level> dimension = strike.dimension();

            if (age >= 700) {
                iterator.remove();

                ServerLevel level = server.getLevel(dimension);
                if (level == null) {
                    continue;
                }

                DamageSource damageSource = new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(STRIKE_DAMAGE));
                strike.entities().forEach(entity -> {
                    if (entity.level().dimension() == dimension && entity.position().subtract(blockPos.getCenter()).lengthSqr() <= RADIUS_SQUARED) {
                        entity.hurt(damageSource, 100000f);
                    }
                });

                explode(blockPos, level);
            } else if (age >= 400) {
                strike.entities().forEach(entity -> {
                    if (entity instanceof Player player && player.isSpectator()) {
                        return;
                    }
                    if (entity.level().dimension() == dimension) {
                        Vec3 dir = blockPos.getCenter().subtract(entity.position());
                        double mag = Math.min(1. / Math.abs(dir.length() - 20.) * 4. * (age - 400.) / 300., 5.);
                        dir = dir.normalize();

                        entity.push(dir.x * mag, dir.y * mag, dir.z * mag);
                        entity.hurtMarked = true;
                    }
                });
            }
        }
    }

    private static void explode(BlockPos origin, Level level) {
        for (int y = level.getMinBuildHeight(); y <= level.getMaxBuildHeight(); y++) {
            for (int x = -RADIUS; x <= RADIUS; x++) {
                for (int z = -RADIUS; z <= RADIUS; z++) {
                    if (mask[x + RADIUS][z + RADIUS]) {
                        level.setBlockAndUpdate(new BlockPos(origin.getX() + x, y, origin.getZ() + z), Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
    }

    public static void initialize() {
        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int z = -RADIUS; z <= RADIUS; z++) {
                mask[x + RADIUS][z + RADIUS] = Vector2i.lengthSquared(x, z) <= RADIUS_SQUARED;
            }
        }
    }
}
