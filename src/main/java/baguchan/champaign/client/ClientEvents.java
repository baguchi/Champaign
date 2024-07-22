package baguchan.champaign.client;

import bagu_chan.bagus_lib.animation.BaguAnimationController;
import bagu_chan.bagus_lib.api.client.IRootModel;
import bagu_chan.bagus_lib.client.event.BagusModelEvent;
import bagu_chan.bagus_lib.util.client.AnimationUtil;
import baguchan.champaign.Champaign;
import baguchan.champaign.attachment.ChampaignAttachment;
import baguchan.champaign.client.animation.LuteAnimation;
import baguchan.champaign.packet.ChangeMusicSlotPacket;
import baguchan.champaign.packet.SummonPacket;
import baguchan.champaign.registry.ModAnimations;
import baguchan.champaign.registry.ModAttachments;
import baguchan.champaign.registry.ModItems;
import baguchan.champaign.registry.ModKeyMappings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = Champaign.MODID, value = Dist.CLIENT)
public class ClientEvents {
    public static int pressSummonTick;

    @SubscribeEvent
    public static void onInitModel(BagusModelEvent.Init event) {
        IRootModel rootModel = event.getRootModel();
        if (rootModel != null) {
            rootModel.getBagusRoot().getAllParts().forEach(ModelPart::resetPose);
        }
    }

    @SubscribeEvent
    public static void onAnimateModel(BagusModelEvent.PostAnimate event) {
        Entity entity = event.getEntity();
        IRootModel rootModel = event.getRootModel();
        BaguAnimationController controller = AnimationUtil.getAnimationController(entity);
        if (rootModel != null && controller != null && entity instanceof LivingEntity livingEntity) {
            boolean flag = livingEntity.getMainArm() == HumanoidArm.RIGHT;
            HumanoidArm humanoidArm = flag ? livingEntity.getMainHandItem().is(ModItems.LUTE.get()) ? HumanoidArm.RIGHT : livingEntity.getOffhandItem().is(ModItems.LUTE.get()) ? HumanoidArm.LEFT : HumanoidArm.RIGHT : HumanoidArm.LEFT;

            if (livingEntity.isHolding(ModItems.LUTE.get())) {
                if (controller.getAnimationState(ModAnimations.PLAYING_LUTE).isStarted()) {
                    if (humanoidArm == HumanoidArm.RIGHT) {
                        rootModel.animateBagu(controller.getAnimationState(ModAnimations.PLAYING_LUTE), LuteAnimation.lute_playing_right, event.getAgeInTick());
                    } else {
                        rootModel.animateBagu(controller.getAnimationState(ModAnimations.PLAYING_LUTE), LuteAnimation.lute_playing_left, event.getAgeInTick());
                    }
                } else {
                    if (humanoidArm == HumanoidArm.RIGHT) {
                        rootModel.applyStaticBagu(LuteAnimation.lute_hold_right);
                    } else {
                        rootModel.applyStaticBagu(LuteAnimation.lute_hold_left);
                    }
                }
                if (event.getModel() instanceof PlayerModel<?> humanoidModel) {
                    humanoidModel.leftSleeve.copyFrom(humanoidModel.leftArm);
                    humanoidModel.rightSleeve.copyFrom(humanoidModel.rightArm);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.getUseItem().is(ModItems.LUTE.get()) && event.getScrollDeltaY() != 0) {
            onMouseScrolled(event.getScrollDeltaY());

            event.setCanceled(true);
        }
    }

    public static boolean onMouseScrolled(double scrollDelta) {
        ChampaignAttachment attachment = Minecraft.getInstance().player.getData(ModAttachments.CHAMPAIGN);
        attachment.cycle(scrollDelta > 0 ? 1 : -1);
        PacketDistributor.sendToServer(new ChangeMusicSlotPacket(scrollDelta > 0 ? 1 : -1));
        return true;
    }


    @SubscribeEvent
    public static void onPlayerPostTick(PlayerTickEvent.Post event) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.getUseItem().is(ModItems.LUTE.get())) {

            if (ModKeyMappings.KEY_SUMMON.isDown()) {
                if (pressSummonTick <= 0) {
                    pressSummonTick = 20;
                    PacketDistributor.sendToServer(new SummonPacket());
                }
            }
        }

        if (pressSummonTick > 0) {
            --pressSummonTick;
        }
    }


}
