package com.xevaryth.simpermanalibrary.mana;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xevaryth.simpermanalibrary.SimperManaLibrary;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class ManaAttachments {
    private ManaAttachments() {}

    private static final Codec<ManaData> CODEC =
        RecordCodecBuilder.create(instance ->
            instance.group(
                Codec.INT
                    .optionalFieldOf("currentMana", 0)
                    .forGetter(ManaData::currentMana),

                Codec.BOOL
                    .optionalFieldOf(
                        "attributesInitialized",
                        false
                    )
                    .forGetter(
                        ManaData::attributesInitialized
                    )
            ).apply(
                instance,
                (currentMana, initialized) -> {
                    ManaData data = new ManaData();

                    data.setCurrentMana(
                        currentMana,
                        ManaDataLimits.HARD_CAP
                    );

                    if (initialized) {
                        data.markAttributesInitialized();
                    }

                    return data;
                }
            )
        );

    private static final StreamCodec<
        RegistryFriendlyByteBuf,
        ManaData
        > STREAM_CODEC = new StreamCodec<>() {
        @Override
        public ManaData decode(
            RegistryFriendlyByteBuf buffer
        ) {
            int currentMana = buffer.readVarInt();
            boolean initialized = buffer.readBoolean();

            ManaData data = new ManaData();

            data.setCurrentMana(
                currentMana,
                ManaDataLimits.HARD_CAP
            );

            if (initialized) {
                data.markAttributesInitialized();
            }

            return data;
        }

        @Override
        public void encode(
            RegistryFriendlyByteBuf buffer,
            ManaData data
        ) {
            buffer.writeVarInt(data.currentMana());
            buffer.writeBoolean(
                data.attributesInitialized()
            );
        }
    };

    private static final DeferredRegister<
        AttachmentType<?>
        > ATTACHMENTS = DeferredRegister.create(
        NeoForgeRegistries.Keys.ATTACHMENT_TYPES,
        SimperManaLibrary.MOD_ID
    );

    public static final Supplier<
        AttachmentType<ManaData>
        > MANA = ATTACHMENTS.register(
        "mana",
        () -> AttachmentType
            .builder(ManaData::new)
            .serialize(CODEC)
            .sync(
                (holder, player) -> holder == player,
                STREAM_CODEC
            )
            .copyOnDeath()
            .build()
    );

    public static void register(IEventBus modBus) {
        ATTACHMENTS.register(modBus);
    }
}
