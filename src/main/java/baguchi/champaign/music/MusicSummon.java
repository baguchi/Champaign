package baguchi.champaign.music;

import baguchi.champaign.Champaign;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.Optional;

public record MusicSummon(EntityType<?> entityType, int summonCost, Optional<ResourceLocation> learning_advancement) {
    public static final ResourceKey<Registry<MusicSummon>> REGISTRY_KEY = ResourceKey
            .createRegistryKey(new ResourceLocation(Champaign.MODID, "music_summon"));

    public static final Codec<MusicSummon> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("target").forGetter(instance2 -> instance2.getEntityType()),
                    Codec.INT.fieldOf("summon_cost").forGetter(instance2 -> instance2.summonCost),
                    ResourceLocation.CODEC.optionalFieldOf("learning_advancement").forGetter(MusicSummon::learning_advancement))
            .apply(instance, MusicSummon::new)
    );

    public EntityType<?> getEntityType() {
        return entityType;
    }

    public Optional<ResourceLocation> getLearningAdvancement() {
        return learning_advancement;
    }
}