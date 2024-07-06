package baguchan.champaign.client;

import baguchan.champaign.Champaign;
import baguchan.champaign.ChampaignConfig;
import baguchan.champaign.packet.CallPacket;
import baguchan.champaign.packet.SummonPacket;
import baguchan.champaign.registry.ModKeyMappings;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = Champaign.MODID, value = Dist.CLIENT)
public class ClientEvents {
    public static int pressCallTick;
    public static int pressSummonTick;

    @SubscribeEvent
    public static void onPlayerPostTick(PlayerTickEvent.Post event) {

        if (ChampaignConfig.COMMON.enableCampaign.get()) {
            if (ModKeyMappings.KEY_CALL.isDown() && !Minecraft.getInstance().player.isShiftKeyDown()) {
                if (pressCallTick <= 0) {
                    pressCallTick = 20;
                    PacketDistributor.sendToServer(new CallPacket());
                }
            }

            if (ModKeyMappings.KEY_SUMMON.isDown() && !Minecraft.getInstance().player.isShiftKeyDown()) {
                if (pressSummonTick <= 0) {
                    pressSummonTick = 20;
                    PacketDistributor.sendToServer(new SummonPacket());
                }
            }
            if (pressCallTick > 0) {
                --pressCallTick;
            }

            if (pressSummonTick > 0) {
                --pressSummonTick;
            }
        }
    }

    @SubscribeEvent
    public static void registerEntityRenders(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            LocalPlayer player = Minecraft.getInstance().player;
            Camera camera = Minecraft.getInstance().getEntityRenderDispatcher().camera;
            /*if (player != null && camera != null && ModKeyMappings.KEY_RANGE.isDown()) {
                HitResult hitResult = player.pick(20.0D, 0.0F, false);

                Vec3 pos = hitResult.getLocation();
                if (hitResult.getType() != HitResult.Type.MISS) {


                    VertexConsumer vertexconsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());
                    LevelRenderer.renderLineBox(event.getPoseStack(), vertexconsumer, (double) pos.x - GatherBlocks.RANGE - camera.getPosition().x, (double) pos.y - GatherBlocks.RANGE - camera.getPosition().y, (double) pos.z - GatherBlocks.RANGE - camera.getPosition().z, (double) (pos.x + GatherBlocks.RANGE) - camera.getPosition().x, (double) (pos.y + GatherBlocks.RANGE) - camera.getPosition().y, (double) (pos.z + GatherBlocks.RANGE) - camera.getPosition().z, 0.3F, 0.3F, 1.0F, 0.3F);


                }
            }*/
        }
    }

}
