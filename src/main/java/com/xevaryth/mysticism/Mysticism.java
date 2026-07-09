package com.xevaryth.mysticism;

import com.mojang.logging.LogUtils;
import com.xevaryth.mysticism.client.ManaHud;
import com.xevaryth.mysticism.mana.ManaAttachments;
import com.xevaryth.mysticism.mana.ManaCommands;
import com.xevaryth.mysticism.mana.ManaEvents;
import com.xevaryth.mysticism.registry.MysticismAttributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(Mysticism.MODID)
public final class Mysticism {
    public static final String MODID = "mysticism";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Mysticism(IEventBus modBus) {
        ManaAttachments.register(modBus);
        MysticismAttributes.ATTRIBUTES.register(modBus);
        ManaHud.register(modBus);

        NeoForge.EVENT_BUS.addListener(ManaCommands::register);
        NeoForge.EVENT_BUS.addListener(ManaEvents::onPlayerTick);
        NeoForge.EVENT_BUS.addListener(ManaEvents::onPlayerClone);
    }
}
