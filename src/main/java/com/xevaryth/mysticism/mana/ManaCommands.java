package com.xevaryth.mysticism.mana;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class ManaCommands {
    private ManaCommands() {}

    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("mana")
                .requires(source -> source.hasPermission(2))
                .executes(ctx -> get(ctx.getSource()))
                .then(Commands.literal("get").executes(ctx -> get(ctx.getSource())))
                .then(Commands.literal("set")
                        .then(Commands.argument("max", IntegerArgumentType.integer(0, ManaData.HARD_CAP))
                                .executes(ctx -> set(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "max")))))
                .then(Commands.literal("add")
                        .then(Commands.argument("amount", IntegerArgumentType.integer(-ManaData.HARD_CAP, ManaData.HARD_CAP))
                                .executes(ctx -> add(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "amount")))))
                .then(Commands.literal("fill").executes(ctx -> fill(ctx.getSource())))
                .then(Commands.literal("empty").executes(ctx -> empty(ctx.getSource())))
        );
    }

    private static int get(CommandSourceStack source) throws Exception {
        ServerPlayer player = source.getPlayerOrException();
        ManaData data = player.getData(ManaAttachments.MANA.get());
        source.sendSuccess(() -> Component.translatable("commands.mysticism.get", data.currentMana(), data.maxMana()), false);
        return data.currentMana();
    }

    private static int set(CommandSourceStack source, int max) throws Exception {
        ServerPlayer player = source.getPlayerOrException();
        ManaData data = player.getData(ManaAttachments.MANA.get());
        data.setMaxMana(max);
        player.setData(ManaAttachments.MANA.get(), data);
        source.sendSuccess(() -> Component.translatable("commands.mysticism.set", max), true);
        return max;
    }

    private static int add(CommandSourceStack source, int amount) throws Exception {
        ServerPlayer player = source.getPlayerOrException();
        ManaData data = player.getData(ManaAttachments.MANA.get());
        data.addMaxMana(amount, true);
        player.setData(ManaAttachments.MANA.get(), data);
        source.sendSuccess(() -> Component.translatable("commands.mysticism.add", amount, data.maxMana()), true);
        return data.maxMana();
    }

    private static int fill(CommandSourceStack source) throws Exception {
        ServerPlayer player = source.getPlayerOrException();
        ManaData data = player.getData(ManaAttachments.MANA.get());
        data.fill();
        player.setData(ManaAttachments.MANA.get(), data);
        source.sendSuccess(() -> Component.translatable("commands.mysticism.fill"), true);
        return data.currentMana();
    }

    private static int empty(CommandSourceStack source) throws Exception {
        ServerPlayer player = source.getPlayerOrException();
        ManaData data = player.getData(ManaAttachments.MANA.get());
        data.empty();
        player.setData(ManaAttachments.MANA.get(), data);
        source.sendSuccess(() -> Component.translatable("commands.mysticism.empty"), true);
        return data.currentMana();
    }
}
