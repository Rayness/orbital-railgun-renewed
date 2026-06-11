package io.github.mishkis.orbital_railgun;

import io.github.mishkis.orbital_railgun.item.OrbitalRailgunItems;
import io.github.mishkis.orbital_railgun.network.OrbitalRailgunNetwork;
import io.github.mishkis.orbital_railgun.sound.OrbitalRailgunSounds;
import io.github.mishkis.orbital_railgun.util.OrbitalRailgunStrikeManager;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@Mod(OrbitalRailgun.MOD_ID)
public class OrbitalRailgun {
    public static final String MOD_ID = "orbital_railgun";

    public OrbitalRailgun(IEventBus modEventBus) {
        OrbitalRailgunItems.ITEMS.register(modEventBus);
        OrbitalRailgunSounds.SOUNDS.register(modEventBus);
        OrbitalRailgunStrikeManager.initialize();

        modEventBus.addListener(OrbitalRailgunNetwork::register);
        modEventBus.addListener(OrbitalRailgunItems::addToCreativeTab);

        NeoForge.EVENT_BUS.addListener((ServerTickEvent.Post event) -> OrbitalRailgunStrikeManager.tick(event.getServer()));
    }
}
