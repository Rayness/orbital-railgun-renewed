package io.github.mishkis.orbital_railgun.client.item;

import io.github.mishkis.orbital_railgun.OrbitalRailgun;
import io.github.mishkis.orbital_railgun.item.OrbitalRailgunItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class OrbitalRailgunRenderer extends GeoItemRenderer<OrbitalRailgunItem> {
    public OrbitalRailgunRenderer() {
        super(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(OrbitalRailgun.MOD_ID, "orbital_railgun")));
    }
}
