package com.xevaryth.mysticism.api.event;

import com.xevaryth.mysticism.api.ManaContext;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public abstract class ManaGainEvent extends Event {
    private final Player player;
    private final ManaContext context;
    private final int requestedAmount;
    protected ManaGainEvent(Player player, ManaContext context, int requestedAmount) {
        this.player = player; this.context = context; this.requestedAmount = requestedAmount;
    }
    public Player getPlayer() { return player; }
    public ManaContext getContext() { return context; }
    public int getRequestedAmount() { return requestedAmount; }

    public static final class Pre extends ManaGainEvent implements ICancellableEvent {
        private int amount;
        public Pre(Player player, ManaContext context, int amount) { super(player, context, amount); this.amount = amount; }
        public int getAmount() { return amount; }
        public void setAmount(int amount) { this.amount = Math.max(0, amount); }
    }
    public static final class Post extends ManaGainEvent {
        private final int finalAmount, actualAmount, manaBefore, manaAfter;
        public Post(Player player, ManaContext context, int requested, int finalAmount, int actualAmount, int before, int after) {
            super(player, context, requested); this.finalAmount=finalAmount; this.actualAmount=actualAmount; this.manaBefore=before; this.manaAfter=after;
        }
        public int getFinalAmount(){return finalAmount;} public int getActualAmount(){return actualAmount;}
        public int getManaBefore(){return manaBefore;} public int getManaAfter(){return manaAfter;}
    }
}
