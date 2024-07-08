package baguchan.champaign;


import baguchan.champaign.music.MusicSummon;
import baguchan.champaign.packet.AddMusicPacket;
import baguchan.champaign.packet.CallPacket;
import baguchan.champaign.packet.ChangeMusicSlotPacket;
import baguchan.champaign.packet.SummonPacket;
import baguchan.champaign.registry.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Locale;

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

        NeoForge.EVENT_BUS.register(this);
        ModMusicSummons.MUSIC_SUMMON.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITIES_REGISTRY.register(modEventBus);
        ModMemorys.MEMORY_REGISTRY.register(modEventBus);
        ModDataComponents.DATA_COMPONENT_TYPES.register(modEventBus);
        ModAttachments.ATTACHMENT_TYPES.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::commonDataSetup);
        modEventBus.addListener(this::setupPackets);
        modContainer.registerConfig(ModConfig.Type.COMMON, ChampaignConfig.COMMON_SPEC);

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    public static RegistryAccess registryAccess() {
        if (EffectiveSide.get().isServer()) {
            return ServerLifecycleHooks.getCurrentServer().registryAccess();
        }
        return Minecraft.getInstance().getConnection().registryAccess();
    }

    private void commonDataSetup(final DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(MusicSummon.REGISTRY_KEY, MusicSummon.CODEC, MusicSummon.CODEC);
    }

    public void setupPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MODID).versioned("1.0.0").optional();
        registrar.playBidirectional(CallPacket.TYPE, CallPacket.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
        registrar.playBidirectional(SummonPacket.TYPE, SummonPacket.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
        registrar.playBidirectional(ChangeMusicSlotPacket.TYPE, ChangeMusicSlotPacket.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
        registrar.playBidirectional(AddMusicPacket.TYPE, AddMusicPacket.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    public static ResourceLocation prefix(String name) {
        return ResourceLocation.fromNamespaceAndPath(Champaign.MODID, name.toLowerCase(Locale.ROOT));
    }
}
