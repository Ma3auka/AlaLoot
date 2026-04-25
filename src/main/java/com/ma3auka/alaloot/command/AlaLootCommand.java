package com.ma3auka.alaloot.command;

import java.util.ArrayList;
import java.util.List;

import com.ma3auka.alaloot.AlaLoot;
import com.ma3auka.alaloot.config.AlaLootConfig;
import com.ma3auka.alaloot.event.MobLootEventHandler;
import com.ma3auka.alaloot.util.BlockCategoryHelper;
import com.ma3auka.alaloot.util.RandomItemHelper;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = AlaLoot.MODID)
public final class AlaLootCommand {
    private static final SuggestionProvider<CommandSourceStack> BLACKLIST_TYPES = (ctx, b) -> {
        for (String s : new String[]{"mob", "block", "item", "mod"}) b.suggest(s);
        return b.buildFuture();
    };
    private static final SuggestionProvider<CommandSourceStack> TOGGLE_TARGETS = (ctx, b) -> {
        for (String s : new String[]{"mob", "block", "boss"}) b.suggest(s);
        return b.buildFuture();
    };

    private AlaLootCommand() {}

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("alaloot")
            .then(Commands.literal("reload")
                .requires(s -> s.hasPermission(2))
                .executes(AlaLootCommand::cmdReload))
            .then(Commands.literal("stats").executes(AlaLootCommand::cmdStats))
            .then(Commands.literal("info").executes(AlaLootCommand::cmdInfo))
            .then(Commands.literal("toggle")
                .requires(s -> s.hasPermission(2))
                .then(Commands.argument("target", StringArgumentType.word())
                    .suggests(TOGGLE_TARGETS)
                    .executes(AlaLootCommand::cmdToggle)))
            .then(Commands.literal("blacklist")
                .requires(s -> s.hasPermission(2))
                .then(Commands.literal("add")
                    .then(Commands.argument("type", StringArgumentType.word())
                        .suggests(BLACKLIST_TYPES)
                        .then(Commands.argument("id", StringArgumentType.greedyString())
                            .executes(ctx -> cmdBlacklist(ctx, true)))))
                .then(Commands.literal("remove")
                    .then(Commands.argument("type", StringArgumentType.word())
                        .suggests(BLACKLIST_TYPES)
                        .then(Commands.argument("id", StringArgumentType.greedyString())
                            .executes(ctx -> cmdBlacklist(ctx, false))))));
        dispatcher.register(root);
    }

    private static int cmdReload(CommandContext<CommandSourceStack> ctx) {
        RandomItemHelper.invalidate();
        ctx.getSource().sendSuccess(() -> tr("commands.alaloot.reload.success"), true);
        return 1;
    }

    private static int cmdStats(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        if (player == null) {
            ctx.getSource().sendFailure(tr("commands.alaloot.error.player_only"));
            return 0;
        }
        int level = player.experienceLevel;
        double chance = MobLootEventHandler.currentChance(player) * 100;
        int poolSize = RandomItemHelper.poolSize();

        ctx.getSource().sendSuccess(() -> tr("commands.alaloot.stats.header").withStyle(ChatFormatting.GOLD), false);
        ctx.getSource().sendSuccess(() -> tr("commands.alaloot.stats.level", level), false);
        ctx.getSource().sendSuccess(() -> tr("commands.alaloot.stats.chance",
                String.format("%.0f", chance)), false);
        ctx.getSource().sendSuccess(() -> tr("commands.alaloot.stats.pool", poolSize), false);
        double curseChance = MobLootEventHandler.curseChance() * 100;
        ctx.getSource().sendSuccess(() -> tr("commands.alaloot.stats.curse",
                String.format("%.0f", curseChance)), false);
        return 1;
    }

    private static int cmdInfo(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        if (player == null) {
            ctx.getSource().sendFailure(tr("commands.alaloot.error.player_only"));
            return 0;
        }
        ItemStack held = player.getMainHandItem();
        if (!held.isEmpty()) {
            Component itemName = held.getHoverName();
            ctx.getSource().sendSuccess(() -> tr("commands.alaloot.info.held", itemName), false);
        } else {
            ctx.getSource().sendSuccess(() -> tr("commands.alaloot.info.held_empty"), false);
        }

        HitResult hr = player.pick(8.0, 1.0f, false);
        if (hr instanceof BlockHitResult bhr && hr.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = bhr.getBlockPos();
            BlockState state = player.level().getBlockState(pos);
            BlockCategoryHelper.Category cat = BlockCategoryHelper.classify(state);
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
            ctx.getSource().sendSuccess(() ->
                    tr("commands.alaloot.info.block",
                            Component.literal(String.valueOf(id)),
                            categoryComponent(cat)), false);
        } else {
            ctx.getSource().sendSuccess(() -> tr("commands.alaloot.info.no_block"), false);
        }
        return 1;
    }

    private static int cmdToggle(CommandContext<CommandSourceStack> ctx) {
        String target = StringArgumentType.getString(ctx, "target");
        ModConfigSpec.BooleanValue v = switch (target) {
            case "mob" -> AlaLootConfig.ENABLE_MOB_DROP;
            case "block" -> AlaLootConfig.ENABLE_BLOCK_DROP;
            case "boss" -> AlaLootConfig.ENABLE_BOSS_DROPS;
            default -> null;
        };
        if (v == null) {
            ctx.getSource().sendFailure(tr("commands.alaloot.toggle.unknown", target));
            return 0;
        }
        boolean newVal = !v.get();
        v.set(newVal);
        ctx.getSource().sendSuccess(() -> tr("commands.alaloot.toggle.set", target,
                Component.literal(String.valueOf(newVal))
                        .withStyle(newVal ? ChatFormatting.GREEN : ChatFormatting.RED)), true);
        return 1;
    }

    private static int cmdBlacklist(CommandContext<CommandSourceStack> ctx, boolean add) {
        String type = StringArgumentType.getString(ctx, "type");
        String id = StringArgumentType.getString(ctx, "id").trim();

        ModConfigSpec.ConfigValue<List<? extends String>> target = switch (type) {
            case "mob" -> AlaLootConfig.MOB_BLACKLIST;
            case "block" -> AlaLootConfig.BLOCK_BLACKLIST;
            case "item" -> AlaLootConfig.ITEM_DROP_BLACKLIST;
            case "mod" -> AlaLootConfig.MOD_BLACKLIST;
            default -> null;
        };
        if (target == null) {
            ctx.getSource().sendFailure(tr("commands.alaloot.blacklist.unknown_type", type));
            return 0;
        }

        List<String> current = new ArrayList<>();
        for (Object o : target.get()) current.add(String.valueOf(o));

        if (add) {
            if (current.contains(id)) {
                ctx.getSource().sendFailure(tr("commands.alaloot.blacklist.already", id));
                return 0;
            }
            current.add(id);
            target.set(current);
            RandomItemHelper.invalidate();
            ctx.getSource().sendSuccess(() -> tr("commands.alaloot.blacklist.added", type, id), true);
        } else {
            if (!current.remove(id)) {
                ctx.getSource().sendFailure(tr("commands.alaloot.blacklist.not_found", id));
                return 0;
            }
            target.set(current);
            RandomItemHelper.invalidate();
            ctx.getSource().sendSuccess(() -> tr("commands.alaloot.blacklist.removed", type, id), true);
        }
        return 1;
    }

    private static MutableComponent tr(String key, Object... args) {
        return Component.translatable(key, args);
    }

    private static Component categoryComponent(BlockCategoryHelper.Category c) {
        return tr("alaloot.category." + c.name().toLowerCase());
    }
}
