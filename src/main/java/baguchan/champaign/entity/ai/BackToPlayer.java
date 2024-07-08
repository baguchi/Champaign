package baguchan.champaign.entity.ai;

import baguchan.champaign.attachment.ChampaignAttachment;
import baguchan.champaign.entity.AbstractWorkerAllay;
import baguchan.champaign.registry.ModAttachments;
import baguchan.champaign.registry.ModMemorys;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Optional;
import java.util.UUID;

public class BackToPlayer extends Behavior<AbstractWorkerAllay> {
    private final float speedMultiplier;
    private boolean workOver = false;
    private BlockPos minPos;
    private BlockPos maxPos;
    private BlockPos currentBlockPos;

    public static final int RANGE = 2;

    public BackToPlayer(float p_275357_) {
        super(ImmutableMap.of(ModMemorys.WORK_POS.get(), MemoryStatus.VALUE_ABSENT, MemoryModuleType.LIKED_PLAYER, MemoryStatus.VALUE_PRESENT), 2400);
        this.speedMultiplier = p_275357_;
    }


    protected boolean canStillUse(ServerLevel p_147391_, AbstractWorkerAllay p_147392_, long p_147393_) {
        return true;
    }

    protected void start(ServerLevel p_147399_, AbstractWorkerAllay p_147400_, long p_147401_) {
    }

    protected void stop(ServerLevel p_217118_, AbstractWorkerAllay p_217119_, long p_217120_) {
        Brain<?> brain = p_217119_.getBrain();
        brain.eraseMemory(ModMemorys.WORK_POS.get());
    }

    protected void tick(ServerLevel level, AbstractWorkerAllay mob, long p_147405_) {
        Brain<?> brain = mob.getBrain();
        Optional<UUID> uuid = brain.getMemory(MemoryModuleType.LIKED_PLAYER);

        if (uuid.isPresent()) {
            Entity entity = level.getEntity(uuid.get());
            if (entity instanceof ServerPlayer serverPlayer) {
                if (serverPlayer.distanceTo(mob) < 1.5F) {
                    ChampaignAttachment attachment = serverPlayer.getData(ModAttachments.CHAMPAIGN);
                    attachment.setAllayCount(attachment.getSummonCount() + 1, serverPlayer);
                    mob.giveResource();
                    mob.discard();
                } else {
                    mob.getNavigation().moveTo(serverPlayer, this.speedMultiplier);
                }
            }
        }

    }

    @Override
    protected boolean timedOut(long gameTime) {
        return false;
    }
}