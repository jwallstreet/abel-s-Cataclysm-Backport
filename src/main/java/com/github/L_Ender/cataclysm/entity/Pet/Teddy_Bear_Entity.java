package com.github.L_Ender.cataclysm.entity.Pet;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class Teddy_Bear_Entity extends InternalAnimationPet {

    public Teddy_Bear_Entity(EntityType<? extends Teddy_Bear_Entity> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder teddy_bear_attributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35F);
    }

}