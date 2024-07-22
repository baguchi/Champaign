package baguchan.champaign.registry;

import bagu_chan.bagus_lib.event.RegisterBagusAnimationEvents;
import baguchan.champaign.Champaign;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = Champaign.MODID)
public class ModAnimations {

    public static final ResourceLocation PLAYING_LUTE = ResourceLocation.fromNamespaceAndPath(Champaign.MODID, "playing_lute");

    @SubscribeEvent
    public static void onInitModel(RegisterBagusAnimationEvents event) {
        event.addAnimationState(PLAYING_LUTE);
    }
}
