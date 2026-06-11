package io.github.mishkis.orbital_railgun.item;

import com.mojang.logging.LogUtils;
import io.github.mishkis.orbital_railgun.OrbitalRailgun;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

public class OrbitalRailgunItems {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(OrbitalRailgun.MOD_ID);

    public static final DeferredItem<OrbitalRailgunItem> ORBITAL_RAILGUN = ITEMS.registerItem("orbital_railgun", OrbitalRailgunItem::new);

    public static void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            try {
                event.insertAfter(Items.CROSSBOW.getDefaultInstance(), ORBITAL_RAILGUN.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            } catch (IllegalArgumentException e) {
                LOGGER.warn("Could not insert the orbital railgun after the crossbow, appending to the combat tab instead", e);
                event.accept(ORBITAL_RAILGUN.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
            LOGGER.info("Added the orbital railgun to the combat creative tab");
        }
    }
}
