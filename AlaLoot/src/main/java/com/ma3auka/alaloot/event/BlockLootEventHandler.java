package com.ma3auka.alaloot.event;

import com.ma3auka.alaloot.AlaLoot;
import com.ma3auka.alaloot.config.AlaLootConfig;
import com.ma3auka.alaloot.util.BlockCategoryHelper;
import com.ma3auka.alaloot.util.RandomItemHelper;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

@EventBusSubscriber(modid = AlaLoot.MODID)
public final class BlockLootEventHandler {
    private static final int BLOCK_XP_REWARD = 1;

    private BlockLootEventHandler() {}

    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent event) {
        if (!AlaLootConfig.ENABLED.get()) return;
        Level level = event.getLevel();
        if (level.isClientSide()) return;
        if (!AlaLootConfig.ENABLE_BLOCK_DROP.get()) return;

        BlockState state = event.getState();
        Identifier blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        if (blockId != null && AlaLootConfig.BLOCK_BLACKLIST.get().contains(blockId.toString())) return;

        BlockCategoryHelper.Category cat = BlockCategoryHelper.classify(state);
        if (cat == BlockCategoryHelper.Category.NETHER && !AlaLootConfig.ENABLE_NETHER_BLOCKS.get()) return;
        if (cat == BlockCategoryHelper.Category.END && !AlaLootConfig.ENABLE_END_BLOCKS.get()) return;

        Entity breaker = event.getBreaker();
        if (!(breaker instanceof Player player)) return;

        event.getDrops().clear();

        if (MobLootEventHandler.rollChance(level, player)) {
            ItemStack stack = RandomItemHelper.randomStack(level.getRandom());
            if (!stack.isEmpty()) {
                var pos = event.getPos();
                ItemEntity ie = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
                ie.setDefaultPickUpDelay();
                event.getDrops().add(ie);
            }
        }

        player.giveExperiencePoints(BLOCK_XP_REWARD);
    }

}
