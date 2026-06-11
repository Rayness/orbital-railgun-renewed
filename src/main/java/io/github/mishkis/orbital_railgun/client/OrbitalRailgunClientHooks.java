package io.github.mishkis.orbital_railgun.client;

import io.github.mishkis.orbital_railgun.client.rendering.OrbitalRailgunShader;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

public class OrbitalRailgunClientHooks {
    public static void onStrikeSync(BlockPos blockPos) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }

        OrbitalRailgunShader.INSTANCE.BlockPosition = blockPos.getCenter().toVector3f();
        OrbitalRailgunShader.INSTANCE.Dimension = minecraft.level.dimension();
    }
}
