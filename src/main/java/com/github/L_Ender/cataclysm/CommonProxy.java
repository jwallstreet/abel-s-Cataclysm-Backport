package com.github.L_Ender.cataclysm;

import static com.github.L_Ender.cataclysm.Cataclysm.MODID;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonProxy {


    public void init() {
    }
    public Player getClientSidePlayer() {
        return null;
    }

    public void clientInit() {
    }
    
    public void blockRenderingEntity(UUID id) {
    }

    public void releaseRenderingEntity(UUID id) {
    }

    public boolean isFirstPersonPlayer(Entity entity) {
        return false;
    }

    public Object getISTERProperties() {
        return null;
    }

    public Object getArmorRenderProperties() {
        return null;
    }

    public void onEntityStatus(Entity entity, byte updateKind) {
    }


    public void clearSoundCacheFor(Entity entity) {

    }

    public float getPartialTicks() {
        return 1.0F;
    }


    public boolean isKeyDown(int keyType) {
        return false;
    }

    public void clearSoundCacheFor(BlockEntity entity) {

    }

    public void playWorldSound(@Nullable Object soundEmitter, byte type) {

    }

    public void removeBossBarRender(UUID bossBar) {
    }

    public void setBossBarRender(UUID bossBar, int renderType) {
    }

}
