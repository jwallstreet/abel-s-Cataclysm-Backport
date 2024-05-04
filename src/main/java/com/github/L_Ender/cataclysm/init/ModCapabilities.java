package com.github.L_Ender.cataclysm.init;

import javax.annotation.Nullable;

import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.capabilities.ChargeCapability;
import com.github.L_Ender.cataclysm.capabilities.Gone_With_SandstormCapability;
import com.github.L_Ender.cataclysm.capabilities.HookCapability;
import com.github.L_Ender.cataclysm.capabilities.TidalTentacleCapability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

public final class ModCapabilities {

    public static final Capability<HookCapability.IHookCapability> HOOK_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<TidalTentacleCapability.ITentacleCapability> TENTACLE_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<ChargeCapability.IChargeCapability> CHARGE_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<Gone_With_SandstormCapability.IGone_With_SandstormCapability> GONE_WITH_SANDSTORM_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});


    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(HookCapability.HookCapabilityImp.class);
        event.register(ChargeCapability.ChargeCapabilityImp.class);
        event.register(TidalTentacleCapability.TentacleCapabilityImp.class);
        event.register(Gone_With_SandstormCapability.Gone_With_SandstormCapabilityImp.class);
    }

    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> e) {
        if (e.getObject() instanceof LivingEntity living) {
            e.addCapability(HookCapability.ID, new HookCapability.HookCapabilityImp.HookProvider());
            e.addCapability(ChargeCapability.ID, new ChargeCapability.ChargeCapabilityImp.ChargeProvider());
            e.addCapability(TidalTentacleCapability.ID, new TidalTentacleCapability.TentacleCapabilityImp.TentacleProvider());
            if (e.getObject() instanceof Player player) {
                Gone_With_SandstormCapability.Gone_With_SandstormCapabilityImp spellHolder = new Gone_With_SandstormCapability.Gone_With_SandstormCapabilityImp(player);
                attachCapability(e, spellHolder, GONE_WITH_SANDSTORM_CAPABILITY, "sandstorm_cap");
            }
        }
    }

    @Nullable
    public static <T> T getCapability(Entity entity, Capability<T> capability) {
        if (entity == null) return null;
        if (!entity.isAlive()) return null;
        return entity.getCapability(capability).isPresent() ? entity.getCapability(capability).orElseThrow(() -> new IllegalArgumentException("Lazy optional must not be empty")) : null;
    }


    private static <T extends Tag, C extends INBTSerializable<T>> void attachCapability(AttachCapabilitiesEvent<?> event, C capData, Capability<C> capability, String name)
    {
        LazyOptional<C> optional = LazyOptional.of(() -> capData);
        ICapabilitySerializable<T> provider = new ICapabilitySerializable<>()
        {
            @Override
            public <S> LazyOptional<S> getCapability(Capability<S> cap, Direction side)
            {
                if(cap == capability)
                {
                    return optional.cast();
                }

                return LazyOptional.empty();
            }

            @Override
            public T serializeNBT()
            {
                return capData.serializeNBT();
            }

            @Override
            public void deserializeNBT(T tag)
            {
                capData.deserializeNBT(tag);
            }
        };

        event.addCapability(new ResourceLocation(Cataclysm.MODID, name), provider);
    }

}
