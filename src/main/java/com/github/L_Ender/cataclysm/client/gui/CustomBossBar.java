package com.github.L_Ender.cataclysm.client.gui;

import java.util.HashMap;
import java.util.Map;

import com.github.L_Ender.cataclysm.Cataclysm;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.BossEvent;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;

public class CustomBossBar {
    public static Map<Integer, CustomBossBar> customBossBars = new HashMap<>();
    static {
        //N.M
        customBossBars.put(0, new CustomBossBar(
                new ResourceLocation(Cataclysm.MODID, "textures/gui/boss_bar/monstrosity_bar_base.png"),
                new ResourceLocation(Cataclysm.MODID, "textures/gui/boss_bar/monstrosity_bar_overlay.png"),
                5, 16, 0, -2, -2, 256, 16, 25, ChatFormatting.RED));
        //E.G
        customBossBars.put(1, new CustomBossBar(
                new ResourceLocation(Cataclysm.MODID, "textures/gui/boss_bar/ender_guardian_bar_base.png"),
                new ResourceLocation(Cataclysm.MODID, "textures/gui/boss_bar/ender_guardian_bar_overlay.png"),
                10, 16, 0, -2, -2, 256, 16, 25, ChatFormatting.LIGHT_PURPLE));
        //Ignis 1 Phase
        customBossBars.put(2, new CustomBossBar(
                new ResourceLocation(Cataclysm.MODID, "textures/gui/boss_bar/ignis_bar_base.png"),
                new ResourceLocation(Cataclysm.MODID, "textures/gui/boss_bar/ignis_bar_overlay.png"),
                10, 16, 0, -2, -2, 256, 16, 25, ChatFormatting.YELLOW));
        //Ignis 2,3 Phase
        customBossBars.put(3, new CustomBossBar(
                new ResourceLocation(Cataclysm.MODID, "textures/gui/boss_bar/ignis_soul_bar_base.png"),
                new ResourceLocation(Cataclysm.MODID, "textures/gui/boss_bar/ignis_soul_bar_overlay.png"),
                10, 16, 0, -2, -2, 256, 16, 25, ChatFormatting.BLUE));
        //harbinger
        customBossBars.put(4, new CustomBossBar(
                new ResourceLocation(Cataclysm.MODID, "textures/gui/boss_bar/harbinger_bar_base.png"),
                new ResourceLocation(Cataclysm.MODID, "textures/gui/boss_bar/harbinger_bar_overlay.png"),
                5, 16, 7, -2, -8, 256, 32, 25, ChatFormatting.DARK_RED));

        //Levi 1
        customBossBars.put(5, new CustomBossBar(
                new ResourceLocation(Cataclysm.MODID, "textures/gui/boss_bar/leviathan_bar_base.png"),
                new ResourceLocation(Cataclysm.MODID, "textures/gui/boss_bar/leviathan_bar_overlay.png"),
                5, 16, 2, -4, -4, 256, 16, 25, ChatFormatting.DARK_PURPLE));
        //Levi 2
        customBossBars.put(6, new CustomBossBar(
                new ResourceLocation(Cataclysm.MODID, "textures/gui/boss_bar/leviathan_bar_base.png"),
                new ResourceLocation(Cataclysm.MODID, "textures/gui/boss_bar/leviathan_meltdown_bar_overlay.png"),
                5, 16, 4, -4, -6, 256, 16, 25, ChatFormatting.DARK_PURPLE));

        //remnant
        customBossBars.put(7, new CustomBossBar(
                new ResourceLocation(Cataclysm.MODID, "textures/gui/boss_bar/remnant_bar_base.png"),
                new ResourceLocation(Cataclysm.MODID, "textures/gui/boss_bar/remnant_bar_overlay.png"),
                5, 16, 7, -4, -10, 256, 32, 25, ChatFormatting.WHITE));


    }

    private final ResourceLocation baseTexture;
    private final ResourceLocation overlayTexture;
    private final boolean hasOverlay;

