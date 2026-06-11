package io.github.mishkis.orbital_railgun.client.rendering;

import io.github.mishkis.orbital_railgun.OrbitalRailgun;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

public class OrbitalRailgunShader extends AbstractOrbitalRailgunShader {
    public static final ResourceLocation ORBITAL_RAILGUN_SHADER = ResourceLocation.fromNamespaceAndPath(OrbitalRailgun.MOD_ID, "orbital_railgun");
    public static final OrbitalRailgunShader INSTANCE = new OrbitalRailgunShader();

    public Vector3f BlockPosition = null;
    public ResourceKey<Level> Dimension = null;

    @Override
    protected ResourceLocation getIdentifier() {
        return ORBITAL_RAILGUN_SHADER;
    }

    @Override
    protected boolean shouldRender() {
        var level = client.level;
        return BlockPosition != null && level != null && level.dimension() == Dimension;
    }

    @Override
    protected void tickExtra() {
        // The pillar glow fades out ~25s after the explosion (~61s total); the original ran for 1600 ticks.
        if (ticks >= 1300 || client.level == null || client.level.dimension() != Dimension) {
            BlockPosition = null;
            Dimension = null;
        }
    }

    @Override
    protected void setExtraUniforms(float partialTick) {
        if (BlockPosition != null) {
            setUniform("BlockPosition", uniform -> uniform.set(BlockPosition));
        }
    }
}
