package com.xevaryth.mysticism.registry;

import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

import static com.xevaryth.mysticism.Mysticism.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public final class MysticismAttributeEvents {

    @SubscribeEvent
    public static void modifyPlayerAttributes(EntityAttributeModificationEvent event) {
        event.add(
            EntityType.PLAYER,
            MysticismAttributes.MANA_REGEN_PER_SECOND
        );
    }

    private MysticismAttributeEvents() {}
}