    private final int baseHeight;
    private final int baseTextureHeight;
    private final int baseOffsetY;
    private final int overlayOffsetX;
    private final int overlayOffsetY;
    private final int overlayWidth;
    private final int overlayHeight;

    private final int verticalIncrement;

    private final ChatFormatting textColor;

    public CustomBossBar(ResourceLocation baseTexture, ResourceLocation overlayTexture, int baseHeight, int baseTextureHeight, int baseOffsetY, int overlayOffsetX, int overlayOffsetY, int overlayWidth, int overlayHeight, int verticalIncrement, ChatFormatting textColor) {
        this.baseTexture = baseTexture;
        this.overlayTexture = overlayTexture;
        this.hasOverlay = overlayTexture != null;
        this.baseHeight = baseHeight;
        this.baseTextureHeight = baseTextureHeight;
        this.baseOffsetY = baseOffsetY;
        this.overlayOffsetX = overlayOffsetX;
        this.overlayOffsetY = overlayOffsetY;
        this.overlayWidth = overlayWidth;
        this.overlayHeight = overlayHeight;
        this.verticalIncrement = verticalIncrement;
        this.textColor = textColor;
    }

    public ResourceLocation getBaseTexture() {
        return baseTexture;
    }

    public ResourceLocation getOverlayTexture() {
        return overlayTexture;
    }

    public boolean hasOverlay() {
        return hasOverlay;
    }

    public int getBaseHeight() {
        return baseHeight;
    }

    public int getBaseTextureHeight() {
        return baseTextureHeight;
    }

    public int getBaseOffsetY() {
        return baseOffsetY;
    }

    public int getOverlayOffsetX() {
        return overlayOffsetX;
    }

    public int getOverlayOffsetY() {
        return overlayOffsetY;
    }

    public int getOverlayWidth() {
        return overlayWidth;
    }

    public int getOverlayHeight() {
        return overlayHeight;
    }

    public int getVerticalIncrement() {
        return verticalIncrement;
    }

    public ChatFormatting getTextColor() {
        return textColor;
    }

    public void renderBossBar(CustomizeGuiOverlayEvent.BossEventProgress event) {
        int y = event.getY();
        int i = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int j = y - 9;
        int k = i / 2 - 91;
        PoseStack poseStack = event.getPoseStack();
        Minecraft.getInstance().getProfiler().push("CataclysmCustomBossBarBase");

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, getBaseTexture());
        drawBar(poseStack, event.getX() + 1, y + getBaseOffsetY(), event.getBossEvent());
        Component component = event.getBossEvent().getName().copy().withStyle(getTextColor());
        Minecraft.getInstance().getProfiler().pop();

        int l = Minecraft.getInstance().font.width(component);
        int i1 = i / 2 - l / 2;
        int j1 = j;
        Minecraft.getInstance().font.draw(poseStack, component, i1, j1, 16777215);


        if (hasOverlay()) {
            Minecraft.getInstance().getProfiler().push("CataclysmCustomBossBarOverlay");
            RenderSystem.setShaderTexture(0, getOverlayTexture());
            GuiComponent.blit(poseStack, event.getX() + 1 + getOverlayOffsetX(), y + getOverlayOffsetY() + getBaseOffsetY(), 0, 0, getOverlayWidth(), getOverlayHeight(), getOverlayWidth(), getOverlayHeight());
            Minecraft.getInstance().getProfiler().pop();
        }

        event.setIncrement(getVerticalIncrement());
    }

    private void drawBar(PoseStack poseStack, int x, int y, BossEvent event) {
    	RenderSystem.setShaderTexture(0, getBaseTexture());
        GuiComponent.blit(poseStack, x, y, 0, 0, 182, getBaseHeight(), 256, getBaseTextureHeight());
        int i = (int)(event.getProgress() * 183.0F);
        if (i > 0) {
        	RenderSystem.setShaderTexture(0, getBaseTexture());
        	GuiComponent.blit(poseStack, x, y, 0, getBaseHeight(), i, getBaseHeight(), 256, getBaseTextureHeight());
        }
    }
}