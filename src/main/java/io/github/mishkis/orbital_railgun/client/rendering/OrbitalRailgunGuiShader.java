package io.github.mishkis.orbital_railgun.client.rendering;

import io.github.mishkis.orbital_railgun.OrbitalRailgun;
import io.github.mishkis.orbital_railgun.item.OrbitalRailgunItem;
import io.github.mishkis.orbital_railgun.sound.OrbitalRailgunSounds;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.joml.Vector3f;

public class OrbitalRailgunGuiShader extends AbstractOrbitalRailgunShader {
    public static final ResourceLocation ORBITAL_RAILGUN_GUI_SHADER = ResourceLocation.fromNamespaceAndPath(OrbitalRailgun.MOD_ID, "orbital_railgun_gui");
    public static final OrbitalRailgunGuiShader INSTANCE = new OrbitalRailgunGuiShader();

    public HitResult hitResult;

    private float isBlockHit = 0f;
    private Vector3f hitPosition = new Vector3f();

    @Override
    protected ResourceLocation getIdentifier() {
        return ORBITAL_RAILGUN_GUI_SHADER;
    }

    @Override
    protected boolean shouldRender() {
        return client != null && client.player != null && client.player.getUseItem().getItem() instanceof OrbitalRailgunItem;
    }

    @Override
    protected void tickExtra() {
        // is it jank to disable the hud rendering here? yeah kinda
        if (shouldRender()) {
            if (ticks == 0) {
                client.getSoundManager().play(SimpleSoundInstance.forUI(OrbitalRailgunSounds.SCOPE_ON.get(), 1.0f));
            }
            this.client.options.hideGui = true;
        } else if (ticks != 0) {
            this.client.options.hideGui = false;
        }
    }

    @Override
    protected void prepareExtraUniforms(float partialTick) {
        hitResult = client.player.pick(300f, partialTick, false);
        switch (hitResult.getType()) {
            case BLOCK:
                isBlockHit = 1f;
                hitPosition = ((BlockHitResult) hitResult).getBlockPos().getCenter().toVector3f();
                break;
            case ENTITY:
                isBlockHit = 1f;
                hitPosition = ((EntityHitResult) hitResult).getEntity().blockPosition().getCenter().toVector3f();
                break;
            case MISS:
                isBlockHit = 0f;
                break;
        }
    }

    @Override
    protected Vector3f getBlockPositionUniform() {
        return hitPosition;
    }

    @Override
    protected float getIsBlockHitUniform() {
        return isBlockHit;
    }
}
