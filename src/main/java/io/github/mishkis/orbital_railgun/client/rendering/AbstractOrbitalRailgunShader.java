package io.github.mishkis.orbital_railgun.client.rendering;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.mishkis.orbital_railgun.client.mixin.PostChainAccessor;
import io.github.mishkis.orbital_railgun.client.mixin.PostPassAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.util.Map;
import java.util.Set;

/**
 * Re-implementation of the Satin-based shader handling on top of the vanilla
 * post-effect pipeline (1.21.6+ format). Custom uniforms live in the std140
 * "RailgunConfig" uniform block declared in the post_effect JSON; its GPU
 * buffer is rebuilt each frame before the chain is processed.
 */
public abstract class AbstractOrbitalRailgunShader {
    private static final Set<ResourceLocation> EXTERNAL_TARGETS = Set.of(PostChain.MAIN_TARGET_ID);
    private static final String UNIFORM_BLOCK = "RailgunConfig";
    // Field order matters: Std140Builder always pads a vec3 out to 16 bytes,
    // while GLSL packs a following scalar into the vec3's tail. Scalars
    // therefore go before the vectors so both layouts agree.
    private static final int UNIFORM_BLOCK_SIZE = new Std140SizeCalculator()
            .putMat4f()
            .putFloat()
            .putFloat()
            .putVec3()
            .putVec3()
            .get();
    private static final Vector3f ZERO = new Vector3f();

    // Lazily initialized: mods are constructed before the Minecraft instance exists.
    protected Minecraft client;

    protected int ticks = 0;

    protected abstract ResourceLocation getIdentifier();

    protected abstract boolean shouldRender();

    /** Hook matching the subclass tick overrides of the original mod. */
    protected void tickExtra() {}

    /** Compute per-frame values (e.g. raycasts) before the chain runs. */
    protected void prepareExtraUniforms(float partialTick) {}

    /** The BlockPosition value pushed into the uniform block. */
    protected Vector3f getBlockPositionUniform() {
        return ZERO;
    }

    /** The IsBlockHit value pushed into the uniform block. */
    protected float getIsBlockHitUniform() {
        return 0f;
    }

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

    public final void onRenderLevelStage(RenderLevelStageEvent.AfterLevel event) {
        if (client == null) {
            client = Minecraft.getInstance();
        }

        if (!shouldRender()) {
            return;
        }

        PostChain chain = client.getShaderManager().getPostChain(getIdentifier(), EXTERNAL_TARGETS);
        if (chain == null) {
            return;
        }

        float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);

        prepareExtraUniforms(partialTick);

        Matrix4f inverseTransformMatrix = new Matrix4f(OrbitalRailgunMatrices.PROJECTION).mul(event.getModelViewMatrix()).invert();
        Vector3f cameraPosition = event.getCamera().getPosition().toVector3f();
        Vector3f blockPosition = getBlockPositionUniform();
        float time = (ticks + partialTick) / 20f;
        float isBlockHit = getIsBlockHitUniform();

        for (PostPass pass : ((PostChainAccessor) chain).orbital_railgun$getPasses()) {
            Map<String, GpuBuffer> customUniforms = ((PostPassAccessor) pass).orbital_railgun$getCustomUniforms();
            GpuBuffer previous = customUniforms.get(UNIFORM_BLOCK);
            if (previous == null) {
                // e.g. the final blit pass, which has no RailgunConfig block
                continue;
            }

            try (MemoryStack memoryStack = MemoryStack.stackPush()) {
                Std140Builder builder = Std140Builder.onStack(memoryStack, UNIFORM_BLOCK_SIZE);
                builder.putMat4f(inverseTransformMatrix);
                builder.putFloat(time);
                builder.putFloat(isBlockHit);
                builder.putVec3(cameraPosition);
                builder.putVec3(blockPosition);

                customUniforms.put(UNIFORM_BLOCK, RenderSystem.getDevice().createBuffer(() -> "orbital_railgun " + UNIFORM_BLOCK, GpuBuffer.USAGE_UNIFORM, builder.get()));
            }

            previous.close();
        }

        chain.process(client.getMainRenderTarget(), GraphicsResourceAllocator.UNPOOLED);
    }
}
