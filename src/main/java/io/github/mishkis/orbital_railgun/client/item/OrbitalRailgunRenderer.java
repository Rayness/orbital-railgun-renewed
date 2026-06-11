package io.github.mishkis.orbital_railgun.client.item;

import io.github.mishkis.orbital_railgun.OrbitalRailgun;
import io.github.mishkis.orbital_railgun.item.OrbitalRailgunItem;
import net.minecraft.resources.Identifier;
import com.geckolib.model.DefaultedItemGeoModel;
import com.geckolib.renderer.GeoItemRenderer;

public class OrbitalRailgunRenderer extends GeoItemRenderer<OrbitalRailgunItem> {
    public OrbitalRailgunRenderer() {
        super(new DefaultedItemGeoModel<>(Identifier.fromNamespaceAndPath(OrbitalRailgun.MOD_ID, "orbital_railgun")));
    }
}
