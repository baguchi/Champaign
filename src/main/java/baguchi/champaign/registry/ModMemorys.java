package baguchi.champaign.registry;

import baguchi.champaign.Champaign;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

public class ModMemorys {
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_REGISTRY = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, Champaign.MODID);

    public static final RegistryObject<MemoryModuleType<GlobalPos>> WORK_POS = MEMORY_REGISTRY.register("work_pos", () -> new MemoryModuleType<>(Optional.of(GlobalPos.CODEC)));
}