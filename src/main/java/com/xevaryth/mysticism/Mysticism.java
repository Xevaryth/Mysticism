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
import com.xevaryth.mysticism.config.MysticismConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import com.xevaryth.mysticism.registry.MysticismAttributeEvents;
import org.slf4j.Logger;

@Mod(Mysticism.MOD_ID)
public final class Mysticism {
    public static final String MOD_ID = "mysticism";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Mysticism(
        IEventBus modBus,
        ModContainer modContainer
    ) {
        modContainer.registerConfig(
            ModConfig.Type.SERVER,
            MysticismConfig.SPEC
        );
        ManaAttachments.register(modBus);
        MysticismAttributeEvents.register(modBus);
        MysticismAttributes.ATTRIBUTES.register(modBus);
        ManaHud.register(modBus);

        NeoForge.EVENT_BUS.addListener(ManaCommands::register);
        NeoForge.EVENT_BUS.addListener(ManaEvents::onPlayerTick);
        NeoForge.EVENT_BUS.addListener(ManaEvents::onPlayerClone);
    }
}
