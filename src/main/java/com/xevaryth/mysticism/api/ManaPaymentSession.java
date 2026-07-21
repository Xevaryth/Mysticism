package com.xevaryth.mysticism.api;

import java.util.Objects;
import net.minecraft.world.entity.player.Player;

public final class ManaPaymentSession implements AutoCloseable {
    private final Player player;
    private final ManaContext context;
    private int nominalPaid;
    private boolean closed;

    ManaPaymentSession(Player player, ManaContext context) {
        this.player = Objects.requireNonNull(player, "player");
        this.context = Objects.requireNonNull(context, "context");
    }

    public ManaSpendResult payToward(int targetNominalCost) {
        if (closed) {
            int mana = ManaApi.getMana(player);
            return ManaSpendResult.failure(targetNominalCost, 0, mana, ManaSpendFailureReason.SESSION_CLOSED);
        }
        if (targetNominalCost < nominalPaid) {
            throw new IllegalArgumentException("Payment target cannot decrease");
        }
        int difference = targetNominalCost - nominalPaid;
        ManaSpendResult result = ManaApi.spendMana(player, difference, context);
        if (result.success()) {
            nominalPaid = targetNominalCost;
        }
        return result;
    }

    public int nominalPaid() { return nominalPaid; }
    public boolean isClosed() { return closed; }
    public ManaContext context() { return context; }
    @Override public void close() { closed = true; }
}
