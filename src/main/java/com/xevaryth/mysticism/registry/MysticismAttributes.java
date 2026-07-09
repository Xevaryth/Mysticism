package com.xevaryth.mysticism.registry;

import com.xevaryth.mysticism.Mysticism;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class MysticismAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
        DeferredRegister.create(Registries.ATTRIBUTE, Mysticism.MODID);

    public static final DeferredHolder<Attribute, Attribute> MANA_REGEN_PER_SECOND =
        ATTRIBUTES.register("mana_regen_per_second",
            () -> new RangedAttribute(
                "attribute.name.mysticism.mana_regen_per_second",
                1.0D,
                0.0D,
                1024.0D
            ).setSyncable(true));

    private MysticismAttributes() {}
}
