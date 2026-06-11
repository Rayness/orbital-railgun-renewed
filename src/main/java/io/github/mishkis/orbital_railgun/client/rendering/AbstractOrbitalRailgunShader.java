package io.github.mishkis.orbital_railgun.client.rendering;

import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Set;

/**
 * Re-implementation of the Satin-based shader handling on top of the vanilla
 * post-effect pipeline (1.21.5+ format). Custom uniforms are pushed through the
 * RenderPass consumer hook of PostChain#process; the depth buffer of the main
 * render target is bound declaratively through the post_effect JSON.
 */
public abstract class AbstractOrbitalRailgunShader {
    private static final Set<ResourceLocation> EXTERNAL_TARGETS = Set.of(PostChain.MAIN_TARGET_ID);

    // Lazily initialized: on 1.21.5+ mods are constructed before the Minecraft instance exists.
    protected Minecraft client;

    protected int ticks = 0;

    protected abstract ResourceLocation getIdentifier();

    protected abstract boolean shouldRender();

    /** Hook matching the subclass tick overrides of the original mod. */
    protected void tickExtra() {}

    /** Compute per-frame values (e.g. raycasts) before the chain runs. */
    protected void prepareExtraUniforms(float partialTick) {}

    /** Push subclass uniforms onto each render pass of the chain. */
    protected void applyExtraUniforms(RenderPass renderPass) {}

    public final void onClientTick(ClientTickEvent.Post event) {
        if (client == null) {
            client = Minecraft.getInstance();
        }

        tickExtra();

        if (shouldRender()) {
            ticks++;
        } else {
            ticks = 0;
        }
    }

    public final void onRenderLevelStage(RenderLevelStageEvent event) {
        if (client == null) {
            client = Minecraft.getInstance();
        }

        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL || !shouldRender()) {
            return;
        }

        PostChain chain = client.getShaderManager().getPostChain(getIdentifier(), EXTERNAL_TARGETS);
        if (chain == null) {
            return;
        }

        float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);

        // The AFTER_LEVEL event does not carry the real model-view matrix, so
        // rebuild it from the camera rotation the same way vanilla does.
        Matrix4f modelViewMatrix = new Matrix4f().rotation(event.getCamera().rotation().conjugate(new Quaternionf()));
        Matrix4f inverseTransformMatrix = new Matrix4f(event.getProjectionMatrix()).mul(modelViewMatrix).invert();
        Vector3f cameraPosition = event.getCamera().getPosition().toVector3f();
        float time = (ticks + partialTick) / 20f;

        prepareExtraUniforms(partialTick);

        RenderSystem.resetTextureMatrix();

        chain.process(client.getMainRenderTarget(), GraphicsResourceAllocator.UNPOOLED, renderPass -> {
            renderPass.setUniform("InverseTransformMatrix", inverseTransformMatrix);
            renderPass.setUniform("CameraPosition", cameraPosition.x, cameraPosition.y, cameraPosition.z);
            renderPass.setUniform("iTime", time);
            applyExtraUniforms(renderPass);
        });
    }
}
