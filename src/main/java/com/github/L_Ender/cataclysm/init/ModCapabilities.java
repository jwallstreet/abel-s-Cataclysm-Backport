package com.github.L_Ender.cataclysm.init;

import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.capabilities.Bloom_Stone_PauldronsCapability;
import com.github.L_Ender.cataclysm.capabilities.ChargeCapability;
import com.github.L_Ender.cataclysm.capabilities.Gone_With_SandstormCapability;
import com.github.L_Ender.cataclysm.capabilities.HookCapability;
import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nullable;

public final class ModCapabilities {

    public static final Capability<HookCapability.IHookCapability> HOOK_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<ChargeCapability.IChargeCapability> CHARGE_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<Bloom_Stone_PauldronsCapability.IBloom_Stone_PauldronsCapability> BLOOM_STONE_PAULDRONS_CAPABILITY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<Gone_With_SandstormCapability.IGone_With_SandstormCapability> GONE_WITH_SANDSTORM_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(HookCapability.HookCapabilityImp.class);
        event.register(ChargeCapability.ChargeCapabilityImp.class);
        event.register(Bloom_Stone_PauldronsCapability.Bloom_Stone_PauldronsCapabilityImp.class);
        event.register(Gone_With_SandstormCapability.Gone_With_SandstormCapabilityImp.class);
    }

    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> e) {
        if (e.getObject() instanceof LivingEntity) {
            e.addCapability(HookCapability.ID, new HookCapability.HookCapabilityImp.HookProvider());
            e.addCapability(ChargeCapability.ID, new ChargeCapability.ChargeCapabilityImp.ChargeProvider());
            if (e.getObject() instanceof Player player) {
                e.addCapability(Bloom_Stone_PauldronsCapability.ID, new Bloom_Stone_PauldronsCapability.Bloom_Stone_PauldronsCapabilityImp.Bloom_Stone_PauldronsProvider());
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
