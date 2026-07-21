package com.xevaryth.mysticism.api;

import com.xevaryth.mysticism.api.event.ManaChangedEvent;
import com.xevaryth.mysticism.api.event.ManaDepletedEvent;
import com.xevaryth.mysticism.api.event.ManaDrainEvent;
import com.xevaryth.mysticism.api.event.ManaGainEvent;
import com.xevaryth.mysticism.api.event.ManaSpendEvent;
import com.xevaryth.mysticism.mana.ManaAttachments;
import com.xevaryth.mysticism.mana.ManaData;
import com.xevaryth.mysticism.registry.MysticismAttributes;
import java.util.Objects;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;

/** Public entry point for Mysticism mana transactions. */
public final class ManaApi {
    private ManaApi() {}

    public static int getMana(Player player) {
        return data(player).currentMana();
    }

    public static int getMaxMana(Player player) {
        return Math.max(0, (int)Math.floor(player.getAttributeValue(
            MysticismAttributes.MAX_MANA.getDelegate()
        )));
    }

    public static double getManaRegenPerSecond(Player player) {
        return player.getAttributeValue(
            MysticismAttributes.MANA_REGEN_PER_SECOND.getDelegate()
        );
    }

    public static ManaSpendResult spendMana(Player player, int amount, ManaContext context) {
        requireServer(player);
        Objects.requireNonNull(context, "context");
        if (amount < 0) {
            throw new IllegalArgumentException("Mana cost cannot be negative");
        }

        int before = getMana(player);
        ManaSpendEvent.Pre pre = new ManaSpendEvent.Pre(player, context, amount);
        if (NeoForge.EVENT_BUS.post(pre).isCanceled()) {
            ManaSpendResult failed = ManaSpendResult.failure(
                amount, pre.getCost(), before, ManaSpendFailureReason.CANCELLED
            );
            NeoForge.EVENT_BUS.post(new ManaSpendEvent.Failed(
                player, context, amount, pre.getCost(), before,
                ManaSpendFailureReason.CANCELLED
            ));
            return failed;
        }

        int finalCost = Math.max(0, pre.getCost());
        if (before < finalCost) {
            ManaSpendResult failed = ManaSpendResult.failure(
                amount, finalCost, before, ManaSpendFailureReason.INSUFFICIENT_MANA
            );
            NeoForge.EVENT_BUS.post(new ManaSpendEvent.Failed(
                player, context, amount, finalCost, before,
                ManaSpendFailureReason.INSUFFICIENT_MANA
            ));
            return failed;
        }

        int after = setRaw(player, before - finalCost);
        NeoForge.EVENT_BUS.post(new ManaSpendEvent.Post(
            player, context, amount, finalCost, before, after
        ));
        postChange(player, context, before, after, -amount);
        return ManaSpendResult.success(amount, finalCost, before, after);
    }

    public static boolean tryConsumeMana(Player player, int amount, ManaContext context) {
        return spendMana(player, amount, context).success();
    }

    public static boolean tryConsumeMana(Player player, int amount) {
        return tryConsumeMana(player, amount, ManaContext.api());
    }

    public static int gainMana(Player player, int amount, ManaContext context) {
        requireServer(player);
        Objects.requireNonNull(context, "context");
        if (amount < 0) {
            throw new IllegalArgumentException("Mana gain cannot be negative");
        }
        ManaGainEvent.Pre pre = new ManaGainEvent.Pre(player, context, amount);
        if (NeoForge.EVENT_BUS.post(pre).isCanceled()) {
            return 0;
        }
        int finalAmount = Math.max(0, pre.getAmount());
        int before = getMana(player);
        int after = setRaw(player, before + finalAmount);
        int actual = after - before;
        NeoForge.EVENT_BUS.post(new ManaGainEvent.Post(
            player, context, amount, finalAmount, actual, before, after
        ));
        postChange(player, context, before, after, amount);
        return actual;
    }

    public static int drainMana(Player player, int amount, ManaContext context) {
        requireServer(player);
        Objects.requireNonNull(context, "context");
        if (amount < 0) {
            throw new IllegalArgumentException("Mana drain cannot be negative");
        }
        ManaDrainEvent.Pre pre = new ManaDrainEvent.Pre(player, context, amount);
        if (NeoForge.EVENT_BUS.post(pre).isCanceled()) {
            return 0;
        }
        int finalAmount = Math.max(0, pre.getAmount());
        int before = getMana(player);
        int after = setRaw(player, before - finalAmount);
        int actual = before - after;
        NeoForge.EVENT_BUS.post(new ManaDrainEvent.Post(
            player, context, amount, finalAmount, actual, before, after
        ));
        postChange(player, context, before, after, -amount);
        return actual;
    }

    public static void setMana(Player player, int amount) {
        requireServer(player);
        int before = getMana(player);
        int after = setRaw(player, amount);
        postChange(player, ManaContext.api(), before, after, amount - before);
    }

    public static void addMana(Player player, int amount) {
        if (amount >= 0) {
            gainMana(player, amount, ManaContext.api());
        } else {
            drainMana(player, -amount, ManaContext.api());
        }
    }

    public static void fillMana(Player player) {
        gainMana(player, Math.max(0, getMaxMana(player) - getMana(player)), ManaContext.api());
    }

    public static void emptyMana(Player player) {
        drainMana(player, getMana(player), ManaContext.api());
    }

    public static ManaPaymentSession beginPaymentSession(Player player, ManaContext context) {
        requireServer(player);
        return new ManaPaymentSession(player, context);
    }

    private static void postChange(Player player, ManaContext context, int before, int after, int requestedDelta) {
        if (before == after) {
            return;
        }
        NeoForge.EVENT_BUS.post(new ManaChangedEvent(
            player, context, before, after, requestedDelta, after - before
        ));
        if (before > 0 && after == 0) {
            NeoForge.EVENT_BUS.post(new ManaDepletedEvent(player, context, before));
        }
    }

    private static int setRaw(Player player, int amount) {
        ManaData data = data(player);
        data.setCurrentMana(amount, getMaxMana(player));
        player.setData(ManaAttachments.MANA.get(), data);
        return data.currentMana();
    }

    private static ManaData data(Player player) {
        return player.getData(ManaAttachments.MANA.get());
    }

    private static void requireServer(Player player) {
        Objects.requireNonNull(player, "player");
        if (player.level().isClientSide()) {
            throw new IllegalStateException("Mana mutations must run on the logical server");
        }
    }
}
