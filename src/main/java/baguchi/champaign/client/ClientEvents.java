package baguchi.champaign.client;

import baguchi.bagus_lib.animation.BaguAnimationController;
import baguchi.bagus_lib.client.event.BagusModelEvent;
import baguchi.champaign.Champaign;
import baguchi.champaign.attachment.ChampaignAttachment;
import baguchi.champaign.client.animation.LuteAnimation;
import baguchi.champaign.packet.ChangeMusicSlotPacket;
import baguchi.champaign.packet.SummonPacket;
import baguchi.champaign.registry.ModAnimations;
import baguchi.champaign.registry.ModAttachments;
import baguchi.champaign.registry.ModItems;
import baguchi.champaign.registry.ModKeyMappings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.HumanoidArm;
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
    public static void onAnimateModel(BagusModelEvent.PostAnimate event) {
        LivingEntityRenderState entity = event.getEntityRenderState();
        BaguAnimationController controller = event.getBaguAnimationController();
        if (controller != null && entity instanceof HumanoidRenderState humanoidRenderState) {
            boolean flag = humanoidRenderState.mainArm == HumanoidArm.RIGHT;
            if (event.getModel() instanceof HumanoidModel<?> humanoidModel) {

                if (controller.getAnimationState(ModAnimations.PLAYING_LUTE).isStarted()) {
                    humanoidModel.leftArm.xRot = 0.0F;
                    humanoidModel.rightArm.xRot = 0.0F;
                    humanoidModel.leftArm.yRot = 0.0F;
                    humanoidModel.rightArm.yRot = 0.0F;
                    humanoidModel.leftArm.zRot = 0.0F;
                    humanoidModel.rightArm.zRot = 0.0F;
                    if (flag) {
                        event.animate(controller.getAnimationState(ModAnimations.PLAYING_LUTE), LuteAnimation.lute_playing_right, entity.ageInTicks);
                    } else {
                        event.animate(controller.getAnimationState(ModAnimations.PLAYING_LUTE), LuteAnimation.lute_playing_left, entity.ageInTicks);
                    }
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
