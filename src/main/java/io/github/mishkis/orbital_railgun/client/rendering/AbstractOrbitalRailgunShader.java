package io.github.mishkis.orbital_railgun.client.rendering;

import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.logging.LogUtils;
import io.github.mishkis.orbital_railgun.client.mixin.PostChainAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import com.mojang.blaze3d.shaders.AbstractUniform;

/**
 * Re-implementation of the Satin-based shader handling on top of the vanilla
 * {@link PostChain} pipeline. The post chain reads the main framebuffer's depth
 * texture through the custom "DepthSampler" sampler, mirroring what Satin's
 * ReadableDepthFramebuffer used to provide on Fabric.
 */
public abstract class AbstractOrbitalRailgunShader {
    private static final Logger LOGGER = LogUtils.getLogger();

    protected final Minecraft client = Minecraft.getInstance();

    private PostChain postChain;
    private boolean loadFailed = false;
    private int lastWidth = -1;
    private int lastHeight = -1;

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

        PostChain chain = getOrLoadPostChain();
        if (chain == null) {
            return;
        }

        RenderTarget mainTarget = client.getMainRenderTarget();
        if (mainTarget.width != lastWidth || mainTarget.height != lastHeight) {
            lastWidth = mainTarget.width;
            lastHeight = mainTarget.height;
            chain.resize(mainTarget.width, mainTarget.height);
        }

        float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);

        Matrix4f inverseTransformMatrix = new Matrix4f(event.getProjectionMatrix()).mul(event.getModelViewMatrix()).invert();
        setUniform("InverseTransformMatrix", uniform -> uniform.set(inverseTransformMatrix));
        setUniform("CameraPosition", uniform -> uniform.set(event.getCamera().getPosition().toVector3f()));
        setUniform("iTime", uniform -> uniform.set((ticks + partialTick) / 20f));
        setExtraUniforms(partialTick);

        chain.process(partialTick);
        mainTarget.bindWrite(false);
    }

    protected final void setUniform(String name, Consumer<AbstractUniform> setter) {
        if (postChain == null) {
            return;
        }

        for (PostPass pass : ((PostChainAccessor) postChain).orbital_railgun$getPasses()) {
            setter.accept(pass.getEffect().safeGetUniform(name));
        }
    }

    private PostChain getOrLoadPostChain() {
        if (loadFailed) {
            return null;
        }

        if (postChain == null) {
            try {
                postChain = new PostChain(client.getTextureManager(), client.getResourceManager(), client.getMainRenderTarget(), getIdentifier());

                lastWidth = client.getMainRenderTarget().width;
                lastHeight = client.getMainRenderTarget().height;
                postChain.resize(lastWidth, lastHeight);

                List<PostPass> passes = ((PostChainAccessor) postChain).orbital_railgun$getPasses();
                for (PostPass pass : passes) {
                    pass.getEffect().setSampler("DepthSampler", () -> client.getMainRenderTarget().getDepthTextureId());
                }
            } catch (IOException | JsonSyntaxException e) {
                LOGGER.error("Failed to load orbital railgun post chain {}", getIdentifier(), e);
                loadFailed = true;
                postChain = null;
            }
        }

        return postChain;
    }
}
