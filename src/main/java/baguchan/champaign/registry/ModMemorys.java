package baguchan.champaign.registry;

import baguchan.champaign.Champaign;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

public class ModMemorys {
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_REGISTRY = DeferredRegister.create(BuiltInRegistries.MEMORY_MODULE_TYPE, Champaign.MODID);

    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<GlobalPos>> WORK_POS = MEMORY_REGISTRY.register("work_pos", () -> new MemoryModuleType<>(Optional.of(GlobalPos.CODEC)));
}