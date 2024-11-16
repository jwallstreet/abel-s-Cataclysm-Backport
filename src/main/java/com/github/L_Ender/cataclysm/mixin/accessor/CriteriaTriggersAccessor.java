package com.github.L_Ender.cataclysm.mixin.accessor;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.resources.ResourceLocation;

@Mixin(CriteriaTriggers.class)
public interface CriteriaTriggersAccessor {
    @Accessor("CRITERIA")
    static Map<ResourceLocation, CriterionTrigger<?>> getValues() {
        throw new AssertionError();
    }
}