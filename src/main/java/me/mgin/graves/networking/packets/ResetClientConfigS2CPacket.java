package me.mgin.graves.networking.packets;

import me.mgin.graves.config.GravesConfig;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class ResetClientConfigS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf _buf,
                               PacketSender sender) {
        GravesConfig.setConfig(new GravesConfig());
        GravesConfig.getConfig().save();
    }
}
