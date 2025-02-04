package baguchi.champaign.entity;

import baguchi.champaign.registry.ModMemorys;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractWorkerAllay extends PathfinderMob implements InventoryCarrier {
    private final SimpleContainer inventory = new SimpleContainer(5);
    private static final Vec3i ITEM_PICKUP_REACH = new Vec3i(1, 1, 1);

    public AbstractWorkerAllay(EntityType<? extends AbstractWorkerAllay> p_218310_, Level p_218311_) {
        super(p_218310_, p_218311_);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setCanPickUpLoot(this.canPickUpLoot());
    }

    @Override
    protected InteractionResult mobInteract(Player p_218361_, InteractionHand p_218362_) {
        this.returnToPlayer();
        return InteractionResult.SUCCESS;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.isAlive() && this.tickCount % 10 == 0) {
            this.heal(1.0F);
        }
    }

    @Override
    public void travel(Vec3 p_218382_) {
        if (this.isControlledByLocalInstance()) {
            if (this.isInWater()) {
                this.moveRelative(0.02F, p_218382_);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale((double) 0.8F));
            } else if (this.isInLava()) {
                this.moveRelative(0.02F, p_218382_);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
            } else {
                this.moveRelative(this.getSpeed(), p_218382_);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale((double) 0.91F));
            }
        }

        this.calculateEntityAnimation(false);
    }

    @Override
    protected PathNavigation createNavigation(Level p_218342_) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, p_218342_);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        return flyingpathnavigation;
    }

    @Override
    protected void playStepSound(BlockPos p_218364_, BlockState p_218365_) {
    }

    @Override
    protected void checkFallDamage(double p_218316_, boolean p_218317_, BlockState p_218318_, BlockPos p_218319_) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.hasItemInSlot(EquipmentSlot.MAINHAND) ? SoundEvents.ALLAY_AMBIENT_WITH_ITEM : SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_218369_) {
        return SoundEvents.ALLAY_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ALLAY_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public SimpleContainer getInventory() {
        return this.inventory;
    }


    @Override
    protected void pickUpItem(ServerLevel p_376246_, ItemEntity p_218359_) {
        InventoryCarrier.pickUpItem(p_376246_, this, this, p_218359_);
    }

    @Override
    public boolean canPickUpLoot() {
        return !this.isOnPickupCooldown();
    }

    @Override
    public boolean wantsToPickUp(ServerLevel p_376111_, ItemStack p_218387_) {
        return this.inventory.canAddItem(p_218387_);
    }

    @Override
    protected boolean considersEntityAsAlly(Entity p_388703_) {
        return this.isLikedPlayer(p_388703_) || super.considersEntityAsAlly(p_388703_);
    }


    private boolean isLikedPlayer(@Nullable Entity p_386522_) {
        if (!(p_386522_ instanceof Player player)) {
            return false;
        } else {
            Optional<UUID> optional = this.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
            return optional.isPresent() && player.getUUID().equals(optional.get());
        }
    }


    private boolean isOnPickupCooldown() {
        return this.getBrain().checkMemory(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryStatus.VALUE_PRESENT);
    }

    @Override
    protected void dropEquipment(ServerLevel p_376761_) {
        super.dropEquipment(p_376761_);
        this.inventory.removeAllItems().forEach(p_375836_ -> this.spawnAtLocation(p_376761_, p_375836_));
        ItemStack itemstack = this.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!itemstack.isEmpty() && !EnchantmentHelper.has(itemstack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)) {
            this.spawnAtLocation(p_376761_, itemstack);
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
    }


    @Override
    public boolean hurtServer(ServerLevel p_376761_, DamageSource p_218339_, float p_218340_) {
        Entity $$3 = p_218339_.getEntity();
        if ($$3 instanceof Player player) {
            Optional<UUID> optional = this.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
            if (optional.isPresent() && player.getUUID().equals(optional.get())) {
                this.returnToPlayer();
                return false;
            }
        }

        return super.hurtServer(p_376761_, p_218339_, p_218340_);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_218367_) {
        super.addAdditionalSaveData(p_218367_);
        this.writeInventoryToTag(p_218367_, this.registryAccess());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_218350_) {
        super.readAdditionalSaveData(p_218350_);
        this.readInventoryFromTag(p_218350_, this.registryAccess());
    }

    public void giveResource() {
        if (!this.level().isClientSide()) {
            if (!this.inventory.isEmpty()) {
                if (this.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER).isPresent()) {
                    Player player = this.level().getPlayerByUUID(this.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER).get());
                    for (int j = 0; j < this.inventory.getContainerSize(); ++j) {
                        ItemStack itemstack = this.inventory.getItem(j);
                        if (!itemstack.isEmpty()) {
                            if (!player.getInventory().add(itemstack)) {
                                player.drop(itemstack, false);
                            }

                        }
                    }
                    player.take(this, 1);
                }
            }
        }
    }

    public boolean isReturnOwner(Player player) {
        return this.isAlive() && this.getBrain().hasMemoryValue(MemoryModuleType.LIKED_PLAYER) && this.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER).get() == player.getUUID();
    }

    public void returnToPlayer() {
        if (!this.level().isClientSide()) {
            this.getBrain().eraseMemory(ModMemorys.WORK_POS.get());
        }
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0D, (double) this.getEyeHeight() * 0.6D, (double) this.getBbWidth() * 0.1D);
    }

    @Override
    protected Vec3i getPickupReach() {
        return ITEM_PICKUP_REACH;
    }

    @Override
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }

    @Override
    public boolean isFlapping() {
        return !this.onGround();
    }
}
