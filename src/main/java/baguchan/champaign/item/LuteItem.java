package baguchan.champaign.item;

import baguchan.champaign.registry.ModKeyMappings;
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

public class LuteItem extends Item {
    public LuteItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CUSTOM;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity p_346168_) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, java.util.List<net.minecraft.network.chat.Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltip, tooltipFlag);
        Minecraft instance = Minecraft.getInstance();
        //操作キーを取得
        Component useKey = instance.options.keyUse.getTranslatedKeyMessage();
        Component summonKey = ModKeyMappings.KEY_SUMMON.getTranslatedKeyMessage();
        tooltip.add(Component.literal("[").append(useKey).append(" + ").append(summonKey).append("] :").withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(Component.translatable("item.champaign.lute.tooltip.description1").withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(Component.literal("[").append(useKey).append("] :").withStyle(ChatFormatting.DARK_AQUA));
        tooltip.add(Component.translatable("item.champaign.lute.tooltip.description2").withStyle(ChatFormatting.DARK_AQUA));
    }
}
