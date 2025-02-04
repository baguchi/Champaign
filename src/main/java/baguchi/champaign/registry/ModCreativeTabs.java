package baguchi.champaign.registry;

import baguchi.champaign.Champaign;
import baguchi.champaign.music.MusicSummon;
import com.google.common.collect.Lists;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import java.util.List;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Champaign.MODID)
public class ModCreativeTabs {
    @SubscribeEvent
    public static void registerCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.acceptAll(generateMusic());
            event.accept(ModItems.LUTE.get());
        }
    }

    public static List<ItemStack> generateMusic() {
        List<ItemStack> items = Lists.newArrayList();
        for (MusicSummon musicSummon : Champaign.registryAccess().registryOrThrow(MusicSummon.REGISTRY_KEY)) {
            ItemStack stack = new ItemStack(ModItems.MUSIC_PATTERN.get());
            stack.set(ModDataComponents.MUSIC_TYPE, Champaign.registryAccess().registryOrThrow(MusicSummon.REGISTRY_KEY).wrapAsHolder(musicSummon));
            items.add(stack);
        }
        return items;
    }
}