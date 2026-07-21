package com.xevaryth.mysticism.api;

public record ManaSpendResult(
    boolean success,
    int requestedCost,
    int finalCost,
    int manaBefore,
    int manaAfter,
    ManaSpendFailureReason failureReason
) {
    public static ManaSpendResult success(int requested, int cost, int before, int after) {
        return new ManaSpendResult(true, requested, cost, before, after, null);
    }

    public static ManaSpendResult failure(
        int requested,
        int cost,
        int mana,
        ManaSpendFailureReason reason
    ) {
        return new ManaSpendResult(false, requested, cost, mana, mana, reason);
    }
}
