package baguchi.champaign.item;

import baguchi.champaign.registry.ModKeyMappings;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LuteItem extends Item {
    public LuteItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.BOW;
    }


    @Override
    public int getUseDuration(ItemStack p_41454_) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        /*if (!level.isClientSide()) {
            AnimationUtil.sendAnimation(player, ModAnimations.PLAYING_LUTE);
        }*/
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        super.onStopUsing(stack, entity, count);
        /*if (!entity.level().isClientSide()) {
            AnimationUtil.sendStopAnimation(entity, ModAnimations.PLAYING_LUTE);
        }*/
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_41422_, List<Component> tooltip, TooltipFlag p_41424_) {
        super.appendHoverText(stack, p_41422_, tooltip, p_41424_);
        Minecraft instance = Minecraft.getInstance();
        //操作キーを取得
        Component useKey = instance.options.keyUse.getTranslatedKeyMessage();
        Component summonKey = ModKeyMappings.KEY_SUMMON.getTranslatedKeyMessage();
        tooltip.add(Component.literal("[").append(useKey).append(" + ").append(summonKey).append("] :").withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(Component.translatable("item.champaign.lute.tooltip.description1").withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(Component.literal("[").append(useKey).append("] :").withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(Component.translatable("item.champaign.lute.tooltip.description2").withStyle(ChatFormatting.DARK_AQUA));

        Component summonAllayKey = ModKeyMappings.KEY_SUMMON_ALLAY.getTranslatedKeyMessage();
        Component callAllayKey = ModKeyMappings.KEY_CALL_ALLAY.getTranslatedKeyMessage();
        tooltip.add(Component.literal("[").append(summonAllayKey).append("] :").withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(Component.translatable("item.champaign.lute.tooltip.description3").withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(Component.literal("[").append(callAllayKey).append("] :").withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(Component.translatable("item.champaign.lute.tooltip.description4").withStyle(ChatFormatting.DARK_AQUA));

    }
}
