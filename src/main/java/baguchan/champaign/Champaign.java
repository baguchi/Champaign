package baguchan.champaign;


import baguchan.champaign.packet.CallPacket;
import baguchan.champaign.packet.SummonPacket;
import baguchan.champaign.packet.SyncAllayPacket;
import baguchan.champaign.registry.ModAttachments;
import baguchan.champaign.registry.ModEntities;
import baguchan.champaign.registry.ModMemorys;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Champaign.MODID)
public class Champaign
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "champaign";
    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Champaign(IEventBus modEventBus, ModContainer modContainer)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::setupPackets);
        NeoForge.EVENT_BUS.register(this);

        ModEntities.ENTITIES_REGISTRY.register(modEventBus);
        ModMemorys.MEMORY_REGISTRY.register(modEventBus);
        ModAttachments.ATTACHMENT_TYPES.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, ChampaignConfig.COMMON_SPEC);

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    public void setupPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MODID).versioned("1.0.0").optional();
        registrar.playBidirectional(CallPacket.TYPE, CallPacket.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
        registrar.playBidirectional(SummonPacket.TYPE, SummonPacket.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
        registrar.playBidirectional(SyncAllayPacket.TYPE, SyncAllayPacket.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }
}
