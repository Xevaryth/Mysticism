package com.xevaryth.mysticism.api.event;

import com.xevaryth.mysticism.api.ManaContext;
import com.xevaryth.mysticism.api.ManaSpendFailureReason;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public abstract class ManaSpendEvent extends Event {
    private final Player player;
    private final ManaContext context;
    private final int requestedCost;

    protected ManaSpendEvent(Player player, ManaContext context, int requestedCost) {
        this.player = player;
        this.context = context;
        this.requestedCost = requestedCost;
    }

    public Player getPlayer() { return player; }
    public ManaContext getContext() { return context; }
    public int getRequestedCost() { return requestedCost; }

    public static final class Pre extends ManaSpendEvent implements ICancellableEvent {
        private int cost;
        public Pre(Player player, ManaContext context, int requestedCost) {
            super(player, context, requestedCost);
            this.cost = requestedCost;
        }
        public int getCost() { return cost; }
        public void setCost(int cost) { this.cost = Math.max(0, cost); }
    }

    public static final class Post extends ManaSpendEvent {
        private final int finalCost;
        private final int manaBefore;
        private final int manaAfter;
        public Post(Player player, ManaContext context, int requestedCost, int finalCost, int manaBefore, int manaAfter) {
            super(player, context, requestedCost);
            this.finalCost = finalCost;
            this.manaBefore = manaBefore;
            this.manaAfter = manaAfter;
        }
        public int getFinalCost() { return finalCost; }
        public int getManaBefore() { return manaBefore; }
        public int getManaAfter() { return manaAfter; }
    }

    public static final class Failed extends ManaSpendEvent {
        private final int finalCost;
        private final int availableMana;
        private final ManaSpendFailureReason reason;
        public Failed(Player player, ManaContext context, int requestedCost, int finalCost, int availableMana, ManaSpendFailureReason reason) {
            super(player, context, requestedCost);
            this.finalCost = finalCost;
            this.availableMana = availableMana;
            this.reason = reason;
        }
        public int getFinalCost() { return finalCost; }
        public int getAvailableMana() { return availableMana; }
        public ManaSpendFailureReason getReason() { return reason; }
    }
}
