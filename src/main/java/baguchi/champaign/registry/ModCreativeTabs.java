package baguchi.champaign.registry;

import baguchi.champaign.Champaign;
import baguchi.champaign.music.MusicSummon;
import baguchi.champaign.util.ChampaignUtil;
import com.google.common.collect.Lists;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Champaign.MODID)
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
            ChampaignUtil.makeMusic(stack, Champaign.registryAccess().registryOrThrow(MusicSummon.REGISTRY_KEY).wrapAsHolder(musicSummon));
            items.add(stack);
        }
        return items;
    }
}