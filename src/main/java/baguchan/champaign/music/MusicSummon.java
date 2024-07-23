package baguchan.champaign.music;

import baguchan.champaign.Champaign;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.Optional;

public record MusicSummon(EntityType<?> entityType, int summonCost, Optional<ResourceLocation> learning_advancement) {
    public static final ResourceKey<Registry<MusicSummon>> REGISTRY_KEY = ResourceKey
            .createRegistryKey(ResourceLocation.fromNamespaceAndPath(Champaign.MODID, "music_summon"));

    public static final Codec<MusicSummon> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("target").forGetter(instance2 -> instance2.getEntityType()),
                    Codec.INT.fieldOf("summon_cost").forGetter(instance2 -> instance2.summonCost),
                    ResourceLocation.CODEC.optionalFieldOf("learning_advancement").forGetter(MusicSummon::learning_advancement))
            .apply(instance, MusicSummon::new)
    );


    public static final Codec<Holder<MusicSummon>> REFERENCE_CODEC = RegistryFileCodec.create(MusicSummon.REGISTRY_KEY, CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<MusicSummon>> STREAM_CODEC = ByteBufCodecs.holderRegistry(REGISTRY_KEY);


    public EntityType<?> getEntityType() {
        return entityType;
    }

    public Optional<ResourceLocation> getLearningAdvancement() {
        return learning_advancement;
    }
}