package baguchan.champaign.music;

import baguchan.champaign.Champaign;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public record MusicSummon(EntityType<?> entityType, int summonCost) {
    public static final Codec<MusicSummon> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("target").forGetter(instance2 -> instance2.getEntityType()),
                    Codec.INT.fieldOf("summon_cost").forGetter(instance2 -> instance2.summonCost))
            .apply(instance, MusicSummon::new)
    );

    public static final ResourceKey<Registry<MusicSummon>> REGISTRY_KEY = ResourceKey
            .createRegistryKey(ResourceLocation.fromNamespaceAndPath(Champaign.MODID, "music_summon"));


    public EntityType<?> getEntityType() {
        return entityType;
    }
}