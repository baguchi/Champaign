package baguchi.champaign.registry;


import bagu_chan.bagus_lib.event.RegisterBagusAnimationEvents;
import baguchi.champaign.Champaign;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Champaign.MODID)
public class ModAnimations {

    public static final ResourceLocation PLAYING_LUTE = new ResourceLocation(Champaign.MODID, "playing_lute");

    @SubscribeEvent
    public static void onInitModel(RegisterBagusAnimationEvents event) {
        event.addAnimationState(PLAYING_LUTE);
    }
}

