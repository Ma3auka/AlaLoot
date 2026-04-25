package com.ma3auka.alaloot.config;

import java.util.List;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class AlaLootConfig {
    private static final ModConfigSpec.Builder B = new ModConfigSpec.Builder();

    // [general]
    public static final ModConfigSpec.BooleanValue ENABLED;

    // [mobs]
    public static final ModConfigSpec.BooleanValue ENABLE_MOB_DROP;

    // [blocks]
    public static final ModConfigSpec.BooleanValue ENABLE_BLOCK_DROP;
    public static final ModConfigSpec.BooleanValue ENABLE_NETHER_BLOCKS;
    public static final ModConfigSpec.BooleanValue ENABLE_END_BLOCKS;

    // [bosses]
    public static final ModConfigSpec.BooleanValue ENABLE_BOSS_DROPS;
    public static final ModConfigSpec.IntValue DRAGON_EGG_STACK_SIZE;
    public static final ModConfigSpec.IntValue WITHER_STAR_STACK_SIZE;
    public static final ModConfigSpec.IntValue WARDEN_DIAMOND_STACK_SIZE;
    public static final ModConfigSpec.IntValue ELDER_GUARDIAN_SPONGE_STACK_SIZE;
    public static final ModConfigSpec.BooleanValue ENABLE_DRAGON_DROP;
    public static final ModConfigSpec.BooleanValue ENABLE_WITHER_DROP;
    public static final ModConfigSpec.BooleanValue ENABLE_WARDEN_DROP;
    public static final ModConfigSpec.BooleanValue ENABLE_ELDER_GUARDIAN_DROP;

    // [blacklist]
    public static final ModConfigSpec.ConfigValue<List<? extends String>> MOB_BLACKLIST;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> BLOCK_BLACKLIST;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_DROP_BLACKLIST;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> MOD_BLACKLIST;

    static {
        B.push("general");
        ENABLED = B.comment("Enable the AlaLoot mod globally").define("enabled", true);
        B.pop();

        B.comment("Drop chance equals player's XP level (capped at 100). Level 0 = 0%, level 50 = 50%, level 100+ = 100%.");

        B.push("mobs");
        ENABLE_MOB_DROP = B.define("enableMobDrop", true);
        B.pop();

        B.push("blocks");
        ENABLE_BLOCK_DROP = B.define("enableBlockDrop", true);
        ENABLE_NETHER_BLOCKS = B.define("enableNetherBlocks", true);
        ENABLE_END_BLOCKS = B.define("enableEndBlocks", true);
        B.pop();

        B.push("bosses");
        ENABLE_BOSS_DROPS = B.define("enableBossDrops", true);
        DRAGON_EGG_STACK_SIZE = B.defineInRange("dragonEggStackSize", 64, 1, 64);
        WITHER_STAR_STACK_SIZE = B.defineInRange("witherStarStackSize", 64, 1, 64);
        WARDEN_DIAMOND_STACK_SIZE = B.defineInRange("wardenDiamondStackSize", 64, 1, 64);
        ELDER_GUARDIAN_SPONGE_STACK_SIZE = B.defineInRange("elderGuardianSpongeStackSize", 64, 1, 64);
        ENABLE_DRAGON_DROP = B.define("enableDragonDrop", true);
        ENABLE_WITHER_DROP = B.define("enableWitherDrop", true);
        ENABLE_WARDEN_DROP = B.define("enableWardenDrop", true);
        ENABLE_ELDER_GUARDIAN_DROP = B.define("enableElderGuardianDrop", true);
        B.pop();

        B.push("blacklist");
        MOB_BLACKLIST = B.defineListAllowEmpty("mobBlacklist", List.of(),
                () -> "minecraft:bat", AlaLootConfig::isString);
        BLOCK_BLACKLIST = B.defineListAllowEmpty("blockBlacklist",
                List.of("minecraft:bedrock", "minecraft:barrier", "minecraft:command_block"),
                () -> "minecraft:bedrock", AlaLootConfig::isString);
        ITEM_DROP_BLACKLIST = B.defineListAllowEmpty("itemDropBlacklist",
                List.of("minecraft:command_block", "minecraft:structure_block", "minecraft:command_block_minecart"),
                () -> "minecraft:command_block", AlaLootConfig::isString);
        MOD_BLACKLIST = B.comment("Whole mods to exclude from random drops (by mod id)")
                .defineListAllowEmpty("modBlacklist", List.of(), () -> "examplemod", AlaLootConfig::isString);
        B.pop();
    }

    public static final ModConfigSpec SPEC = B.build();

    private AlaLootConfig() {}

    private static boolean isString(Object o) {
        return o instanceof String;
    }
}
