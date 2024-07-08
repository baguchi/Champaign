package baguchan.champaign.registry;

import baguchan.champaign.Champaign;
import baguchan.champaign.block.MusicTableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, Champaign.MODID);

    public static final Supplier<Block> MUSIC_TABLE = register("music_table", () -> new MusicTableBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_PINK)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F).sound(SoundType.CHERRY_WOOD)));

    private static boolean always(BlockState p_50775_, BlockGetter p_50776_, BlockPos p_50777_) {
        return true;
    }

    private static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {
        return false;
    }

    private static <T extends Block> Supplier<T> baseRegister(String name, Supplier<? extends T> block, Function<Supplier<T>, Supplier<? extends Item>> item) {
        Supplier<T> register = BLOCKS.register(name, block);
        Supplier<? extends Item> itemSupplier = item.apply(register);
        ModItems.ITEMS.register(name, itemSupplier);
        return register;
    }

    private static <T extends Block> Supplier<T> noItemRegister(String name, Supplier<? extends T> block) {
        Supplier<T> register = BLOCKS.register(name, block);
        return register;
    }

    private static <B extends Block> Supplier<B> register(String name, Supplier<? extends Block> block) {
        return (Supplier<B>) baseRegister(name, block, (object) -> ModBlocks.registerBlockItem(object));
    }

    private static <T extends Block> Supplier<BlockItem> registerBlockItem(final Supplier<T> block) {
        return () -> {
            return new BlockItem(Objects.requireNonNull(block.get()), new Item.Properties());
        };
    }
}
