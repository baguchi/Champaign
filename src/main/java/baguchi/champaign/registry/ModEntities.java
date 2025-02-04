package baguchi.champaign.registry;

import baguchi.champaign.Champaign;
import baguchi.champaign.entity.GatherAllay;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = Champaign.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES_REGISTRY = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Champaign.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<GatherAllay>> GATHER_ALLAY = ENTITIES_REGISTRY.register("gather_allay", () -> EntityType.Builder.of(GatherAllay::new, MobCategory.CREATURE).sized(0.6F, 0.6F).build(prefix("gather_allay")));

    private static String prefix(String path) {
        return Champaign.MODID + "." + path;
    }

    @SubscribeEvent
    public static void registerEntity(EntityAttributeCreationEvent event) {
        event.put(GATHER_ALLAY.get(), Allay.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacement(RegisterSpawnPlacementsEvent event) {
        event.register(GATHER_ALLAY.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
    }
}