package com.xevaryth.simpermanalibrary.api;

import com.xevaryth.simpermanalibrary.mana.ManaAttachments;
import com.xevaryth.simpermanalibrary.mana.ManaData;
import com.xevaryth.simpermanalibrary.registry.SimperManaAttributes;
import net.minecraft.world.entity.player.Player;

/**
 * Public entry point for interacting with Simper Mana Library's player mana system.
 *
 * <p>Consumers should use this class instead of accessing attachments or
 * implementation classes directly. All mutations are clamped to the player's
 * current maximum mana and are synchronized by the underlying attachment.</p>
 */
public final class ManaApi {
    private ManaApi() {}

    public static int getMana(Player player) {
        return data(player).currentMana();
    }

    public static int getMaxMana(Player player) {
        return Math.max(0, (int) Math.floor(player.getAttributeValue(
            SimperManaAttributes.MAX_MANA.getDelegate()
        )));
    }

    public static double getManaRegenPerSecond(Player player) {
        return player.getAttributeValue(
            SimperManaAttributes.MANA_REGEN_PER_SECOND.getDelegate()
        );
    }

    public static void setMana(Player player, int amount) {
        ManaData data = data(player);
        data.setCurrentMana(amount, getMaxMana(player));
        save(player, data);
    }

    public static void addMana(Player player, int amount) {
        ManaData data = data(player);
        data.addCurrentMana(amount, getMaxMana(player));
        save(player, data);
    }

    /**
     * Attempts to spend mana without allowing the balance to become negative.
     *
     * @return {@code true} when the full amount was spent
     * @throws IllegalArgumentException when {@code amount} is negative
     */
    public static boolean tryConsumeMana(Player player, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Mana cost cannot be negative");
        }

        ManaData data = data(player);
        if (data.currentMana() < amount) {
            return false;
        }

        data.addCurrentMana(-amount, getMaxMana(player));
        save(player, data);
        return true;
    }

    public static void fillMana(Player player) {
        setMana(player, getMaxMana(player));
    }

    public static void emptyMana(Player player) {
        setMana(player, 0);
    }

    private static ManaData data(Player player) {
        return player.getData(ManaAttachments.MANA.get());
    }

    private static void save(Player player, ManaData data) {
        player.setData(ManaAttachments.MANA.get(), data);
    }
}
