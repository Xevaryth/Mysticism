package com.xevaryth.mysticism.registry;

import com.xevaryth.mysticism.Mysticism;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.xevaryth.mysticism.mana.ManaDataLimits;

public final class MysticismAttributes {
    public static final double FALLBACK_MAX_MANA = 40.0D;
    public static final double FALLBACK_MANA_REGEN = 1.0D;

    public static final DeferredRegister<Attribute> ATTRIBUTES =
        DeferredRegister.create(
            Registries.ATTRIBUTE,
            Mysticism.MODID
        );

    public static final DeferredHolder<Attribute, Attribute> MAX_MANA =
        ATTRIBUTES.register(
            "max_mana",
            () -> new RangedAttribute(
                "attribute.name.mysticism.max_mana",
                FALLBACK_MAX_MANA,
                0.0D,
                ManaDataLimits.HARD_CAP
            ).setSyncable(true)
        );

    public static final DeferredHolder<Attribute, Attribute>
        MANA_REGEN_PER_SECOND =
        ATTRIBUTES.register(
            "mana_regen_per_second",
            () -> new RangedAttribute(
                "attribute.name.mysticism.mana_regen_per_second",
                FALLBACK_MANA_REGEN,
                0.0D,
                1024.0D
            ).setSyncable(true)
        );

    private MysticismAttributes() {}
}
