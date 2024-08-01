package com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters;


import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.Internal_Animation_Monster;
import com.github.L_Ender.cataclysm.init.ModTag;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

public class IABoss_monster extends Internal_Animation_Monster implements Enemy {

    public IABoss_monster(EntityType entity, Level world) {
        super(entity, world);
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (source.isBypassInvul()) {
            return super.hurt(source, damage);
        } else {
            damage = Math.min(DamageCap(), damage);
        }
        return super.hurt(source, damage);
    }

    public float DamageCap() {
        return Float.MAX_VALUE;
    }


    public boolean canBePushedByEntity(Entity entity) {
        return true;
    }


    public boolean canBeAffected(MobEffectInstance p_34192_) {
        return ModTag.EFFECTIVE_FOR_BOSSES_LOOKUP.contains(p_34192_.getEffect()) && super.canBeAffected(p_34192_);
    }

    public boolean removeWhenFarAway(double p_21542_) {
        return false;
    }

    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    protected boolean canRide(Entity p_31508_) {
        return false;
    }
}
