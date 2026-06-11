package io.github.mishkis.orbital_railgun.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.mishkis.orbital_railgun.item.OrbitalRailgunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Shadow @Final private Minecraft minecraft;

    @ModifyExpressionValue(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isScoping()Z"))
    public boolean smoothCursorOnAim(boolean original) {
        return original || this.minecraft.player.getUseItem().getItem() instanceof OrbitalRailgunItem;
    }
}
