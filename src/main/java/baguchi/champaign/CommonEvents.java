package baguchi.champaign;

import baguchi.champaign.attachment.ChampaignAttachment;
import baguchi.champaign.attachment.OwnerAttachment;
import baguchi.champaign.packet.AddMusicPacket;
import baguchi.champaign.packet.SyncAllayPacket;
import baguchi.champaign.registry.ModAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Champaign.MODID)
public class CommonEvents {

    @SubscribeEvent
    public static void onTarget(LivingChangeTargetEvent event) {
        LivingEntity entity = event.getEntity();
        OwnerAttachment attachment = entity.getData(ModAttachments.OWNER);
        if (event.getOriginalAboutToBeSetTarget() instanceof OwnableEntity ownableEntity) {
            if (attachment.getOwnerID() != null && attachment.getOwnerID() == ownableEntity.getOwnerUUID()) {
                event.setNewAboutToBeSetTarget(null);
                event.setCanceled(true);
            }
        }
        if (event.getOriginalAboutToBeSetTarget() != null && attachment.getOwnerID() != null && attachment.getOwnerID() == event.getOriginalAboutToBeSetTarget().getUUID()) {
            event.setNewAboutToBeSetTarget(null);
            event.setCanceled(true);
        }

        if (event.getOriginalAboutToBeSetTarget() != null) {
            OwnerAttachment attachment1 = event.getOriginalAboutToBeSetTarget().getData(ModAttachments.OWNER);
            if (attachment.getOwnerID() != null && attachment.getOwnerID() == attachment1.getOwnerID()) {
                event.setNewAboutToBeSetTarget(null);
                event.setCanceled(true);
            }
        }

        if (event.getOriginalAboutToBeSetTarget() != null && attachment.getOwnerID() != null) {
            OwnerAttachment attachment2 = event.getOriginalAboutToBeSetTarget().getData(ModAttachments.OWNER);
            if (attachment.getOwnerID() == attachment2.getOwnerID()) {
                event.setNewAboutToBeSetTarget(null);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onTickTarget(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        OwnerAttachment attachment = entity.getData(ModAttachments.OWNER);
        if (attachment.getOwnerID() != null) {
            Player player = entity.level().getPlayerByUUID(attachment.getOwnerID());

            if (player != null && entity instanceof Mob livingEntity && player.getLastHurtByMob() != null) {
                livingEntity.setTarget(player.getLastHurtByMob());
            } else if (player != null && entity instanceof Mob livingEntity && player.getLastHurtMob() != null) {
                livingEntity.setTarget(player.getLastHurtMob());
            }
        }
    }

    @SubscribeEvent
    public static void progressAdvancement(AdvancementEvent.AdvancementProgressEvent event) {
        Player player = event.getEntity();
        ChampaignAttachment attachment2 = player.getData(ModAttachments.CHAMPAIGN);

        attachment2.trackDiscoveries(player, event.getAdvancement());
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        ChampaignAttachment attachment = player.getData(ModAttachments.CHAMPAIGN);
        if (player instanceof ServerPlayer serverPlayer) {
            attachment.getMusicList().forEach(musicSummon -> {
                PacketDistributor.sendToPlayer(serverPlayer, new AddMusicPacket(serverPlayer.getId(), musicSummon.value(), false));
            });
            PacketDistributor.sendToPlayer(serverPlayer, new SyncAllayPacket(serverPlayer.getId(), attachment.getAllayCount(), attachment.getMaxAllayCount()));

        }
    }

    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        ChampaignAttachment attachment = player.getData(ModAttachments.CHAMPAIGN);
        if (player instanceof ServerPlayer serverPlayer) {
            attachment.getMusicList().forEach(musicSummon -> {
                PacketDistributor.sendToPlayer(serverPlayer, new AddMusicPacket(serverPlayer.getId(), musicSummon.value(), false));
            });
            PacketDistributor.sendToPlayer(serverPlayer, new SyncAllayPacket(serverPlayer.getId(), attachment.getAllayCount(), attachment.getMaxAllayCount()));

        }
    }

    @SubscribeEvent
    public static void onRespawnDimension(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        ChampaignAttachment attachment = player.getData(ModAttachments.CHAMPAIGN);
        if (player instanceof ServerPlayer serverPlayer) {
            attachment.getMusicList().forEach(musicSummon -> {
                PacketDistributor.sendToPlayer(serverPlayer, new AddMusicPacket(serverPlayer.getId(), musicSummon.value(), false));
            });

            PacketDistributor.sendToPlayer(serverPlayer, new SyncAllayPacket(serverPlayer.getId(), attachment.getAllayCount(), attachment.getMaxAllayCount()));

        }
    }
}
