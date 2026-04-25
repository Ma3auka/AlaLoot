package com.ma3auka.alaloot;

import org.slf4j.Logger;

import com.ma3auka.alaloot.config.AlaLootConfig;
import com.ma3auka.alaloot.event.CacheLifecycleHandler;
import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;

@Mod(AlaLoot.MODID)
public class AlaLoot {
    public static final String MODID = "alaloot";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AlaLoot(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.SERVER, AlaLootConfig.SPEC);

        modEventBus.addListener(CacheLifecycleHandler::onConfigLoaded);
        modEventBus.addListener(CacheLifecycleHandler::onConfigReloaded);
        NeoForge.EVENT_BUS.addListener(CacheLifecycleHandler::onServerStarting);

        LOGGER.info("AlaLoot initialized");
    }
}
