package io.github.mishkis.orbital_railgun.client.mixin;

import io.github.mishkis.orbital_railgun.client.rendering.OrbitalRailgunMatrices;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    // Since 26.1 the projection matrix is no longer passed to LevelRenderer.renderLevel;
    // GameRenderer uploads it (with view bobbing applied) straight into a UBO. Intercept
    // that upload to keep a CPU-side copy for the post shader's inverse transform.
    @ModifyArg(method = "renderLevel(Lnet/minecraft/client/DeltaTracker;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ProjectionMatrixBuffer;getBuffer(Lorg/joml/Matrix4f;)Lcom/mojang/blaze3d/buffers/GpuBufferSlice;"))
    private Matrix4f orbital_railgun$captureProjectionMatrix(Matrix4f projectionMatrix) {
        OrbitalRailgunMatrices.PROJECTION.set(projectionMatrix);
        return projectionMatrix;
    }
}
