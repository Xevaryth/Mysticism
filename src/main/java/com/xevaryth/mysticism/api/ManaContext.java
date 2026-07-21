package com.xevaryth.mysticism.api;

import java.util.Objects;
import net.minecraft.resources.ResourceLocation;

public record ManaContext(ResourceLocation sourceId, ManaChangeReason reason) {
    public ManaContext {
        Objects.requireNonNull(sourceId, "sourceId");
        Objects.requireNonNull(reason, "reason");
    }

    public static ManaContext api() {
        return new ManaContext(
            ResourceLocation.fromNamespaceAndPath("mysticism", "api"),
            ManaChangeReason.API
        );
    }
}
