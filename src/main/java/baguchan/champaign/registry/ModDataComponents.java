package baguchan.champaign.registry;

import baguchan.champaign.Champaign;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Champaign.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<EntityType<?>>> MUSIC_TYPE = register(
            "music_type",
            p_341845_ -> p_341845_.persistent(BuiltInRegistries.ENTITY_TYPE.byNameCodec()).networkSynchronized(ByteBufCodecs.fromCodec(BuiltInRegistries.ENTITY_TYPE.byNameCodec())).cacheEncoding()
    );

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String p_332092_, UnaryOperator<DataComponentType.Builder<T>> p_331261_) {
        return DATA_COMPONENT_TYPES.register(p_332092_, () -> p_331261_.apply(DataComponentType.builder()).build());
    }
}
