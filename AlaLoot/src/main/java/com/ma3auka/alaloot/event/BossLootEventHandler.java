package com.ma3auka.alaloot.event;

import com.ma3auka.alaloot.config.AlaLootConfig;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

public final class BossLootEventHandler {
    private BossLootEventHandler() {}

    /** Returns true if entity was a boss and we handled the drops. */
    public static boolean tryHandle(LivingDropsEvent event) {
        if (!AlaLootConfig.ENABLE_BOSS_DROPS.get()) return false;
        LivingEntity entity = event.getEntity();
        Level level = entity.level();

        ItemStack drop = ItemStack.EMPTY;

        if (entity instanceof EnderDragon && AlaLootConfig.ENABLE_DRAGON_DROP.get()) {
            drop = new ItemStack(Items.DRAGON_EGG, AlaLootConfig.DRAGON_EGG_STACK_SIZE.get());
        } else if (entity instanceof WitherBoss && AlaLootConfig.ENABLE_WITHER_DROP.get()) {
            drop = new ItemStack(Items.NETHER_STAR, AlaLootConfig.WITHER_STAR_STACK_SIZE.get());
        } else if (entity instanceof Warden && AlaLootConfig.ENABLE_WARDEN_DROP.get()) {
            drop = new ItemStack(Items.DIAMOND, AlaLootConfig.WARDEN_DIAMOND_STACK_SIZE.get());
        } else if (entity instanceof ElderGuardian && AlaLootConfig.ENABLE_ELDER_GUARDIAN_DROP.get()) {
            drop = new ItemStack(Items.SPONGE, AlaLootConfig.ELDER_GUARDIAN_SPONGE_STACK_SIZE.get());
        } else {
            return false;
        }

        event.getDrops().clear();
        if (!drop.isEmpty()) {
            ItemEntity ie = new ItemEntity(level, entity.getX(), entity.getY() + 0.5, entity.getZ(), drop);
            ie.setDefaultPickUpDelay();
            event.getDrops().add(ie);
        }
        return true;
    }
}
