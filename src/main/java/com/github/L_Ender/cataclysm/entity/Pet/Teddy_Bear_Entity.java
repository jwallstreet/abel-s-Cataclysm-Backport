package com.github.L_Ender.cataclysm.entity.Pet;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.entity.Pet.AI.InternalPetStateGoal;
import com.github.L_Ender.cataclysm.entity.Pet.AI.TameableAIFollowOwner;
import com.github.L_Ender.cataclysm.init.ModItems;
import com.github.L_Ender.cataclysm.inventory.TeddyBearMenu;
import com.github.L_Ender.cataclysm.message.MessageTeddyInventory;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.network.PacketDistributor;

public class Teddy_Bear_Entity extends InternalAnimationPet implements Bucketable, ContainerListener, HasCustomInventoryScreen {
    
    private static final EntityDataAccessor<Boolean> IS_AWAKEN = SynchedEntityData.defineId(Teddy_Bear_Entity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Teddy_Bear_Entity.class, EntityDataSerializers.BOOLEAN);
    
    public SimpleContainer inventory;
    
    // Animation states
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState sleepAnimationState = new AnimationState();
    public AnimationState chestopenAnimationState = new AnimationState();
    public AnimationState chestloopAnimationState = new AnimationState();
    public AnimationState chestcloseAnimationState = new AnimationState();

