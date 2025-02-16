package baguchi.champaign.registry;

import baguchi.champaign.Champaign;
import baguchi.champaign.item.LuteItem;
import baguchi.champaign.item.MusicPatternItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Champaign.MODID);

    public static final RegistryObject<Item> MUSIC_PATTERN = ITEMS.register("music_pattern", () -> new MusicPatternItem(new Item.Properties()));

    public static final RegistryObject<Item> LUTE = ITEMS.register("lute", () -> new LuteItem((new Item.Properties().stacksTo(1))));

}
