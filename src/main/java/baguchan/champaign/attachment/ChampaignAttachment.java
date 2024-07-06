package baguchan.champaign.attachment;

import baguchan.champaign.entity.AbstractWorkerAllay;
import baguchan.champaign.entity.GatherAllay;
import baguchan.champaign.packet.SyncAllayPacket;
import baguchan.champaign.registry.ModEntities;
import baguchan.champaign.registry.ModMemorys;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class ChampaignAttachment implements INBTSerializable<CompoundTag> {
    private boolean champain;
    private int allayCount = 3;
    private int maxAllayCount = 3;

    public void summonAllay(ServerPlayer player) {
        Vec3 vec3 = player.getEyePosition();
        Vec3 vec31 = player.getViewVector(1.0F);
        double d0 = player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);
        Vec3 vec32 = vec3.add(vec31.x * d0, vec31.y * d0, vec31.z * d0);
        ServerLevel serverLevel = player.serverLevel();

        HitResult hitResult = player.pick(20.0D, 0.0F, false);

        Vec3 pos = hitResult.getLocation();
        if (hitResult.getType() != HitResult.Type.MISS) {
            if (allayCount > 0) {
                hitResult = BlockHitResult.miss(pos, Direction.getNearest(vec3.x, vec3.y, vec3.z), BlockPos.containing(pos));

                if (hitResult instanceof BlockHitResult blockHitResult) {
                    BlockPos blockpos = blockHitResult.getBlockPos();
                    if (!serverLevel.isClientSide) {
                        GatherAllay thrownpotion = ModEntities.GATHER_ALLAY.get().create(serverLevel);
                        thrownpotion.getBrain().setMemory(MemoryModuleType.LIKED_PLAYER, player.getUUID());
                        thrownpotion.setPos(player.position());
                        thrownpotion.getBrain().setMemory(ModMemorys.WORK_POS.get(), GlobalPos.of(serverLevel.dimension(), blockpos));
                        serverLevel.addFreshEntity(thrownpotion);
                    }

                    this.setAllayCount(this.getAllayCount() - 1, player);
                    player.playSound(SoundEvents.ALLAY_ITEM_GIVEN);
                }
            }
        }
    }

    public void callAllay(ServerPlayer player) {
        Vec3 vec3 = player.getEyePosition();
        Vec3 vec31 = player.getViewVector(1.0F);
        double d0 = player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);
        Vec3 vec32 = vec3.add(vec31.x * d0, vec31.y * d0, vec31.z * d0);
        ServerLevel serverLevel = player.serverLevel();

        HitResult hitResult = player.pick(20.0D, 0.0F, false);

        Vec3 pos = hitResult.getLocation();

        List<AbstractWorkerAllay> list = serverLevel.getEntitiesOfClass(AbstractWorkerAllay.class, new AABB(new BlockPos((int) pos.x, (int) pos.y, (int) pos.z)).inflate(8.0F), predicate -> {
            return predicate.isReturnOwner(player);
        });

        list.forEach(allay -> {
            allay.getBrain().eraseMemory(ModMemorys.WORK_POS.get());
        });
    }

    public void setAllayCount(int allayCount, Player player) {
        this.allayCount = allayCount;
        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new SyncAllayPacket(player.getId(), this.allayCount, this.maxAllayCount));
        }
    }

    public int getAllayCount() {
        return allayCount;
    }

    public int getMaxAllayCount() {
        return maxAllayCount;
    }

    public void setMaxAllayCount(int maxAllayCount, Player player) {
        this.maxAllayCount = maxAllayCount;
        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new SyncAllayPacket(player.getId(), this.allayCount, this.maxAllayCount));
        }
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("AllayCount", this.allayCount);
        nbt.putInt("MaxAllayCount", this.maxAllayCount);
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.allayCount = nbt.getInt("AllayCount");
        this.maxAllayCount = nbt.getInt("MaxAllayCount");
    }
}