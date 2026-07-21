package com.xevaryth.mysticism.api.event;

import com.xevaryth.mysticism.api.ManaContext;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

public final class ManaChangedEvent extends Event {
    private final Player player; private final ManaContext context; private final int manaBefore, manaAfter, requestedDelta, actualDelta;
    public ManaChangedEvent(Player player,ManaContext context,int before,int after,int requestedDelta,int actualDelta){this.player=player;this.context=context;this.manaBefore=before;this.manaAfter=after;this.requestedDelta=requestedDelta;this.actualDelta=actualDelta;}
    public Player getPlayer(){return player;} public ManaContext getContext(){return context;} public int getManaBefore(){return manaBefore;} public int getManaAfter(){return manaAfter;} public int getRequestedDelta(){return requestedDelta;} public int getActualDelta(){return actualDelta;}
}
