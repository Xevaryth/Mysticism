package com.xevaryth.simpermanalibrary.config;

import com.xevaryth.simpermanalibrary.mana.ManaDataLimits;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class SimperManaConfig {
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.IntValue STARTING_MAX_MANA;

    public static final ModConfigSpec.DoubleValue
        STARTING_MANA_REGEN_PER_SECOND;

    static {
        ModConfigSpec.Builder builder =
            new ModConfigSpec.Builder();

        builder.push("mana");

        STARTING_MAX_MANA = builder
            .comment(
                "The maximum mana assigned to a new player."
            )
            .defineInRange(
                "startingMaxMana",
                40,
                0,
                ManaDataLimits.HARD_CAP
            );

        STARTING_MANA_REGEN_PER_SECOND = builder
            .comment(
                "The mana regenerated per second by a new player."
            )
            .defineInRange(
                "startingManaRegenPerSecond",
                1.0D,
                0.0D,
                1024.0D
            );

        builder.pop();

        SPEC = builder.build();
    }

    private SimperManaConfig() {}
}
