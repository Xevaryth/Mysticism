package com.xevaryth.simpermanalibrary.registry;

import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

public final class SimperManaAttributeEvents {
    private SimperManaAttributeEvents() {}

    public static void register(IEventBus modBus) {
        modBus.addListener(
            SimperManaAttributeEvents::modifyPlayerAttributes
        );
    }

    private static void modifyPlayerAttributes(
        EntityAttributeModificationEvent event
    ) {
        event.add(
            EntityType.PLAYER,
            SimperManaAttributes.MAX_MANA
        );

        event.add(
            EntityType.PLAYER,
            SimperManaAttributes.MANA_REGEN_PER_SECOND
        );
    }
}
