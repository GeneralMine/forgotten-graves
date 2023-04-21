package me.mgin.graves.command.restore;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.mgin.graves.block.utility.RetrieveGrave;
import me.mgin.graves.state.PlayerState;
import me.mgin.graves.state.ServerState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;

import static me.mgin.graves.command.utility.ArgumentUtility.getProfileArgument;

public class RestoreCommand {
    static public int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        MinecraftServer server = context.getSource().getServer();
        GameProfile player = getProfileArgument(context, "player", 4);
        Integer graveId = context.getArgument("graveid", Integer.class) - 1; // Remove one for zero-indexing

        // Attempt to get the optional recipient's game profile
        GameProfile recipient = getProfileArgument(context, "recipient", 6);

        // Get player state for given player
        PlayerState playerState = ServerState.getPlayerState(server, player.getId());

        // Ensure grave exists within player's PlayerState.graves
        if (graveId + 1 > playerState.graves.size()) {
            System.out.println("Grave does not exist!");
            return Command.SINGLE_SUCCESS;
        }

        // Get requested grave
        NbtCompound grave = (NbtCompound) playerState.graves.get(graveId);

        // Get player entity and ensure they are online
        PlayerEntity playerEntity = server.getPlayerManager().getPlayer(
            recipient != null ? recipient.getId() : player.getId()
        );

        if (playerEntity == null) {
            System.out.println("Player is not online!");
            return Command.SINGLE_SUCCESS;
        }

        // Restore grave
        RetrieveGrave.retrieveWithCommand(playerEntity, grave);

        return Command.SINGLE_SUCCESS;
    }
}