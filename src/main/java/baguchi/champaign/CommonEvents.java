package baguchi.champaign;

import baguchi.champaign.attachment.ChampaignAttachment;
import baguchi.champaign.attachment.OwnerAttachment;
import baguchi.champaign.packet.AddMusicPacket;
import baguchi.champaign.packet.SyncAllayPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = Champaign.MODID)
public class CommonEvents {

    @SubscribeEvent
    public static void onRegisterEntityCapabilities(RegisterCapabilitiesEvent event) {
        event.register(ChampaignAttachment.class);
        event.register(OwnerAttachment.class);
    }

    @SubscribeEvent
    public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            event.addCapability(new ResourceLocation(Champaign.MODID, "owner"), new OwnerAttachment());
        }
        if (event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation(Champaign.MODID, "champaign"), new ChampaignAttachment());
        }
    }

    @SubscribeEvent
    public static void onTarget(LivingChangeTargetEvent event) {
        LivingEntity entity = event.getEntity();
        OwnerAttachment attachment = entity.getCapability(Champaign.OWNER_CAPABILITY).orElse(new OwnerAttachment());
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
            OwnerAttachment attachment2 = event.getOriginalTarget().getCapability(Champaign.OWNER_CAPABILITY).orElse(new OwnerAttachment());
            if (attachment.getOwnerID() == attachment2.getOwnerID()) {
                event.setNewTarget(null);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onTickTarget(LivingEvent.LivingTickEvent event) {
        Entity entity = event.getEntity();
        OwnerAttachment attachment = entity.getCapability(Champaign.OWNER_CAPABILITY).orElse(new OwnerAttachment());
        if (attachment.getOwnerID() != null) {
            Player player = entity.level().getPlayerByUUID(attachment.getOwnerID());
            if (player != null && entity instanceof Mob livingEntity && player.getLastHurtByMob() != null) {
                livingEntity.setTarget(player.getLastHurtByMob());
            }
        }
    }

    @SubscribeEvent
    public static void progressAdvancement(AdvancementEvent.AdvancementProgressEvent event) {
        Player player = event.getEntity();
        ChampaignAttachment attachment2 = player.getCapability(Champaign.CHAMPAIGN_CAPABILITY).orElse(new ChampaignAttachment());

        attachment2.trackDiscoveries(player, event.getAdvancement());
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        ChampaignAttachment attachment = player.getCapability(Champaign.CHAMPAIGN_CAPABILITY).orElse(new ChampaignAttachment());
        if (player instanceof ServerPlayer serverPlayer) {
            attachment.getMusicList().forEach(musicSummon -> {
                Champaign.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new AddMusicPacket(serverPlayer.getId(), musicSummon.value(), false));
            });
            Champaign.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SyncAllayPacket(serverPlayer.getId(), attachment.getAllayCount(), attachment.getMaxAllayCount()));

        }
    }

    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        ChampaignAttachment attachment = player.getCapability(Champaign.CHAMPAIGN_CAPABILITY).orElse(new ChampaignAttachment());
        if (player instanceof ServerPlayer serverPlayer) {
            attachment.getMusicList().forEach(musicSummon -> {
                Champaign.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new AddMusicPacket(serverPlayer.getId(), musicSummon.value(), false));
            });
            Champaign.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SyncAllayPacket(serverPlayer.getId(), attachment.getAllayCount(), attachment.getMaxAllayCount()));

        }
    }

    @SubscribeEvent
    public static void onRespawnDimension(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        ChampaignAttachment attachment = player.getCapability(Champaign.CHAMPAIGN_CAPABILITY).orElse(new ChampaignAttachment());
        if (player instanceof ServerPlayer serverPlayer) {
            attachment.getMusicList().forEach(musicSummon -> {
                Champaign.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new AddMusicPacket(serverPlayer.getId(), musicSummon.value(), false));
            });

            Champaign.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SyncAllayPacket(serverPlayer.getId(), attachment.getAllayCount(), attachment.getMaxAllayCount()));

        }
    }
}
