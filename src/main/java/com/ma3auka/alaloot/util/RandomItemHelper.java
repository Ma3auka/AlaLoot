package com.ma3auka.alaloot.util;

import java.util.ArrayList;
import java.util.List;

import com.ma3auka.alaloot.AlaLoot;
import com.ma3auka.alaloot.config.AlaLootConfig;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class RandomItemHelper {
    private static final List<Item> POOL = new ArrayList<>();
    private static volatile boolean initialized = false;

    private RandomItemHelper() {}

    public static synchronized void invalidate() {
        initialized = false;
        POOL.clear();
    }

    private static synchronized void init() {
        if (initialized) return;
        List<? extends String> itemBlacklist = AlaLootConfig.ITEM_DROP_BLACKLIST.get();
        List<? extends String> modBlacklist = AlaLootConfig.MOD_BLACKLIST.get();
        int total = 0, skipped = 0;
        for (Item item : BuiltInRegistries.ITEM) {
            if (item == Items.AIR) continue;
            total++;
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
            if (id != null) {
                if (itemBlacklist.contains(id.toString())) { skipped++; continue; }
                if (modBlacklist.contains(id.getNamespace())) { skipped++; continue; }
            }
            POOL.add(item);
        }
        AlaLoot.LOGGER.info("AlaLoot pool built: {} items, {} skipped by blacklists", total - skipped, skipped);
        initialized = true;
    }

    public static ItemStack randomStack(RandomSource random) {
        if (!initialized) init();
        if (POOL.isEmpty()) return ItemStack.EMPTY;
        return new ItemStack(POOL.get(random.nextInt(POOL.size())));
    }

    public static int poolSize() {
        if (!initialized) init();
        return POOL.size();
    }
}
