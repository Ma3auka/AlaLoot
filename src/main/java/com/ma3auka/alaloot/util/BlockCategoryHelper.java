package com.ma3auka.alaloot.util;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;

public final class BlockCategoryHelper {
    public enum Category { EARTH_STONE, ORE, WOOD_NATURE, BUILDING, NETHER, END }

    private BlockCategoryHelper() {}

    public static Category classify(BlockState state) {
        Identifier id = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(state.getBlock());
        String path = id == null ? "" : id.getPath();

        // End first
        if (path.contains("end") || path.contains("chorus") || path.contains("purpur") || path.contains("shulker")) {
            return Category.END;
        }
        // Nether
        if (path.contains("nether") || path.contains("blaze") || path.contains("crimson")
                || path.contains("warped") || path.contains("basalt") || path.contains("soul")
                || path.contains("magma")) {
            return Category.NETHER;
        }
        // Ores
        if (state.is(BlockTags.COAL_ORES) || state.is(BlockTags.IRON_ORES) || state.is(BlockTags.GOLD_ORES)
                || state.is(BlockTags.DIAMOND_ORES) || state.is(BlockTags.EMERALD_ORES)
                || state.is(BlockTags.LAPIS_ORES) || state.is(BlockTags.REDSTONE_ORES)
                || state.is(BlockTags.COPPER_ORES) || path.endsWith("_ore")) {
            return Category.ORE;
        }
        // Wood/Nature
        if (state.is(BlockTags.LOGS) || state.is(BlockTags.LEAVES) || state.is(BlockTags.FLOWERS)
                || state.is(BlockTags.SAPLINGS) || state.is(BlockTags.CROPS)
                || path.contains("grass") || path.contains("mushroom") || path.contains("moss")
                || path.contains("vine") || path.contains("fern") || path.contains("kelp")) {
            return Category.WOOD_NATURE;
        }
        // Earth/Stone
        if (path.contains("dirt") || path.contains("sand") || path.contains("gravel")
                || path.contains("clay") || path.contains("stone") || path.contains("cobble")
                || path.contains("deepslate") || path.contains("granite") || path.contains("diorite")
                || path.contains("andesite") || path.contains("tuff") || path.contains("snow")
                || path.contains("ice")) {
            return Category.EARTH_STONE;
        }
        // Default: building
        return Category.BUILDING;
    }
}
