package com.xevaryth.simpermanalibrary;

import com.mojang.logging.LogUtils;
import com.xevaryth.simpermanalibrary.client.ManaHud;
import com.xevaryth.simpermanalibrary.mana.ManaAttachments;
import com.xevaryth.simpermanalibrary.mana.ManaCommands;
import com.xevaryth.simpermanalibrary.mana.ManaEvents;
import com.xevaryth.simpermanalibrary.registry.SimperManaAttributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import com.xevaryth.simpermanalibrary.config.SimperManaConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import com.xevaryth.simpermanalibrary.registry.SimperManaAttributeEvents;
import org.slf4j.Logger;

@Mod(SimperManaLibrary.MOD_ID)
public final class SimperManaLibrary {
    public static final String MOD_ID = "simper_mana_library";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SimperManaLibrary(
        IEventBus modBus,
        ModContainer modContainer
    ) {
        modContainer.registerConfig(
            ModConfig.Type.SERVER,
            SimperManaConfig.SPEC
        );
        ManaAttachments.register(modBus);
        SimperManaAttributeEvents.register(modBus);
        SimperManaAttributes.ATTRIBUTES.register(modBus);
        ManaHud.register(modBus);

        NeoForge.EVENT_BUS.addListener(ManaCommands::register);
        NeoForge.EVENT_BUS.addListener(ManaEvents::onPlayerTick);
        NeoForge.EVENT_BUS.addListener(ManaEvents::onPlayerClone);
    }
}
