package baguchan.champaign.entity;

import baguchan.champaign.registry.ModMemorys;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.level.Level;

public class GatherAllay extends AbstractWorkerAllay {
    public static final ImmutableList<SensorType<? extends Sensor<? super GatherAllay>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY, SensorType.NEAREST_ITEMS);
    public static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.PATH, MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.HURT_BY, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.LIKED_PLAYER, MemoryModuleType.IS_PANICKING, ModMemorys.WORK_POS.get());


    public GatherAllay(EntityType<? extends GatherAllay> p_218310_, Level p_218311_) {
        super(p_218310_, p_218311_);
    }

    protected Brain.Provider<GatherAllay> brainAllayProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    protected Brain<?> makeBrain(Dynamic<?> p_218344_) {
        return GatherAllayAi.makeBrain(this.brainAllayProvider().makeBrain(p_218344_));
    }

    public Brain<GatherAllay> getBrain() {
        return (Brain<GatherAllay>) super.getBrain();
    }

    protected void customServerAiStep() {
        this.level().getProfiler().push("allayBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        this.level().getProfiler().pop();
        this.level().getProfiler().push("allayActivityUpdate");
        GatherAllayAi.updateActivity(this);
        this.level().getProfiler().pop();
        super.customServerAiStep();
    }
}
