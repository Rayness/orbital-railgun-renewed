package io.github.mishkis.orbital_railgun.client;

import io.github.mishkis.orbital_railgun.OrbitalRailgun;
import io.github.mishkis.orbital_railgun.client.rendering.AbstractOrbitalRailgunShader;
import io.github.mishkis.orbital_railgun.client.rendering.OrbitalRailgunGuiShader;
import io.github.mishkis.orbital_railgun.client.rendering.OrbitalRailgunShader;
import io.github.mishkis.orbital_railgun.item.OrbitalRailgunItem;
import io.github.mishkis.orbital_railgun.sound.OrbitalRailgunSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.List;

@Mod(value = OrbitalRailgun.MOD_ID, dist = Dist.CLIENT)
public class OrbitalRailgunClient {
    private Item lastMainHandItem = null;

    public OrbitalRailgunClient(IEventBus modEventBus) {
        for (AbstractOrbitalRailgunShader shader : List.of(OrbitalRailgunShader.INSTANCE, OrbitalRailgunGuiShader.INSTANCE)) {
            NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post event) -> shader.onClientTick(event));
            NeoForge.EVENT_BUS.addListener((RenderLevelStageEvent event) -> shader.onRenderLevelStage(event));
        }

        NeoForge.EVENT_BUS.addListener(this::playEquipSound);
    }

    private void playEquipSound(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            lastMainHandItem = null;
            return;
        }

        Item current = minecraft.player.getMainHandItem().getItem();
        if (current != lastMainHandItem) {
            if (current instanceof OrbitalRailgunItem) {
                minecraft.player.playSound(OrbitalRailgunSounds.EQUIP.get(), 1.0f, 1.0f);
            }
            lastMainHandItem = current;
        }
    }
}
