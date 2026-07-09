package com.xevaryth.mysticism.mana;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xevaryth.mysticism.Mysticism;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class ManaAttachments {
    private ManaAttachments() {}

    private static final Codec<ManaData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("currentMana").forGetter(ManaData::currentMana),
            Codec.INT.fieldOf("maxMana").forGetter(ManaData::maxMana)
    ).apply(instance, (currentMana, maxMana) -> {
        ManaData data = new ManaData();
        data.setMaxMana(maxMana);
        data.setCurrentMana(currentMana);
        return data;
    }));

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Mysticism.MODID);

    public static final Supplier<AttachmentType<ManaData>> MANA = ATTACHMENTS.register("mana", () ->
            AttachmentType.builder(ManaData::new)
                    .serialize(CODEC)
                    .copyOnDeath()
                    .build()
    );

    public static void register(IEventBus modBus) {
        ATTACHMENTS.register(modBus);
    }
}
