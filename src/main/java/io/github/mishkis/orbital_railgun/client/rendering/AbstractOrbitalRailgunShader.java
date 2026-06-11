package io.github.mishkis.orbital_railgun.client.rendering;

import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.shaders.AbstractUniform;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.mishkis.orbital_railgun.client.mixin.PostChainAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.Set;
import java.util.function.Consumer;

/**
 * Re-implementation of the Satin-based shader handling on top of the vanilla
 * post-effect pipeline (1.21.2+ format). The depth buffer of the main render
 * target is bound declaratively through the post_effect JSON ("use_depth_buffer"),
 * mirroring what Satin's ReadableDepthFramebuffer used to provide on Fabric.
 */
public abstract class AbstractOrbitalRailgunShader {
    private static final Set<ResourceLocation> EXTERNAL_TARGETS = Set.of(PostChain.MAIN_TARGET_ID);

    protected final Minecraft client = Minecraft.getInstance();

    private PostChain activeChain;

    protected int ticks = 0;

    protected abstract ResourceLocation getIdentifier();

    protected abstract boolean shouldRender();

    /** Hook matching the subclass tick overrides of the original mod. */
    protected void tickExtra() {}

    /** Hook for subclasses to push their additional uniforms before the chain runs. */
    protected void setExtraUniforms(float partialTick) {}

    public final void onClientTick(ClientTickEvent.Post event) {
        tickExtra();

        if (shouldRender()) {
            ticks++;
        } else {
            ticks = 0;
        }
    }

    public final void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL || !shouldRender()) {
            return;
        }

        PostChain chain = client.getShaderManager().getPostChain(getIdentifier(), EXTERNAL_TARGETS);
        if (chain == null) {
            return;
        }

        this.activeChain = chain;

        float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);

        // The AFTER_LEVEL event does not carry the real model-view matrix on this
        // version, so rebuild it from the camera rotation the same way vanilla does.
        Matrix4f modelViewMatrix = new Matrix4f().rotation(event.getCamera().rotation().conjugate(new Quaternionf()));
        Matrix4f inverseTransformMatrix = new Matrix4f(event.getProjectionMatrix()).mul(modelViewMatrix).invert();
        setUniform("InverseTransformMatrix", uniform -> uniform.set(inverseTransformMatrix));
        setUniform("CameraPosition", uniform -> uniform.set(event.getCamera().getPosition().toVector3f()));
        setUniform("iTime", uniform -> uniform.set((ticks + partialTick) / 20f));
        setExtraUniforms(partialTick);

        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.resetTextureMatrix();

        chain.process(client.getMainRenderTarget(), GraphicsResourceAllocator.UNPOOLED);
        client.getMainRenderTarget().bindWrite(true);
    }

    protected final void setUniform(String name, Consumer<AbstractUniform> setter) {
        if (activeChain == null) {
            return;
        }

        for (PostPass pass : ((PostChainAccessor) activeChain).orbital_railgun$getPasses()) {
            setter.accept(pass.getShader().safeGetUniform(name));
        }
    }
}
