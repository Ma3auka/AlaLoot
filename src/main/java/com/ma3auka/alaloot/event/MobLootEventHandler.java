package com.ma3auka.alaloot.event;

import com.ma3auka.alaloot.AlaLoot;
import com.ma3auka.alaloot.config.AlaLootConfig;
import com.ma3auka.alaloot.util.RandomItemHelper;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

@EventBusSubscriber(modid = AlaLoot.MODID)
public final class MobLootEventHandler {
    private static final int MOB_XP_REWARD = 30;
    private static final double CURSE_CHANCE = 0.05;

    private static final Item[] CURSE_TOOLS = {
        Items.WOODEN_SWORD,   Items.WOODEN_PICKAXE,   Items.WOODEN_AXE,   Items.WOODEN_SHOVEL,   Items.WOODEN_HOE,
        Items.STONE_SWORD,    Items.STONE_PICKAXE,    Items.STONE_AXE,    Items.STONE_SHOVEL,    Items.STONE_HOE,
        Items.IRON_SWORD,     Items.IRON_PICKAXE,     Items.IRON_AXE,     Items.IRON_SHOVEL,     Items.IRON_HOE,
        Items.GOLDEN_SWORD,   Items.GOLDEN_PICKAXE,   Items.GOLDEN_AXE,   Items.GOLDEN_SHOVEL,   Items.GOLDEN_HOE,
        Items.DIAMOND_SWORD,  Items.DIAMOND_PICKAXE,  Items.DIAMOND_AXE,  Items.DIAMOND_SHOVEL,  Items.DIAMOND_HOE,
        Items.NETHERITE_SWORD, Items.NETHERITE_PICKAXE, Items.NETHERITE_AXE, Items.NETHERITE_SHOVEL, Items.NETHERITE_HOE
    };

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

        ResourceLocation mobId = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        if (mobId != null && AlaLootConfig.MOB_BLACKLIST.get().contains(mobId.toString())) return;

        event.getDrops().clear();

        if (rollChance(level, player)) {
            ItemStack stack = RandomItemHelper.randomStack(level.getRandom());
            if (!stack.isEmpty()) {
                ItemEntity ie = new ItemEntity(level, entity.getX(), entity.getY() + 0.25, entity.getZ(), stack);
                ie.setDefaultPickUpDelay();
                event.getDrops().add(ie);
            }
        } else if (level.getRandom().nextDouble() < CURSE_CHANCE) {
            ItemStack cursed = makeCursedStack(level.getRandom());
            ItemEntity ie = new ItemEntity(level, entity.getX(), entity.getY() + 0.25, entity.getZ(), cursed);
            ie.setDefaultPickUpDelay();
            event.getDrops().add(ie);
        }

        grantXp(player);
    }

    private static ItemStack makeCursedStack(RandomSource random) {
        Item tool = CURSE_TOOLS[random.nextInt(CURSE_TOOLS.length)];
        ItemStack stack = new ItemStack(tool);
        stack.setDamageValue(stack.getMaxDamage() - 1);
        return stack;
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

    public static double curseChance() {
        return CURSE_CHANCE;
    }
}
