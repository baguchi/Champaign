package baguchi.champaign.attachment;

import baguchi.champaign.Champaign;
import baguchi.champaign.entity.AbstractWorkerAllay;
import baguchi.champaign.entity.GatherAllay;
import baguchi.champaign.music.MusicSummon;
import baguchi.champaign.packet.AddMusicPacket;
import baguchi.champaign.packet.SyncAllayPacket;
import baguchi.champaign.registry.ModEntities;
import baguchi.champaign.registry.ModMemorys;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.compress.utils.Lists;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ChampaignAttachment implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
    private final List<Holder<MusicSummon>> musicList = Lists.newArrayList();
    private int musicIndex;
    private boolean sync;
    private int allayCount = 3;
    private int maxAllayCount = 3;

    public void summonAllay(ServerPlayer player) {
        Vec3 vec3 = player.getEyePosition();
        Vec3 vec31 = player.getViewVector(1.0F);
        double d0 = player.getAttributeValue(ForgeMod.ENTITY_REACH.get());
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
        double d0 = player.getAttributeValue(ForgeMod.ENTITY_REACH.get());
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
            Champaign.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SyncAllayPacket(player.getId(), this.allayCount, this.maxAllayCount));
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
            Champaign.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SyncAllayPacket(player.getId(), this.allayCount, this.maxAllayCount));
        }
    }

    public void summonEntity(ServerPlayer player) {
        ServerLevel serverLevel = player.serverLevel();
        if (!getMusicList().isEmpty() && getMusicList().get(this.musicIndex) != null) {
            Holder<MusicSummon> musicSummon = getMusicList().get(this.musicIndex);
            int count = countLapis(player);
            if (musicSummon.value().summonCost() <= count || player.isCreative()) {

                Entity entity = getMusicList().get(this.musicIndex).value().getEntityType().create(serverLevel);

                OwnerAttachment ownerAttachment = entity.getCapability(Champaign.OWNER_CAPABILITY).orElse(new OwnerAttachment());
                ownerAttachment.setOwnerID(player.getUUID());
                entity.setPos(player.position());
                if (entity instanceof Mob mob) {
                    mob.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(player.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                    for (EquipmentSlot equipmentslot : EquipmentSlot.values()) {
                        mob.setDropChance(equipmentslot, 0.0F);
                    }
                }
                if (!player.isCreative()) {
                    player.getInventory().clearOrCountMatchingItems(predicate -> {
                        return predicate.getItem() == Items.LAPIS_LAZULI;
                    }, musicSummon.value().summonCost(), player.getInventory());
                }
                serverLevel.addFreshEntity(entity);
                serverLevel.playSound(null, player, SoundEvents.NOTE_BLOCK_GUITAR.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
            } else {
                serverLevel.playSound(null, player, SoundEvents.THORNS_HIT, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        } else {
            serverLevel.playSound(null, player, SoundEvents.THORNS_HIT, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    private int countLapis(ServerPlayer serverPlayer) {
        int count = 0;
        for (int i = 0; i < serverPlayer.getInventory().getContainerSize(); ++i) {
            ItemStack itemstack = serverPlayer.getInventory().getItem(i);
            if (!itemstack.isEmpty() && itemstack.is(Items.LAPIS_LAZULI)) {
                count += itemstack.getCount();
            }
        }
        return count;
    }


    public boolean cycle() {
        return cycle(1);
    }

    public boolean cycle(boolean clockWise) {
        return cycle(clockWise ? 1 : -1);
    }

    public boolean cycle(int slotsMoved) {
        int originalSlot = this.musicIndex;
        var content = this.getMusicList();
        if (slotsMoved == 0) {
            if (content.isEmpty()) return false;
        }
        int maxSlots = content.size();
        if (maxSlots <= 0) {
            return false;
        }
        slotsMoved = slotsMoved % maxSlots;
        setMusicIndex((maxSlots + (getMusicIndex() + slotsMoved)) % maxSlots);
        for (int i = 0; i < maxSlots; i++) {
            setMusicIndex((maxSlots + (getMusicIndex() + (slotsMoved >= 0 ? 1 : -1))) % maxSlots);
        }
        return originalSlot != getMusicIndex();
    }

    public int getMusicIndex() {
        return musicIndex;
    }

    public void setMusicIndex(int musicIndex) {
        this.musicIndex = musicIndex;
    }

    public void addMusicList(Holder<MusicSummon> music, Player player) {
        this.musicList.add(music);
        if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
            Champaign.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new AddMusicPacket(player, music.value(), true));
        }
    }

    public void trackDiscoveries(Player player, Advancement advancement) {
        if (player instanceof ServerPlayer serverPlayer) {
            RegistryAccess registryAccess = serverPlayer.serverLevel().registryAccess();
            this.trackMusicEntries(serverPlayer, registryAccess, advancement);
            if (this.sync) {

                this.sync = false;
            }
        }
    }

    private void trackMusicEntries(ServerPlayer serverPlayer, RegistryAccess registryAccess, Advancement advancement) {
        Registry<MusicSummon> musicSummons = registryAccess.registryOrThrow(MusicSummon.REGISTRY_KEY);
        for (Holder.Reference<MusicSummon> entry : musicSummons.holders().toList()) {
            if (entry.value().learning_advancement().isPresent() && advancement.getId().equals(entry.value().learning_advancement().get()) && !this.musicList.contains(entry)) {
                this.musicList.add(entry);
                Champaign.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new AddMusicPacket(serverPlayer, entry.value(), true));
            }
        }
    }

    public List<Holder<MusicSummon>> getMusicList() {
        return musicList;
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        return (capability == Champaign.CHAMPAIGN_CAPABILITY) ? LazyOptional.of(() -> this).cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();

        ListTag listnbt = new ListTag();

        for (int i = 0; i < musicList.size(); i++) {
            CompoundTag compoundTag = new CompoundTag();
            ResourceLocation resourceLocation = Champaign.registryAccess().registryOrThrow(MusicSummon.REGISTRY_KEY).getKey(musicList.get(i).value());
            if (resourceLocation != null) {
                compoundTag.putString("Music", resourceLocation.toString());
            }
            listnbt.add(compoundTag);
        }

        nbt.put("LearnedEntity", listnbt);
        nbt.putInt("AllayCount", this.allayCount);
        nbt.putInt("MaxAllayCount", this.maxAllayCount);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ListTag list = nbt.contains("LearnedEntity") ? nbt.getList("LearnedEntity", 10) : new ListTag();
        this.allayCount = nbt.getInt("AllayCount");
        this.maxAllayCount = nbt.getInt("MaxAllayCount");

        musicList.clear();
        for (int i = 0; i < list.size(); ++i) {
            CompoundTag compoundnbt = list.getCompound(i);

            Optional<Holder.Reference<MusicSummon>> musicSummon = Champaign.registryAccess().registryOrThrow(MusicSummon.REGISTRY_KEY).getHolder(ResourceKey.create(MusicSummon.REGISTRY_KEY, ResourceLocation.tryParse(compoundnbt.getString("Music"))));
            //check mob enchant is not null
            musicSummon.ifPresent(musicList::add);
        }
    }
}