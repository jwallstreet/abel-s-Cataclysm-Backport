package com.github.L_Ender.cataclysm.util;

import com.mojang.math.Quaternion;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class CMMathUtil {
    public static float approachSmooth(float current, float previous, float desired, float desiredSpeed, float deltaSpeed) {
        float prevSpeed = current - previous;
        desiredSpeed = Mth.abs(desiredSpeed);
        desiredSpeed = current < desired ? desiredSpeed : -desiredSpeed;
        float speed = Mth.approach(prevSpeed, desiredSpeed, deltaSpeed);
        float speedApproachReduction = (float) (1.0f - Math.pow(Mth.clamp(-Mth.abs(current - desired)/Mth.abs(2 * desiredSpeed/deltaSpeed) + 1.0f, 0, 1), 4)); // Extra math to make speed smaller when current is close to desired
        speed *= speedApproachReduction;
        return current < desired ? Mth.clamp(current + speed, current, desired) : Mth.clamp(current + speed, desired, current);
    }

    public static float approachDegreesSmooth(float current, float previous, float desired, float desiredSpeed, float deltaSpeed) {
        float desiredDifference = Mth.degreesDifference(current, desired);
        float previousDifference = Mth.degreesDifference(current, previous);
        return approachSmooth(current, current + previousDifference, current + desiredDifference, desiredSpeed, deltaSpeed);
    }
    
    public static Quaternion quatFromRotationXYZ(float x, float y, float z, boolean degrees) {
        return new Quaternion(x, y, z, degrees);
    }
    
    public static Vec3 getOffsetPos(Entity entity, double offsetX, double offsetY, double offsetZ, float rotationX, float rotationY) {
        Vec3 Vec3 = (new Vec3(offsetZ, offsetY, offsetX)).zRot(rotationX * 0.017453292F).yRot(-rotationY * 0.017453292F - 1.5707964F);
        return entity.position().add(Vec3.x, Vec3.y, Vec3.z);
    }

    public static Vec3 getOffsetMotion(Entity entity, double offsetX, double offsetY, double offsetZ, float rotationX, float rotationY) {
        Vec3 Vec3 = (new Vec3(offsetZ, offsetY, offsetX)).zRot(rotationX * 0.017453292F).yRot(-rotationY * 0.017453292F - 1.5707964F);
        return Vec3;
    }

}
