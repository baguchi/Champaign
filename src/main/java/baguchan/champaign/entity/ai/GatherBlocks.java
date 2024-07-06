package baguchan.champaign.entity.ai;

import baguchan.champaign.entity.AbstractWorkerAllay;
import baguchan.champaign.registry.ModMemorys;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class GatherBlocks extends Behavior<AbstractWorkerAllay> {
    private final float speedMultiplier;
    private boolean workOver = false;
    private BlockPos minPos;
    private BlockPos maxPos;
    private BlockPos currentBlockPos;

    public static final int RANGE = 2;

    public GatherBlocks(float p_275357_) {
        super(ImmutableMap.of(ModMemorys.WORK_POS.get(), MemoryStatus.VALUE_PRESENT), 2400);
        this.speedMultiplier = p_275357_;
    }


    protected boolean canStillUse(ServerLevel p_147391_, AbstractWorkerAllay p_147392_, long p_147393_) {
        return !workOver;
    }

    protected void start(ServerLevel p_147399_, AbstractWorkerAllay p_147400_, long p_147401_) {
        this.workOver = false;
        Brain<?> brain = p_147400_.getBrain();
        GlobalPos globalPos = brain.getMemory(ModMemorys.WORK_POS.get()).get();
        minPos = globalPos.pos().offset(-RANGE, -RANGE, -RANGE);
        maxPos = globalPos.pos().offset(RANGE, RANGE, RANGE);
    }

    protected void stop(ServerLevel p_217118_, AbstractWorkerAllay p_217119_, long p_217120_) {
        Brain<?> brain = p_217119_.getBrain();
        brain.eraseMemory(ModMemorys.WORK_POS.get());
    }

    protected void tick(ServerLevel level, AbstractWorkerAllay mob, long p_147405_) {
        Brain<?> brain = mob.getBrain();
        Optional<GlobalPos> globalPos = brain.getMemory(ModMemorys.WORK_POS.get());

        if (globalPos.isPresent()) {
            if (currentBlockPos == null) {
                BlockPos closest = null;
                for (BlockPos pos : BlockPos.betweenClosed(minPos, maxPos)) {
                    if (this.isGatherable(level.getBlockState(pos), level, pos, mob) && (closest == null || mob.distanceToSqr(Vec3.atCenterOf(pos)) < mob.distanceToSqr(Vec3.atCenterOf(closest)))) {
                        closest = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
                    }
                }

                if (closest != null) {
                    currentBlockPos = closest;
                } else {
                    workOver = true;
                }
            } else {
                if (currentBlockPos != null) {
                    if (mob.getNavigation().isDone()) {
                        brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(currentBlockPos, this.speedMultiplier, 1));

                    }
                    if (currentBlockPos.distSqr(mob.blockPosition()) < 2.5F) {
                        if (this.isGatherable(level.getBlockState(currentBlockPos), level, currentBlockPos, mob)) {
                            level.destroyBlock(currentBlockPos, true);
                        } else {
                            currentBlockPos = null;
                        }
                    }
                }
            }
        }

    }

    private boolean isGatherable(BlockState blockState, ServerLevel level, BlockPos pos, AbstractWorkerAllay mob) {
        return !blockState.isAir() && blockState.getDestroySpeed(level, pos) >= 0.0 && blockState.getDestroySpeed(level, pos) <= 10F;
    }
}