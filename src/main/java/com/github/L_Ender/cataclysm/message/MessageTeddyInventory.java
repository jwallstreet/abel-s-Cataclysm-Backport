package com.github.L_Ender.cataclysm.message;

import java.util.function.Supplier;

import com.github.L_Ender.cataclysm.client.gui.TeddyBearInventoryScreen;
import com.github.L_Ender.cataclysm.entity.Pet.Teddy_Bear_Entity;
import com.github.L_Ender.cataclysm.inventory.TeddyBearMenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class MessageTeddyInventory {
    private final int id;
    private final int size;
    private final int entityId;

    public MessageTeddyInventory(int id, int size, int entityId) {
        this.id = id;
        this.size = size;
        this.entityId = entityId;
    }
    
    public static MessageTeddyInventory read(FriendlyByteBuf buf) {
        return new MessageTeddyInventory(buf.readUnsignedByte(), buf.readVarInt(), buf.readInt());
    }

    public static void write(MessageTeddyInventory message, FriendlyByteBuf buf) {
        buf.writeByte(message.id);
        buf.writeVarInt(message.size);
        buf.writeInt(message.entityId);
    }

    public int getId() {
        return this.id;
    }
    
    public int getSize() {
        return this.size;
    }
    
    public int getEntityId() {
        return this.entityId;
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageTeddyInventory msg, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                openInventory(msg);
            });
            context.get().setPacketHandled(true);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void openInventory(MessageTeddyInventory packet) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            Entity entity = player.level.getEntity(packet.getEntityId());
            if (entity instanceof Teddy_Bear_Entity teddyBear) {
                LocalPlayer clientplayerentity = Minecraft.getInstance().player;
                TeddyBearMenu container = new TeddyBearMenu(packet.getId(), player.getInventory(), teddyBear.inventory, teddyBear);
                clientplayerentity.containerMenu = container;
                Minecraft.getInstance().setScreen(new TeddyBearInventoryScreen(container, player.getInventory(), teddyBear));
            }
        }
    }
}