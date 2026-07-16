package com.xevaryth.simpermanalibrary.api;

import com.xevaryth.simpermanalibrary.SimperManaLibrary;
import com.xevaryth.simpermanalibrary.registry.SimperManaAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.registries.DeferredHolder;

/** Stable identifiers and registry handles exposed to dependent mods. */
public final class SimperManaLibraryApi {
    public static final String MOD_ID = SimperManaLibrary.MOD_ID;
    public static final ResourceLocation MANA_ATTACHMENT_ID = id("mana");
    public static final ResourceLocation MAX_MANA_ATTRIBUTE_ID = id("max_mana");
    public static final ResourceLocation MANA_REGEN_ATTRIBUTE_ID =
        id("mana_regen_per_second");

    public static final DeferredHolder<Attribute, Attribute> MAX_MANA =
        SimperManaAttributes.MAX_MANA;
    public static final DeferredHolder<Attribute, Attribute> MANA_REGEN_PER_SECOND =
        SimperManaAttributes.MANA_REGEN_PER_SECOND;

    private SimperManaLibraryApi() {}

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
