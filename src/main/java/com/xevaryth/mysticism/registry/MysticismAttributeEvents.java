package com.xevaryth.mysticism.registry;

import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

public final class MysticismAttributeEvents {
    private MysticismAttributeEvents() {}

    public static void register(IEventBus modBus) {
        modBus.addListener(
            MysticismAttributeEvents::modifyPlayerAttributes
        );
    }

    private static void modifyPlayerAttributes(
        EntityAttributeModificationEvent event
    ) {
        event.add(
            EntityType.PLAYER,
            MysticismAttributes.MAX_MANA
        );

        event.add(
            EntityType.PLAYER,
            MysticismAttributes.MANA_REGEN_PER_SECOND
        );
    }
}
