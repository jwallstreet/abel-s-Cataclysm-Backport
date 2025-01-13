package com.github.L_Ender.cataclysm.entity.Pet;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.config.CMConfig;
import com.github.L_Ender.cataclysm.entity.Pet.AI.InternalPetStateGoal;
import com.github.L_Ender.cataclysm.entity.Pet.AI.TameableAIFollowOwner;
import com.github.L_Ender.cataclysm.entity.etc.SmartBodyHelper2;
import com.github.L_Ender.cataclysm.init.ModItems;
import com.github.L_Ender.cataclysm.init.ModSounds;
import com.github.L_Ender.cataclysm.inventory.MinistrostiyMenu;
import com.github.L_Ender.cataclysm.message.MessageMiniinventory;

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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.network.PacketDistributor;

public class Netherite_Ministrosity_Entity extends InternalAnimationPet implements Bucketable,ContainerListener, HasCustomInventoryScreen {
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Netherite_Ministrosity_Entity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_AWAKEN = SynchedEntityData.defineId(Netherite_Ministrosity_Entity.class, EntityDataSerializers.BOOLEAN);
    public SimpleContainer miniInventory;
    public float LayerBrightness, oLayerBrightness;
    public int LayerTicks;
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState sleepAnimationState = new AnimationState();
    public AnimationState operationAnimationState = new AnimationState();
    public AnimationState chestopenAnimationState = new AnimationState();
    public AnimationState chestloopAnimationState = new AnimationState();
    public AnimationState chestcloseAnimationState = new AnimationState();


    public Netherite_Ministrosity_Entity(EntityType type, Level world) {
        super(type, world);
        this.createInventory();
        this.xpReward = 0;
        setConfigattribute(this, CMConfig.MinistrosityHealthMultiplier,1);

    }
    
    @Override
    public float getStepHeight() {
    	return 1.0F;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(6, new TameableAIFollowOwner(this, 1.3D, 6.0F, 2.0F, true));
        //this.goalSelector.addGoal(6, new TemptGoal(this, 1.0D, Ingredient.of(Items.SNIFFER_EGG), false));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0D, 60));
        this.goalSelector.addGoal(1, new InternalPetStateGoal(this,1,1,0,0,0){

            @Override
            public boolean canUse() {
                return super.canUse() ;
            }

            @Override
            public void tick() {
                entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
            }
        });

        this.goalSelector.addGoal(0, new InternalPetStateGoal(this,1,2,0,40,0){
            @Override
            public boolean canUse() {
                return super.canUse() && Netherite_Ministrosity_Entity.this.getIsAwaken();
            }
        });


       // this.goalSelector.addGoal(1, new InternalPetStateGoal(this,5,5,0,10,0));

    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.MINISTROSITY_HURT.get();
    }

    public boolean isSleep() {
        return this.getAttackState() == 1 || this.getAttackState() == 2;
    }

    public boolean isOpen() {
        return this.getAttackState() == 3 || this.getAttackState() == 4;
    }

    public void setIsAwaken(boolean isAwaken) {
        this.entityData.set(IS_AWAKEN, isAwaken);
        if (!isAwaken) {
            this.setAttackState(1);
        }
    }

    public boolean getIsAwaken() {
        return this.entityData.get(IS_AWAKEN);
    }

    protected int getInventorySize() {
        return 17;
    }



