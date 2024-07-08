package baguchan.champaign.registry;

import baguchan.champaign.Champaign;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Champaign.MODID);

    public static final Supplier<Item> MUSIC_PATTERN = ITEMS.register("music_pattern", () -> new Item((new Item.Properties())));

}
