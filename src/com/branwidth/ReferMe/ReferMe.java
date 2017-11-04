package com.branwidth.ReferMe;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.SQLException;
import java.util.UUID;


public class ReferMe implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        String playerName = player.getName();
        UUID playerUUID = player.getUniqueId();
        Integer argsLength = args.length;

        if (argsLength == 0) {
            sendMenu(player);
        } else if (argsLength == 1) {
            String argCommand = args[0];
            switch (argCommand) {
                case "help":
                    sendMenu(player);
                    break;
                case "check":
                    checkSelf(playerUUID);
                    break;
                default:
                    sendMenu(player);
            }
        } else if (argsLength == 2) {
            String argCommand = args[0];
            String argPlayer = args[1];
            switch (argCommand){
                case "thanks":
                    thankOther(playerUUID, argPlayer);
                    break;
                case "check":
                    checkOther(argPlayer);
                    break;
                case "top":
                    checkTop(argPlayer);
                default:
                    sendMenu(player);
            }
        } else {
            sendMenu(player);
        }
        return true;
    }

    private void checkTop(Object argPlayer) throws SQLException {
        int Count = (int)argPlayer;
        Database.getTopReferrers(Count);
    }

    private void sendMenu(Player player){
        player.sendMessage("§l§6Refer§r§cMe - §fReferral Plugin");
        player.sendMessage("§7-----------------------------------");
        player.sendMessage("§d>  §f/referme help §b- §7Get Plugin help");
        player.sendMessage("§d>  §f/referme thanks [Player Name] §b- §7Thank a player for referring you");
        player.sendMessage("§d>  §f/referme check §b- §7Check the amount of players you have referred");
        player.sendMessage("§d>  §f/referme check [Player Name] §b- §7Check the amount of players another player has referred");
        player.sendMessage("§d>  §f/referme top [#] §b- §7Check the top Referrers by amount");
    }

    private void checkSelf(UUID uuid){

    }

    private void checkOther(String referrerName){

    }

    private void thankOther(UUID referredUUID, String referrerName){

    }
}
