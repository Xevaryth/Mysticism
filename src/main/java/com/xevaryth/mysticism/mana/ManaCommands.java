package com.xevaryth.mysticism.mana;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.xevaryth.mysticism.registry.MysticismAttributes;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class ManaCommands {
    private ManaCommands() {}

    public static void register(
        RegisterCommandsEvent event
    ) {
        CommandDispatcher<CommandSourceStack> dispatcher =
            event.getDispatcher();

        dispatcher.register(
            Commands.literal("mana")
                .requires(source ->
                    source.hasPermission(2)
                )
                .executes(context ->
                    get(context.getSource())
                )
                .then(
                    Commands.literal("get")
                        .executes(context ->
                            get(context.getSource())
                        )
                )
                .then(
                    Commands.literal("set")
                        .then(
                            Commands.argument(
                                "max",
                                IntegerArgumentType.integer(
                                    0,
                                    ManaDataLimits.HARD_CAP
                                )
                            ).executes(context ->
                                setMaximumMana(
                                    context.getSource(),
                                    IntegerArgumentType
                                        .getInteger(
                                            context,
                                            "max"
                                        )
                                )
                            )
                        )
                )
                .then(
                    Commands.literal("add")
                        .then(
                            Commands.argument(
                                "amount",
                                IntegerArgumentType.integer(
                                    -ManaDataLimits.HARD_CAP,
                                    ManaDataLimits.HARD_CAP
                                )
                            ).executes(context ->
                                addMaximumMana(
                                    context.getSource(),
                                    IntegerArgumentType
                                        .getInteger(
                                            context,
                                            "amount"
                                        )
                                )
                            )
                        )
                )
                .then(
                    Commands.literal("fill")
                        .executes(context ->
                            fill(context.getSource())
                        )
                )
                .then(
                    Commands.literal("empty")
                        .executes(context ->
                            empty(context.getSource())
                        )
                )
        );
    }

    private static int get(
        CommandSourceStack source
    ) throws CommandSyntaxException {
        ServerPlayer player =
            source.getPlayerOrException();

        ManaData data = player.getData(
            ManaAttachments.MANA.get()
        );

        int maximumMana =
            ManaEvents.getMaximumMana(player);

        source.sendSuccess(
            () -> Component.translatable(
                "commands.mysticism.get",
                data.currentMana(),
                maximumMana
            ),
            false
        );

        return data.currentMana();
    }

    private static int setMaximumMana(
        CommandSourceStack source,
        int maximum
    ) throws CommandSyntaxException {
        ServerPlayer player =
            source.getPlayerOrException();

        AttributeInstance attribute =
            getMaximumManaAttribute(player);

        attribute.setBaseValue(maximum);

        ManaData data = player.getData(
            ManaAttachments.MANA.get()
        );

        int resultingMaximum =
            ManaEvents.getMaximumMana(player);

        data.setCurrentMana(
            data.currentMana(),
            resultingMaximum
        );

        player.setData(
            ManaAttachments.MANA.get(),
            data
        );

        source.sendSuccess(
            () -> Component.translatable(
                "commands.mysticism.set",
                resultingMaximum
            ),
            true
        );

        return resultingMaximum;
    }

    private static int addMaximumMana(
        CommandSourceStack source,
        int amount
    ) throws CommandSyntaxException {
        ServerPlayer player =
            source.getPlayerOrException();

        AttributeInstance attribute =
            getMaximumManaAttribute(player);

        int oldMaximum =
            ManaEvents.getMaximumMana(player);

        double newBaseValue = Math.max(
            0.0D,
            Math.min(
                ManaDataLimits.HARD_CAP,
                attribute.getBaseValue() + amount
            )
        );

        attribute.setBaseValue(newBaseValue);

        int newMaximum =
            ManaEvents.getMaximumMana(player);

        ManaData data = player.getData(
            ManaAttachments.MANA.get()
        );

        /*
         * Preserve the old behavior: positive increases also
         * grant the newly gained mana.
         */
        if (newMaximum > oldMaximum) {
            data.addCurrentMana(
                newMaximum - oldMaximum,
                newMaximum
            );
        } else {
            data.setCurrentMana(
                data.currentMana(),
                newMaximum
            );
        }

        player.setData(
            ManaAttachments.MANA.get(),
            data
        );

        source.sendSuccess(
            () -> Component.translatable(
                "commands.mysticism.add",
                amount,
                newMaximum
            ),
            true
        );

        return newMaximum;
    }

    private static int fill(
        CommandSourceStack source
    ) throws CommandSyntaxException {
        ServerPlayer player =
            source.getPlayerOrException();

        ManaData data = player.getData(
            ManaAttachments.MANA.get()
        );

        data.fill(
            ManaEvents.getMaximumMana(player)
        );

        player.setData(
            ManaAttachments.MANA.get(),
            data
        );

        source.sendSuccess(
            () -> Component.translatable(
                "commands.mysticism.fill"
            ),
            true
        );

        return data.currentMana();
    }

    private static int empty(
        CommandSourceStack source
    ) throws CommandSyntaxException {
        ServerPlayer player =
            source.getPlayerOrException();

        ManaData data = player.getData(
            ManaAttachments.MANA.get()
        );

        data.empty();

        player.setData(
            ManaAttachments.MANA.get(),
            data
        );

        source.sendSuccess(
            () -> Component.translatable(
                "commands.mysticism.empty"
            ),
            true
        );

        return data.currentMana();
    }

    private static AttributeInstance
    getMaximumManaAttribute(
        ServerPlayer player
    ) {
        AttributeInstance attribute =
            player.getAttribute(
                MysticismAttributes
                    .MAX_MANA
                    .getDelegate()
            );

        if (attribute == null) {
            throw new IllegalStateException(
                "Player is missing the Mysticism maximum mana attribute."
            );
        }

        return attribute;
    }
}
