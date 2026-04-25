package com.ma3auka.alaloot.event;

import com.ma3auka.alaloot.AlaLoot;
import com.ma3auka.alaloot.util.RandomItemHelper;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

public final class CacheLifecycleHandler {
    private CacheLifecycleHandler() {}

    public static void onConfigReloaded(ModConfigEvent.Reloading event) {
        AlaLoot.LOGGER.info("AlaLoot config reloaded — invalidating pool");
        RandomItemHelper.invalidate();
    }

    public static void onConfigLoaded(ModConfigEvent.Loading event) {
        RandomItemHelper.invalidate();
    }

    public static void onServerStarting(ServerStartingEvent event) {
        AlaLoot.LOGGER.info("AlaLoot: server starting — rebuilding pool");
        RandomItemHelper.invalidate();
    }
}
