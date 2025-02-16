package baguchi.champaign.client;

import bagu_chan.bagus_lib.animation.BaguAnimationController;
import bagu_chan.bagus_lib.api.client.IRootModel;
import bagu_chan.bagus_lib.client.event.BagusModelEvent;
import bagu_chan.bagus_lib.util.client.AnimationUtil;
import baguchi.champaign.Champaign;
import baguchi.champaign.attachment.ChampaignAttachment;
import baguchi.champaign.client.animation.LuteAnimation;
import baguchi.champaign.packet.CallPacket;
import baguchi.champaign.packet.ChangeMusicSlotPacket;
import baguchi.champaign.packet.SummonAllayPacket;
import baguchi.champaign.packet.SummonPacket;
import baguchi.champaign.registry.ModAnimations;
import baguchi.champaign.registry.ModItems;
import baguchi.champaign.registry.ModKeyMappings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Champaign.MODID, value = Dist.CLIENT)
public class ClientEvents {
    public static int pressSummonTick;

    @SubscribeEvent
    public static void onInitModelEvent(BagusModelEvent.Init event) {
        IRootModel rootModel = event.getRootModel();
        if (event.isSupportedAnimateModel()) {
            rootModel.getBagusRoot().getAllParts().forEach(ModelPart::resetPose);
        }
    }

    @SubscribeEvent
    public static void onAnimateModel(BagusModelEvent.PostAnimate event) {
        Entity entity = event.getEntity();
        IRootModel rootModel = event.getRootModel();
        BaguAnimationController controller = AnimationUtil.getAnimationController(event.getEntity());
        if (controller != null && entity instanceof Player player && event.isSupportedAnimateModel()) {
            boolean flag = player.getMainArm() == HumanoidArm.RIGHT;
            if (event.getModel() instanceof HumanoidModel<?> humanoidModel) {

                if (controller.getAnimationState(ModAnimations.PLAYING_LUTE).isStarted()) {
                    humanoidModel.leftArm.xRot = 0.0F;
                    humanoidModel.rightArm.xRot = 0.0F;
                    humanoidModel.leftArm.yRot = 0.0F;
                    humanoidModel.rightArm.yRot = 0.0F;
                    humanoidModel.leftArm.zRot = 0.0F;
                    humanoidModel.rightArm.zRot = 0.0F;
                    if (flag) {
                        rootModel.animateBagu(controller.getAnimationState(ModAnimations.PLAYING_LUTE), LuteAnimation.lute_playing_right, event.getAgeInTick());
                    } else {
                        rootModel.animateBagu(controller.getAnimationState(ModAnimations.PLAYING_LUTE), LuteAnimation.lute_playing_left, event.getAgeInTick());
                    }
                }
                if (event.getModel() instanceof PlayerModel<?> playerModel) {
                    playerModel.leftPants.copyFrom(playerModel.leftLeg);
                    playerModel.rightPants.copyFrom(playerModel.rightLeg);
                    playerModel.leftSleeve.copyFrom(playerModel.leftArm);
                    playerModel.rightSleeve.copyFrom(playerModel.rightArm);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.getUseItem().is(ModItems.LUTE.get()) && event.getMouseY() != 0) {
            onMouseScrolled(event.getMouseY());

            event.setCanceled(true);
        }
    }

    public static boolean onMouseScrolled(double scrollDelta) {
        ChampaignAttachment attachment = Minecraft.getInstance().player.getCapability(Champaign.CHAMPAIGN_CAPABILITY).orElse(new ChampaignAttachment());
        attachment.cycle(scrollDelta > 0 ? 1 : -1);
        Champaign.CHANNEL.send(PacketDistributor.SERVER.noArg(), new ChangeMusicSlotPacket(scrollDelta > 0 ? 1 : -1));
        return true;
    }


    @SubscribeEvent
    public static void onPlayerPostTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.getUseItem().is(ModItems.LUTE.get())) {

                if (ModKeyMappings.KEY_SUMMON.isDown()) {
                    if (pressSummonTick <= 0) {
                        pressSummonTick = 20;
                        Champaign.CHANNEL.send(PacketDistributor.SERVER.noArg(), new SummonPacket());
                    }
                }
            }

            if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isHolding(ModItems.LUTE.get())) {

                if (ModKeyMappings.KEY_SUMMON_ALLAY.isDown()) {
                    if (pressSummonTick <= 0) {
                        pressSummonTick = 20;
                        Champaign.CHANNEL.send(PacketDistributor.SERVER.noArg(), new SummonAllayPacket());
                    }
                }

                if (ModKeyMappings.KEY_CALL_ALLAY.isDown()) {
                    if (pressSummonTick <= 0) {
                        pressSummonTick = 20;
                        Champaign.CHANNEL.send(PacketDistributor.SERVER.noArg(), new CallPacket());
                    }
                }
            }

            if (pressSummonTick > 0) {
                --pressSummonTick;
            }
        }
    }


}
