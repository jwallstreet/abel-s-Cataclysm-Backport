package com.github.L_Ender.cataclysm.entity.BossMonsters.AI;

import com.github.L_Ender.cataclysm.entity.AnimationMonster.Animation_Monster;
import com.github.L_Ender.cataclysm.entity.BossMonsters.Boss_monster;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;

import java.util.EnumSet;

public class AttackAniamtionGoal3<T extends Animation_Monster & IAnimatedEntity> extends SimpleAnimationGoal<T> {
    public AttackAniamtionGoal3(T entity, Animation animation) {
        super(entity, animation);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    public void tick() {
        entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
    }
}
