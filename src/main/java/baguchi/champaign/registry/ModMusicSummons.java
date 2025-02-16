package baguchi.champaign.registry;

import baguchi.champaign.Champaign;
import baguchi.champaign.music.MusicSummon;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class ModMusicSummons {
    public static final DeferredRegister<MusicSummon> MUSIC_SUMMON = DeferredRegister.create(MusicSummon.REGISTRY_KEY,
            Champaign.MODID);
    public static final Supplier<IForgeRegistry<MusicSummon>> MUSIC_SUMMON_REGISTRY = MUSIC_SUMMON.makeRegistry(
            () -> new RegistryBuilder<MusicSummon>().disableSaving());
    public static ResourceKey<MusicSummon> key(ResourceLocation name) {
        return ResourceKey.create(MusicSummon.REGISTRY_KEY, name);
    }

}