package com.xevaryth.mysticism.registry;

import com.xevaryth.mysticism.Mysticism;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class MysticismAttributes {
    private MysticismAttributes() {}

    private static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(NeoForgeRegistries.ATTRIBUTES, Mysticism.MODID);

    public static final Supplier<Attribute> MANA_REGEN_PER_SECOND = ATTRIBUTES.register(
            "mana_regen_per_second",
            () -> new RangedAttribute("attribute.name.mysticism.mana_regen_per_second", 1.0D, 0.0D, 1024.0D)
                    .setSyncable(true)
    );

    public static void register(IEventBus modBus) {
        ATTRIBUTES.register(modBus);
    }
}
