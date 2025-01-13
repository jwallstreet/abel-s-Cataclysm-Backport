package com.github.L_Ender.cataclysm.init;

import javax.annotation.Nullable;

import com.github.L_Ender.cataclysm.capabilities.ChargeCapability;
import com.github.L_Ender.cataclysm.capabilities.HookCapability;
import com.github.L_Ender.cataclysm.capabilities.ParryCapability;
import com.github.L_Ender.cataclysm.capabilities.RenderRushCapability;
import com.github.L_Ender.cataclysm.capabilities.TidalTentacleCapability;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;

public final class ModCapabilities {

    public static final Capability<HookCapability.IHookCapability> HOOK_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<TidalTentacleCapability.ITentacleCapability> TENTACLE_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<ChargeCapability.IChargeCapability> CHARGE_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<RenderRushCapability.IRenderRushCapability> RENDER_RUSH_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<ParryCapability.IParryCapability> PARRY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});


    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(HookCapability.HookCapabilityImp.class);
        event.register(RenderRushCapability.IRenderRushCapability.class);
        event.register(ChargeCapability.ChargeCapabilityImp.class);
        event.register(TidalTentacleCapability.TentacleCapabilityImp.class);
        event.register(ParryCapability.ParryCapabilityImp.class);
    }

    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> e) {
        if (e.getObject() instanceof LivingEntity living) {
            e.addCapability(HookCapability.ID, new HookCapability.HookCapabilityImp.HookProvider());
            e.addCapability(ChargeCapability.ID, new ChargeCapability.ChargeCapabilityImp.ChargeProvider());
            e.addCapability(RenderRushCapability.ID, new RenderRushCapability.RenderRushCapabilityImp.RenderRushProvider());
            e.addCapability(TidalTentacleCapability.ID, new TidalTentacleCapability.TentacleCapabilityImp.TentacleProvider());
            e.addCapability(ParryCapability.ID, new ParryCapability.ParryCapabilityImp.ParryProvider());
        }
    }

    @Nullable
    public static <T> T getCapability(Entity entity, Capability<T> capability) {
        if (entity == null) return null;
        if (!entity.isAlive()) return null;
        return entity.getCapability(capability).isPresent() ? entity.getCapability(capability).orElseThrow(() -> new IllegalArgumentException("Lazy optional must not be empty")) : null;
    }

}