    protected void createInventory() {
        SimpleContainer simplecontainer = this.miniInventory;
        this.miniInventory = new SimpleContainer(this.getInventorySize());
        if (simplecontainer != null) {
            simplecontainer.removeListener(this);
            int i = Math.min(simplecontainer.getContainerSize(), this.miniInventory.getContainerSize());

            for(int j = 0; j < i; ++j) {
                ItemStack itemstack = simplecontainer.getItem(j);
                if (!itemstack.isEmpty()) {
                    this.miniInventory.setItem(j, itemstack.copy());
                }
            }
        }

        this.miniInventory.addListener(this);
        this.itemHandler = net.minecraftforge.common.util.LazyOptional.of(() -> new net.minecraftforge.items.wrapper.InvWrapper(this.miniInventory));
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
                Cataclysm.NETWORK_WRAPPER.send(PacketDistributor.PLAYER.with(() -> serverplayer), new MessageMiniinventory(serverplayer.containerCounter, this.miniInventory.getContainerSize(), this.getId()));
                serverplayer.containerMenu = new MinistrostiyMenu(serverplayer.containerCounter, serverplayer.getInventory(), this.miniInventory, this);
                serverplayer.initMenu(serverplayer.containerMenu);
                MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(serverplayer, serverplayer.containerMenu));
            }
        }

    }


    public int getInventoryColumns() {
        return 5;
    }

    public void containerChanged(Container p_30548_) {

    }

    public AnimationState getAnimationState(String input) {
        if (input == "idle") {
            return this.idleAnimationState;
        } else if (input == "sleep") {
            return this.sleepAnimationState;
        } else if (input == "operation") {
            return this.operationAnimationState;
        } else if (input == "chest_open") {
            return this.chestopenAnimationState;
        } else if (input == "chest_loop") {
            return this.chestloopAnimationState;
        } else if (input == "chest_close") {
            return this.chestcloseAnimationState;
        }else {
            return new AnimationState();
        }
    }



    public static AttributeSupplier.Builder ministrosity() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 120.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.ARMOR, 5D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4F);
    }

    protected int decreaseAirSupply(int air) {
        return air;
    }

    public void travel(Vec3 vec3d) {
        if (this.isSitting()) {
            if (this.getNavigation().getPath() != null) {
                this.getNavigation().stop();
            }
            vec3d = Vec3.ZERO;
        }
        super.travel(vec3d);
    }


    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source == DamageSource.IN_WALL || source == DamageSource.FALLING_BLOCK || super.isInvulnerableTo(source);
    }

    protected void dropEquipment() {
        super.dropEquipment();
        if (miniInventory != null) {
            for (int i = 0; i < miniInventory.getContainerSize(); ++i) {
                ItemStack itemstack = miniInventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    this.spawnAtLocation(itemstack, 0.0F);
                }
            }
        }
    }


    public boolean canBreatheUnderwater() {
        return true;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FROM_BUCKET, false);
        this.entityData.define(IS_AWAKEN, false);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("FromBucket", this.fromBucket());
        compound.putBoolean("is_Awaken", this.getIsAwaken());
        ListTag listtag = new ListTag();

        for(int i = 2; i < this.miniInventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.miniInventory.getItem(i);
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
        this.setFromBucket(compound.getBoolean("FromBucket"));
        this.setIsAwaken(compound.getBoolean("is_Awaken"));
        this.createInventory();
        ListTag listtag = compound.getList("Items", 10);

        for(int i = 0; i < listtag.size(); ++i) {
            CompoundTag compoundtag = listtag.getCompound(i);
            int j = compoundtag.getByte("Slot") & 255;
            if (j >= 2 && j < this.miniInventory.getContainerSize()) {
                this.miniInventory.setItem(j, ItemStack.of(compoundtag));
            }
        }

    }


    public void onSyncedDataUpdated(EntityDataAccessor<?> p_21104_) {
        if (ATTACK_STATE.equals(p_21104_)) {
            if (this.level.isClientSide)
                switch (this.getAttackState()) {
                    case 0 -> this.stopAllAnimationStates();
                    case 1 -> {
                        this.stopAllAnimationStates();
                        this.sleepAnimationState.startIfStopped(this.tickCount);
                    }
                    case 2 -> {
                        this.stopAllAnimationStates();
                        this.operationAnimationState.startIfStopped(this.tickCount);
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

        super.onSyncedDataUpdated(p_21104_);
    }


    public void stopAllAnimationStates() {
        this.sleepAnimationState.stop();
        this.operationAnimationState.stop();
        this.chestopenAnimationState.stop();
        this.chestloopAnimationState.stop();
        this.chestcloseAnimationState.stop();
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean sit) {
        this.entityData.set(FROM_BUCKET, sit);
    }

    @Override
    public void saveToBucketTag(@Nonnull ItemStack bucket) {
        CompoundTag platTag = new CompoundTag();
        CompoundTag compound = bucket.getOrCreateTag();
        this.addAdditionalSaveData(platTag);
        Bucketable.saveDefaultDataToBucketTag(this, bucket);
        compound.put("MinistrosityData", platTag);

    }

    @Override
    public void loadFromBucketTag(CompoundTag p_148832_) {
        Bucketable.loadDefaultDataFromBucketTag(this, p_148832_);
        if (p_148832_.contains("MinistrosityData")) {
            this.readAdditionalSaveData(p_148832_.getCompound("MinistrosityData"));
        }
    }

    @Override
    @Nonnull
    public ItemStack getBucketItemStack() {
        ItemStack stack = new ItemStack(ModItems.NETHERITE_MINISTROSITY_BUCKET.get());
        return stack;
    }

    @Override
    public SoundEvent getPickupSound() {
        return ModSounds.MINISTROSITY_FILL_BUCKET.get();
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        boolean owner = this.isTame() && isOwnedBy(player);
        InteractionResult type = super.mobInteract(player, hand);
        if (owner) {
            Optional<InteractionResult> result = emptybucketMobPickup(player, hand, this);
            if (result.isPresent()) {
                return result.get();
            }else{
                if (!player.isShiftKeyDown()) {
                    this.openCustomInventoryScreen(player);
                    this.setCommand(2);
                    this.setOrderedToSit(true);
                    return InteractionResult.sidedSuccess(this.level.isClientSide);
                }
            }

        }


        if (!isTame() && stack.is(ModItems.LAVA_POWER_CELL.get())) {
            this.usePlayerItem(player, hand, stack);
            this.gameEvent(GameEvent.EAT);
            if (!net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
                this.tame(player);
                this.setIsAwaken(true);
                this.level.broadcastEntityEvent(this, (byte) 7);
            } else {
                this.level.broadcastEntityEvent(this, (byte) 6);
            }
            return InteractionResult.SUCCESS;
        }


        if (isTame() && stack.is(Items.COAL) && this.getHealth() < this.getMaxHealth()) {
            this.heal(5);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            this.gameEvent(GameEvent.EAT, this);
            return InteractionResult.SUCCESS;

        }

        InteractionResult interactionresult = stack.interactLivingEntity(player, this, hand);
        if (interactionresult != InteractionResult.SUCCESS && type != InteractionResult.SUCCESS && isTame() && isOwnedBy(player)) {
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


    private static <T extends LivingEntity & Bucketable> Optional<InteractionResult> emptybucketMobPickup(Player p_148829_, InteractionHand p_148830_, T p_148831_) {
        ItemStack itemstack = p_148829_.getItemInHand(p_148830_);
        if (itemstack.getItem() == Items.BUCKET && p_148831_.isAlive()) {
            p_148831_.playSound(p_148831_.getPickupSound(), 1.0F, 1.0F);
            ItemStack itemstack1 = p_148831_.getBucketItemStack();
            p_148831_.saveToBucketTag(itemstack1);
            ItemStack itemstack2 = ItemUtils.createFilledResult(itemstack, p_148829_, itemstack1, false);
            p_148829_.setItemInHand(p_148830_, itemstack2);
            Level level = p_148831_.level;
            if (!level.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)p_148829_, itemstack1);
            }

            p_148831_.discard();
            return Optional.of(InteractionResult.sidedSuccess(level.isClientSide));
        } else {
            return Optional.empty();
        }
    }

    public void aiStep() {
        super.aiStep();
        if ((this.isSitting() || isOpen())  && this.getNavigation().isDone()) {
            this.getNavigation().stop();
        }
        if (this.level.isClientSide()) {
            this.animateWhen(this.idleAnimationState, this.getAttackState() == 0, this.tickCount);
        }
        if(this.getAttackState() == 3){
            if(this.attackTicks == 1){
                this.playSound(SoundEvents.SHULKER_AMBIENT, 1.0F, 2.0F);
            }
            if(this.attackTicks >= 9){
                this.setAttackState(4);
            }


        }
        if(this.getAttackState() == 5){
            if(this.attackTicks == 1){
                this.playSound(SoundEvents.SHULKER_AMBIENT, 1.0F, 2.0F);
            }
            if(this.attackTicks >= 10){
                this.setAttackState(0);
            }
        }

        if (this.level.isClientSide){
            ++LayerTicks;
            this.LayerBrightness += (0.0F - this.LayerBrightness) * 0.8F;
        }

    }
    
    public void animateWhen(AnimationState state, boolean p_252220_, int p_249486_) {
        if (p_252220_) {
        	state.startIfStopped(p_249486_);
        } else {
        	state.stop();
        }
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new SmartBodyHelper2(this);
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (this.isTame()) {
            LivingEntity livingentity = this.getOwner();
            if (entityIn == livingentity) {
                return true;
            }
            if (entityIn instanceof TamableAnimal) {
                return ((TamableAnimal) entityIn).isOwnedBy(livingentity);
            }
            if (livingentity != null) {
                return livingentity.isAlliedTo(entityIn);
            }
        }

        return super.isAlliedTo(entityIn);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        return null;
    }

    @Override
    public boolean shouldFollow() {
        return this.getCommand() == 1;
    }


    private net.minecraftforge.common.util.LazyOptional<?> itemHandler = null;

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.core.Direction facing) {
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
        return this.miniInventory != p_149512_;
    }

}
