package com.github.L_Ender.cataclysm.items;

import com.github.L_Ender.cataclysm.entity.Pet.Teddy_Bear_Entity;
import com.github.L_Ender.cataclysm.init.ModEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class Teddy_Bear_Effigy_Item extends Item {

    public Teddy_Bear_Effigy_Item(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            ItemStack itemstack = context.getItemInHand();
            BlockPos blockpos = context.getClickedPos().relative(context.getClickedFace());
            
            Teddy_Bear_Entity teddyBear = (Teddy_Bear_Entity) ModEntities.TEDDY_BEAR.get().spawn(
                serverLevel, 
                itemstack, 
                context.getPlayer(), 
                blockpos, 
                MobSpawnType.SPAWN_EGG, 
                true, 
                false
            );
            
            if (teddyBear != null) {
                // Set the teddy bear to dormant state
                teddyBear.setIsAwaken(false);
                
                // Consume the item if not in creative mode
                if (!context.getPlayer().getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                
                return InteractionResult.CONSUME;
            }
        }
        
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}