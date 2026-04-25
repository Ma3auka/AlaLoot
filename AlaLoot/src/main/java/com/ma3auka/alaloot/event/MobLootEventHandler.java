package com.ma3auka.alaloot.event;

import com.ma3auka.alaloot.AlaLoot;
import com.ma3auka.alaloot.config.AlaLootConfig;
import com.ma3auka.alaloot.util.RandomItemHelper;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

@EventBusSubscriber(modid = AlaLoot.MODID)
public final class MobLootEventHandler {
    private static final int MOB_XP_REWARD = 10;

    private MobLootEventHandler() {}

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (!AlaLootConfig.ENABLED.get()) return;
        LivingEntity entity = event.getEntity();
        Level level = entity.level();
        if (level.isClientSide()) return;

        if (!(event.getSource().getEntity() instanceof Player player)) return;

        if (BossLootEventHandler.tryHandle(event)) {
            grantXp(player);
            return;
        }

        if (!AlaLootConfig.ENABLE_MOB_DROP.get()) return;

        Identifier mobId = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        if (mobId != null && AlaLootConfig.MOB_BLACKLIST.get().contains(mobId.toString())) return;

        event.getDrops().clear();

        if (rollChance(level, player)) {
            ItemStack stack = RandomItemHelper.randomStack(level.getRandom());
            if (!stack.isEmpty()) {
                ItemEntity ie = new ItemEntity(level, entity.getX(), entity.getY() + 0.25, entity.getZ(), stack);
                ie.setDefaultPickUpDelay();
                event.getDrops().add(ie);
            }
        }

        grantXp(player);
    }

    private static void grantXp(Player player) {
        if (player != null) player.giveExperiencePoints(MOB_XP_REWARD);
    }

    static boolean rollChance(Level level, Player player) {
        if (player == null) return false;
        int xpLevel = player.experienceLevel;
        if (xpLevel <= 0) return false;
        double chance = Math.min(xpLevel, 100) / 100.0;
        return level.getRandom().nextDouble() < chance;
    }

    public static double currentChance(Player player) {
        if (player == null) return 0.0;
        return Math.min(player.experienceLevel, 100) / 100.0;
    }
}
