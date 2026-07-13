package com.xevaryth.mysticism.mana;

import com.xevaryth.mysticism.config.MysticismConfig;
import com.xevaryth.mysticism.registry.MysticismAttributes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public final class ManaEvents {
    private ManaEvents() {}

    public static void onPlayerTick(
        PlayerTickEvent.Post event
    ) {
        if (
            !(event.getEntity()
                instanceof ServerPlayer player)
        ) {
            return;
        }

        ManaData data = player.getData(
            ManaAttachments.MANA.get()
        );

        if (!data.attributesInitialized()) {
            initializeAttributes(player, data);

            player.setData(
                ManaAttachments.MANA.get(),
                data
            );
        }

        int maxMana = getMaximumMana(player);

        /*
         * Clamp current mana if an attribute modifier lowers
         * maximum mana.
         */
        if (data.currentMana() > maxMana) {
            data.setCurrentMana(
                data.currentMana(),
                maxMana
            );

            player.setData(
                ManaAttachments.MANA.get(),
                data
            );
        }

        double regeneration =
            player.getAttributeValue(
                MysticismAttributes
                    .MANA_REGEN_PER_SECOND
                    .getDelegate()
            );

        if (
            data.tickRegen(
                regeneration,
                maxMana
            )
        ) {
            player.setData(
                ManaAttachments.MANA.get(),
                data
            );
        }
    }

    public static void onPlayerClone(
        PlayerEvent.Clone event
    ) {
        ManaData oldData =
            event.getOriginal().getData(
                ManaAttachments.MANA.get()
            );

        event.getEntity().setData(
            ManaAttachments.MANA.get(),
            oldData.copy()
        );
    }

    public static int getMaximumMana(
        ServerPlayer player
    ) {
        return Math.max(
            0,
            (int) Math.floor(
                player.getAttributeValue(
                    MysticismAttributes
                        .MAX_MANA
                        .getDelegate()
                )
            )
        );
    }

    private static void initializeAttributes(
        ServerPlayer player,
        ManaData data
    ) {
        AttributeInstance maximumMana =
            player.getAttribute(
                MysticismAttributes
                    .MAX_MANA
                    .getDelegate()
            );

        AttributeInstance regeneration =
            player.getAttribute(
                MysticismAttributes
                    .MANA_REGEN_PER_SECOND
                    .getDelegate()
            );

        if (maximumMana != null) {
            maximumMana.setBaseValue(
                MysticismConfig
                    .STARTING_MAX_MANA
                    .get()
            );
        }

        if (regeneration != null) {
            regeneration.setBaseValue(
                MysticismConfig
                    .STARTING_MANA_REGEN_PER_SECOND
                    .get()
            );
        }

        int maxMana = getMaximumMana(player);

        data.fill(maxMana);
        data.markAttributesInitialized();
    }
}
