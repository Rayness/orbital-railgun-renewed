package io.github.mishkis.orbital_railgun.client.mixin;

import io.github.mishkis.orbital_railgun.client.rendering.OrbitalRailgunGuiShader;
import io.github.mishkis.orbital_railgun.client.rendering.OrbitalRailgunShader;
import io.github.mishkis.orbital_railgun.item.OrbitalRailgunItem;
import io.github.mishkis.orbital_railgun.network.ShootPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow @Final public Options options;

    @Shadow @Nullable public LocalPlayer player;

    @Shadow @Nullable public MultiPlayerGameMode gameMode;

    @Inject(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"))
    public void shootOnAttack(CallbackInfo ci) {
        if (player.getUseItem().getItem() instanceof OrbitalRailgunItem orbitalRailgun && this.options.keyAttack.isDown() && OrbitalRailgunShader.INSTANCE.BlockPosition == null) {
            HitResult hitResult = OrbitalRailgunGuiShader.INSTANCE.hitResult;
            if (hitResult != null && hitResult.getType() != HitResult.Type.MISS && hitResult instanceof BlockHitResult blockHitResult) {
                this.gameMode.releaseUsingItem(this.player);
                orbitalRailgun.shoot(this.player);
                OrbitalRailgunShader.INSTANCE.BlockPosition = blockHitResult.getBlockPos().getCenter().toVector3f();
                OrbitalRailgunShader.INSTANCE.Dimension = player.level().dimension();

                ClientPacketDistributor.sendToServer(new ShootPayload(blockHitResult.getBlockPos()));
            }
        }
    }
}