    public Teddy_Bear_Entity(EntityType<? extends Teddy_Bear_Entity> type, Level world) {
        super(type, world);
        this.createInventory();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(6, new TameableAIFollowOwner(this, 1.3D, 6.0F, 2.0F, true));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0D, 60));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        
        // Sleep state goal when not awakened
        this.goalSelector.addGoal(1, new InternalPetStateGoal(this, 1, 1, 0, 0, 0) {
            @Override
            public boolean canUse() {
                return super.canUse() && !Teddy_Bear_Entity.this.getIsAwaken();
            }
        });
        
        // Chest close state transition
        this.goalSelector.addGoal(1, new InternalPetStateGoal(this, 5, 5, 0, 10, 0) {
            @Override
            public boolean canUse() {
                return super.canUse() && Teddy_Bear_Entity.this.getAttackState() == 5;
            }
        });
    }

    protected int getInventorySize() {
        return 9;
    }

    protected void createInventory() {
        SimpleContainer simplecontainer = this.inventory;
        this.inventory = new SimpleContainer(this.getInventorySize());
        if (simplecontainer != null) {
            simplecontainer.removeListener(this);
            int i = Math.min(simplecontainer.getContainerSize(), this.inventory.getContainerSize());

            for(int j = 0; j < i; ++j) {
                ItemStack itemstack = simplecontainer.getItem(j);
                if (!itemstack.isEmpty()) {
                    this.inventory.setItem(j, itemstack.copy());
                }
            }
        }

        this.inventory.addListener(this);
        this.itemHandler = net.minecraftforge.common.util.LazyOptional.of(() -> new net.minecraftforge.items.wrapper.InvWrapper(this.inventory));
    }

    @Override
    public void openCustomInventoryScreen(Player playerEntity) {
        if(playerEntity instanceof ServerPlayer serverplayer) {
            if (isAlive()) {
                if (serverplayer.containerMenu != serverplayer.inventoryMenu) {
                    serverplayer.closeContainer();
                }

                this.setAttackState(3);
                serverplayer.nextContainerCounter();
                Cataclysm.NETWORK_WRAPPER.send(PacketDistributor.PLAYER.with(() -> serverplayer), new MessageTeddyInventory(serverplayer.containerCounter, this.inventory.getContainerSize(), this.getId()));
                serverplayer.containerMenu = new TeddyBearMenu(serverplayer.containerCounter, serverplayer.getInventory(), this.inventory, this);
                serverplayer.initMenu(serverplayer.containerMenu);
                MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(serverplayer, serverplayer.containerMenu));
            }
        }
    }

    public void containerChanged(Container p_30548_) {
        // Container change handling
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_AWAKEN, false);
        this.entityData.define(FROM_BUCKET, false);
    }

    public void setIsAwaken(boolean isAwaken) {
        this.entityData.set(IS_AWAKEN, isAwaken);
        if (!isAwaken) {
            this.setAttackState(1); // Set to sleep state
        }
    }

    public boolean getIsAwaken() {
        return this.entityData.get(IS_AWAKEN);
    }

    public AnimationState getAnimationState(String input) {
        if (input == "idle") {
            return this.idleAnimationState;
        } else if (input == "sleep") {
            return this.sleepAnimationState;
        } else if (input == "chest_open") {
            return this.chestopenAnimationState;
        } else if (input == "chest_loop") {
            return this.chestloopAnimationState;
        } else if (input == "chest_close") {
            return this.chestcloseAnimationState;
        } else {
            return new AnimationState();
        }
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor) {
        if (ATTACK_STATE.equals(dataAccessor)) {
            if (this.level.isClientSide)
                switch (this.getAttackState()) {
                    case 0 -> this.stopAllAnimationStates();
                    case 1 -> {
                        this.stopAllAnimationStates();
                        this.sleepAnimationState.startIfStopped(this.tickCount);
                    }
                    case 3 -> {
                        this.stopAllAnimationStates();
                        this.chestopenAnimationState.startIfStopped(this.tickCount);
                    }
                    case 4 -> {
                        this.stopAllAnimationStates();
                        this.chestloopAnimationState.startIfStopped(this.tickCount);
                    }
                    case 5 -> {
                        this.stopAllAnimationStates();
                        this.chestcloseAnimationState.startIfStopped(this.tickCount);
                    }
                }
        }
        super.onSyncedDataUpdated(dataAccessor);
    }

    public void stopAllAnimationStates() {
        this.sleepAnimationState.stop();
        this.chestopenAnimationState.stop();
        this.chestloopAnimationState.stop();
        this.chestcloseAnimationState.stop();
    }

    public void aiStep() {
        super.aiStep();
        
        if (this.level.isClientSide()) {
            this.animateWhen(this.idleAnimationState, this.getAttackState() == 0, this.tickCount);
        }
        
        // Handle chest open -> chest loop transition
        if(this.getAttackState() == 3){
            if(this.attackTicks >= 9){
                this.setAttackState(4);
            }
        }
        
        // Handle chest close -> idle transition
        if(this.getAttackState() == 5){
            if(this.attackTicks >= 10){
                this.setAttackState(0);
            }
        }
    }
    
    public void animateWhen(AnimationState state, boolean condition, int tickCount) {
        if (condition) {
            state.startIfStopped(tickCount);
        } else {
            state.stop();
        }
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("is_Awaken", this.getIsAwaken());
        compound.putBoolean("FromBucket", this.fromBucket());
        ListTag listtag = new ListTag();

        for(int i = 0; i < this.inventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty()) {
                CompoundTag compoundtag = new CompoundTag();
                compoundtag.putByte("Slot", (byte)i);
                itemstack.save(compoundtag);
                listtag.add(compoundtag);
            }
        }

        compound.put("Items", listtag);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setIsAwaken(compound.getBoolean("is_Awaken"));
        this.setFromBucket(compound.getBoolean("FromBucket"));
        this.createInventory();
        ListTag listtag = compound.getList("Items", 10);

        for(int i = 0; i < listtag.size(); ++i) {
            CompoundTag compoundtag = listtag.getCompound(i);
            int j = compoundtag.getByte("Slot") & 255;
            if (j < this.inventory.getContainerSize()) {
                this.inventory.setItem(j, ItemStack.of(compoundtag));
            }
        }
    }

    protected void dropEquipment() {
        super.dropEquipment();
        if (inventory != null) {
            for (int i = 0; i < inventory.getContainerSize(); ++i) {
                ItemStack itemstack = inventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    this.spawnAtLocation(itemstack, 0.0F);
                }
            }
        }
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        boolean owner = this.isTame() && isOwnedBy(player);
        InteractionResult type = super.mobInteract(player, hand);

        // Handle bucket pickup for owner
        if (owner) {
            Optional<InteractionResult> result = bucketMobPickup(player, hand, this);
            if (result.isPresent()) {
                return result.get();
            } else {
                if (!player.isShiftKeyDown()) {
                    this.openCustomInventoryScreen(player);
                    this.setCommand(2);
                    this.setOrderedToSit(true);
                    return InteractionResult.sidedSuccess(this.level.isClientSide);
                }
            }
        }

        // Taming logic with HONEYCOMB
        if (!isTame() && stack.is(Items.HONEYCOMB)) {
            this.usePlayerItem(player, hand, stack);
            this.gameEvent(GameEvent.EAT);
            if (!net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
                this.tame(player);
                this.setIsAwaken(true); // Awaken the teddy bear when tamed
                this.level.broadcastEntityEvent(this, (byte) 7);
            } else {
                this.level.broadcastEntityEvent(this, (byte) 6);
            }
            return InteractionResult.SUCCESS;
        }

        // Command cycling for tamed entity
        if (isTame() && isOwnedBy(player)) {
            if (player.isShiftKeyDown()) {
                this.setCommand(this.getCommand() + 1);
                if (this.getCommand() == 3) {
                    this.setCommand(0);
                }
                player.displayClientMessage(Component.translatable("entity.cataclysm.all.command_" + this.getCommand(), this.getName()), true);
                boolean sit = this.getCommand() == 2;
                if (sit) {
                    this.setOrderedToSit(true);
                    return InteractionResult.SUCCESS;
                } else {
                    this.setOrderedToSit(false);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        
        return type;
    }

    private static <T extends LivingEntity & Bucketable> Optional<InteractionResult> bucketMobPickup(Player player, InteractionHand hand, T entity) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.getItem() == Items.BUCKET && entity.isAlive()) {
            entity.playSound(entity.getPickupSound(), 1.0F, 1.0F);
            ItemStack itemstack1 = entity.getBucketItemStack();
            entity.saveToBucketTag(itemstack1);
            ItemStack itemstack2 = ItemUtils.createFilledResult(itemstack, player, itemstack1, false);
            player.setItemInHand(hand, itemstack2);
            Level level = entity.level;
            if (!level.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, itemstack1);
            }

            entity.discard();
            return Optional.of(InteractionResult.sidedSuccess(level.isClientSide));
        } else {
            return Optional.empty();
        }
    }

    public static AttributeSupplier.Builder teddy_bear_attributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35F);
    }

    // Bucketable interface methods
    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public void saveToBucketTag(@Nonnull ItemStack bucket) {
        CompoundTag tag = new CompoundTag();
        CompoundTag compound = bucket.getOrCreateTag();
        this.addAdditionalSaveData(tag);
        Bucketable.saveDefaultDataToBucketTag(this, bucket);
        compound.put("TeddyBearData", tag);
    }

    @Override
    public void loadFromBucketTag(CompoundTag compound) {
        Bucketable.loadDefaultDataFromBucketTag(this, compound);
        if (compound.contains("TeddyBearData")) {
            this.readAdditionalSaveData(compound.getCompound("TeddyBearData"));
        }
    }

    @Override
    @Nonnull
    public ItemStack getBucketItemStack() {
        ItemStack stack = new ItemStack(ModItems.TEDDY_BEAR_BUCKET.get());
        return stack;
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_LAVA; // Temporary sound, will be replaced in Phase 8
    }

    private net.minecraftforge.common.util.LazyOptional<?> itemHandler = null;

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.core.Direction facing) {
        if (this.isAlive() && capability == net.minecraftforge.common.capabilities.ForgeCapabilities.ITEM_HANDLER && itemHandler != null)
            return itemHandler.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        if (itemHandler != null) {
            net.minecraftforge.common.util.LazyOptional<?> oldHandler = itemHandler;
            itemHandler = null;
            oldHandler.invalidate();
        }
    }

    public boolean hasInventoryChanged(Container p_149512_) {
        return this.inventory != p_149512_;
    }

}