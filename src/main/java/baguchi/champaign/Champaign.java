package baguchi.champaign;


import baguchi.champaign.attachment.ChampaignAttachment;
import baguchi.champaign.attachment.OwnerAttachment;
import baguchi.champaign.music.MusicSummon;
import baguchi.champaign.packet.*;
import baguchi.champaign.registry.ModEntities;
import baguchi.champaign.registry.ModItems;
import baguchi.champaign.registry.ModMemorys;
import baguchi.champaign.registry.ModMusicSummons;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Locale;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Champaign.MODID)
public class Champaign
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "champaign";
    public static final String NETWORK_PROTOCOL = "2";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MODID, "net"))
            .networkProtocolVersion(() -> NETWORK_PROTOCOL)
            .clientAcceptedVersions(NETWORK_PROTOCOL::equals)
            .serverAcceptedVersions(NETWORK_PROTOCOL::equals)
            .simpleChannel();

    public static final Capability<ChampaignAttachment> CHAMPAIGN_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static final Capability<OwnerAttachment> OWNER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Champaign()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);
        ModMusicSummons.MUSIC_SUMMON.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITIES_REGISTRY.register(modEventBus);
        ModMemorys.MEMORY_REGISTRY.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::commonDataSetup);
        this.setupMessages();
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

    private void setupMessages() {
        CHANNEL.messageBuilder(AddMusicPacket.class, 0)
                .encoder(AddMusicPacket::serialize).decoder(AddMusicPacket::deserialize)
                .consumerMainThread(AddMusicPacket::handle)
                .add();
        CHANNEL.messageBuilder(CallPacket.class, 1)
                .encoder(CallPacket::serialize).decoder(CallPacket::deserialize)
                .consumerMainThread(CallPacket::handle)
                .add();
        CHANNEL.messageBuilder(ChangeMusicSlotPacket.class, 2)
                .encoder(ChangeMusicSlotPacket::serialize).decoder(ChangeMusicSlotPacket::deserialize)
                .consumerMainThread(ChangeMusicSlotPacket::handle)
                .add();
        CHANNEL.messageBuilder(SummonAllayPacket.class, 3)
                .encoder(SummonAllayPacket::serialize).decoder(SummonAllayPacket::deserialize)
                .consumerMainThread(SummonAllayPacket::handle)
                .add();
        CHANNEL.messageBuilder(SummonPacket.class, 4)
                .encoder(SummonPacket::serialize).decoder(SummonPacket::deserialize)
                .consumerMainThread(SummonPacket::handle)
                .add();
        CHANNEL.messageBuilder(SyncAllayPacket.class, 5)
                .encoder(SyncAllayPacket::serialize).decoder(SyncAllayPacket::deserialize)
                .consumerMainThread(SyncAllayPacket::handle)
                .add();
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(Champaign.MODID, name.toLowerCase(Locale.ROOT));
    }
}
