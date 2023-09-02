package me.mgin.graves.command.config;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.mgin.graves.networking.config.ConfigNetworking;
import me.mgin.graves.util.Responder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

public class C2SSyncConfigCommand {
    static public int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        Responder res = new Responder(source.getPlayer(), source.getServer());

        if (source.getEntity() instanceof ServerPlayerEntity player) {
            if (player.hasPermissionLevel(4))
                ServerPlayNetworking.send(player, ConfigNetworking.REQUEST_CONFIG_S2C, PacketByteBufs.create());

            res.sendSuccess(new TranslatableText("command.server.config.sync:success"), null);
        } else {
            res.sendError(new TranslatableText("command.generic:error.not-player"), null);
        }

        return Command.SINGLE_SUCCESS;
    }
}
