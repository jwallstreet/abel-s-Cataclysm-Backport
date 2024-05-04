package com.github.L_Ender.cataclysm.entity.AnimationMonster.AI;

import java.util.EnumSet;

import com.github.L_Ender.cataclysm.entity.AnimationMonster.Animation_Monster;
import com.github.L_Ender.lionfishapi.server.animation.Animation;
import com.github.L_Ender.lionfishapi.server.animation.IAnimatedEntity;

public class AttackAniamtionGoal3<T extends Animation_Monster & IAnimatedEntity> extends SimpleAnimationGoal<T> {
    public AttackAniamtionGoal3(T entity, Animation animation) {
        super(entity, animation);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    public void tick() {
        entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
    }
}
