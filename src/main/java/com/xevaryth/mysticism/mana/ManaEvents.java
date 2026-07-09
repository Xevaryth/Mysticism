package com.xevaryth.mysticism.mana;

import com.xevaryth.mysticism.registry.MysticismAttributes;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public final class ManaEvents {
    private ManaEvents() {}

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ManaData data = player.getData(ManaAttachments.MANA.get());
        double regen = player.getAttributeValue(MysticismAttributes.MANA_REGEN_PER_SECOND.getDelegate());
        if (data.tickRegen(regen)) {
            player.setData(ManaAttachments.MANA.get(), data);
        }
    }

    public static void onPlayerClone(PlayerEvent.Clone event) {
        ManaData oldData = event.getOriginal().getData(ManaAttachments.MANA.get());
        event.getEntity().setData(ManaAttachments.MANA.get(), oldData.copy());
    }
}
