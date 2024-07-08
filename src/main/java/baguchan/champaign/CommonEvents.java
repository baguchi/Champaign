package baguchan.champaign;

import baguchan.champaign.attachment.ChampaignAttachment;
import baguchan.champaign.attachment.OwnerAttachment;
import baguchan.champaign.packet.AddMusicPacket;
import baguchan.champaign.registry.ModAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Champaign.MODID)
public class CommonEvents {

    @SubscribeEvent
    public static void onTarget(LivingChangeTargetEvent event) {
        LivingEntity entity = event.getEntity();
        OwnerAttachment attachment = entity.getData(ModAttachments.OWNER);
        if (event.getOriginalTarget() instanceof OwnableEntity ownableEntity) {
            if (attachment.getOwnerID() != null && attachment.getOwnerID() == ownableEntity.getOwnerUUID()) {
                event.setNewTarget(null);
                event.setCanceled(true);
            }
        }
        if (event.getOriginalTarget() != null && attachment.getOwnerID() != null && attachment.getOwnerID() == event.getOriginalTarget().getUUID()) {
            event.setNewTarget(null);
            event.setCanceled(true);
        }

        if (event.getOriginalTarget() != null && attachment.getOwnerID() != null) {
            OwnerAttachment attachment2 = event.getOriginalTarget().getData(ModAttachments.OWNER);
            if (attachment.getOwnerID() == attachment2.getOwnerID()) {
                event.setNewTarget(null);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        ChampaignAttachment attachment = player.getData(ModAttachments.CHAMPAIGN);
        if (player instanceof ServerPlayer serverPlayer) {
            attachment.getMusicList().forEach(musicSummon -> {
                PacketDistributor.sendToPlayer(serverPlayer, new AddMusicPacket(serverPlayer.getId(), musicSummon.value()));
            });
        }
    }

    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        ChampaignAttachment attachment = player.getData(ModAttachments.CHAMPAIGN);
        if (player instanceof ServerPlayer serverPlayer) {
            attachment.getMusicList().forEach(musicSummon -> {
                PacketDistributor.sendToPlayer(serverPlayer, new AddMusicPacket(serverPlayer.getId(), musicSummon.value()));
            });
        }
    }

    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        ChampaignAttachment attachment = player.getData(ModAttachments.CHAMPAIGN);
        if (player instanceof ServerPlayer serverPlayer) {
            attachment.getMusicList().forEach(musicSummon -> {
                PacketDistributor.sendToPlayer(serverPlayer, new AddMusicPacket(serverPlayer.getId(), musicSummon.value()));
            });
        }
    }
}
