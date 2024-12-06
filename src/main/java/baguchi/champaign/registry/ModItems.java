package baguchi.champaign.registry;

import baguchi.champaign.Champaign;
import baguchi.champaign.item.LuteItem;
import baguchi.champaign.item.MusicPatternItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Champaign.MODID);

    public static final DeferredItem<Item> MUSIC_PATTERN = ITEMS.registerItem("music_pattern", (properties) -> new MusicPatternItem(properties));

    public static final DeferredItem<Item> LUTE = ITEMS.registerItem("lute", (properties) -> new LuteItem((properties.stacksTo(1))));

}
