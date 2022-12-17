package me.mgin.graves.client.commands;

import me.mgin.graves.config.ConfigOptions;
import me.mgin.graves.config.GravesConfig;
import me.mgin.graves.util.Constants;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static me.mgin.graves.util.ConfigCommandUtil.determineSubClass;
import static me.mgin.graves.util.ConfigCommandUtil.extractNbtValue;

public class SetClientConfig {
    public static void execute(PacketByteBuf buf) {
        GravesConfig config = GravesConfig.getConfig();
        NbtCompound nbt = buf.readNbt();
        if (nbt == null) {
            sendResponse("error.missing-nbt", null);
            return;
        }

        // Passed by respective option handler
        String option = nbt.getString("option");
        String type = nbt.getString("type");
        Object value = extractNbtValue(nbt, option, type);

        // An improper enum value was given.
        if (value == null) {
            sendResponse("error.invalid-enum-value", nbt);
            return;
        }

        // Handle clientOptions commands
        if (option.contains(":")) {
            // Do not add non-option values to the list
            if (!ConfigOptions.all.contains(value)) {
                sendResponse("error.invalid-config-option", nbt);
                return;
            }

            // Generate the value
            String[] options = option.split(":");
            value = updateClientOptions(config, options[1], (String) value);

            // Reassign option so determineSubClass can operate properly
            option = options[0];
        }

        // Sets the config field dynamically
        try {
            Field target = determineSubClass(option);
            Field field = target.getType().getDeclaredField(option);
            field.set(target.get(config), value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // Save the config
        AutoConfig.getConfigHolder(GravesConfig.class).save();
        sendResponse("success", nbt);
    }

    static private List<String> updateClientOptions(GravesConfig config, String secondaryOption, String value) {
        List<String> clientOptions = config.server.clientOptions;

        if (secondaryOption.equals("add")) {
            if (clientOptions.contains(value)) return clientOptions;
            clientOptions.add((String) value);
        }

        if (secondaryOption.equals("remove")) {
            if (!clientOptions.contains(value)) return clientOptions;
            clientOptions.remove((String) value);
        }

        return clientOptions;
    }

    static private void sendResponse(String text, NbtCompound nbt) {
        PacketByteBuf buf = PacketByteBufs.create();
        if (nbt == null) nbt = new NbtCompound();
        nbt.putString("text", text);
        buf.writeNbt(nbt);

        ClientPlayNetworking.send(Constants.SET_CLIENT_CONFIG_DONE, buf);
    }
}