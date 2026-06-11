package io.github.mishkis.orbital_railgun.sound;

import io.github.mishkis.orbital_railgun.OrbitalRailgun;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class OrbitalRailgunSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, OrbitalRailgun.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> EQUIP = register("equip");
    public static final DeferredHolder<SoundEvent, SoundEvent> SCOPE_ON = register("scope_on");
    // Fixed range: the server must send the sound packet to everyone near the strike,
    // not just within the default 16-block radius of a volume-1 sound.
    public static final DeferredHolder<SoundEvent, SoundEvent> RAILGUN_SHOOT = SOUNDS.register("railgun_shoot",
            () -> SoundEvent.createFixedRangeEvent(ResourceLocation.fromNamespaceAndPath(OrbitalRailgun.MOD_ID, "railgun_shoot"), 500.0f));

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(OrbitalRailgun.MOD_ID, name)));
    }
}
