package com.xevaryth.mysticism.api.event;

import com.xevaryth.mysticism.api.ManaContext;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

public final class ManaDepletedEvent extends Event {
    private final Player player; private final ManaContext context; private final int manaBefore;
    public ManaDepletedEvent(Player player, ManaContext context, int manaBefore){this.player=player;this.context=context;this.manaBefore=manaBefore;}
    public Player getPlayer(){return player;} public ManaContext getContext(){return context;} public int getManaBefore(){return manaBefore;}
}
