package baguchan.champaign.attachment;

import baguchan.champaign.Champaign;
import baguchan.champaign.entity.AbstractWorkerAllay;
import baguchan.champaign.music.MusicSummon;
import baguchan.champaign.packet.AddMusicPacket;
import baguchan.champaign.registry.ModAttachments;
import baguchan.champaign.registry.ModMemorys;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Optional;

public class ChampaignAttachment implements INBTSerializable<CompoundTag> {
    private final List<Holder<MusicSummon>> musicList = Lists.newArrayList();
    private int musicIndex;
    private boolean sync;


    public void summonEntity(ServerPlayer player) {
        ServerLevel serverLevel = player.serverLevel();
        if (getMusicList().get(this.musicIndex) != null && !getMusicList().isEmpty()) {
            Holder<MusicSummon> musicSummon = getMusicList().get(this.musicIndex);
            int count = countLapis(player);
            if (musicSummon.value().summonCost() <= count || player.isCreative()) {

                Entity entity = getMusicList().get(this.musicIndex).value().getEntityType().create(serverLevel);

                OwnerAttachment ownerAttachment = entity.getData(ModAttachments.OWNER);
                ownerAttachment.setOwnerID(player.getUUID());
                entity.setPos(player.position());
                if (entity instanceof Mob mob) {
                    mob.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(player.blockPosition()), MobSpawnType.MOB_SUMMONED, null);
                    mob.dropPreservedEquipment();
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
        if (!player.level().isClientSide()) {
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new AddMusicPacket(player, music.value(), true));
        }
    }

    public void trackDiscoveries(Player player, AdvancementHolder advancement) {
        if (player instanceof ServerPlayer serverPlayer) {
            RegistryAccess registryAccess = serverPlayer.registryAccess();
            this.trackMusicEntries(serverPlayer, registryAccess, advancement);
            if (this.sync) {

                this.sync = false;
            }
        }
    }

    private void trackMusicEntries(ServerPlayer serverPlayer, RegistryAccess registryAccess, AdvancementHolder advancement) {
        Registry<MusicSummon> bestiaryEntries = registryAccess.registryOrThrow(MusicSummon.REGISTRY_KEY);
        for (Holder.Reference<MusicSummon> entry : bestiaryEntries.holders().toList()) {
            if (entry.value().learning_advancement().isPresent() && advancement.id().equals(entry.value().learning_advancement().get()) && !this.musicList.contains(entry)) {
                this.musicList.add(entry);
                PacketDistributor.sendToPlayer(serverPlayer, new AddMusicPacket(serverPlayer, entry.value(), true));

                this.sync = true;
            }
        }
        if (this.sync) {
            //PacketDistributor.sendToPlayer(serverPlayer, new GuidebookToastPacket(GuidebookToast.Icons.BESTIARY, "gui.aether_ii.toast.guidebook.bestiary", "gui.aether_ii.toast.guidebook.description"));
        }
    }

    public List<Holder<MusicSummon>> getMusicList() {
        return musicList;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
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
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        ListTag list = nbt.contains("LearnedEntity") ? nbt.getList("LearnedEntity", 10) : new ListTag();


        musicList.clear();
        for (int i = 0; i < list.size(); ++i) {
            CompoundTag compoundnbt = list.getCompound(i);

            Optional<Holder.Reference<MusicSummon>> musicSummon = Champaign.registryAccess().registryOrThrow(MusicSummon.REGISTRY_KEY).getHolder(ResourceLocation.parse(compoundnbt.getString("Music")));
            //check mob enchant is not null
            musicSummon.ifPresent(musicList::add);
        }
    }
}