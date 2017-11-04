package com.branwidth.ReferMe;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
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
            Object argPlayer = args[1];
            switch (argCommand){
                case "thanks":
                    thankOther(playerUUID, argPlayer);
                    break;
                case "check":
                    checkOther(argPlayer, player);
                    break;
                case "top":
                    try {
                        checkTop(argPlayer);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                default:
                    sendMenu(player);
            }
        } else {
            sendMenu(player);
        }
        return true;
    }

    private void checkTop(Object argPlayer, Player player) throws SQLException {
        int Count = (int)argPlayer;
        int Counti = 0;
        Map<String, Integer> topReferrers = Database.getTopReferrers(Count);
        Iterator it = topReferrers.entrySet().iterator();

        player.sendMessage("§l§6Refer§r§cMe - §fReferral Plugin");
        player.sendMessage("§7----§o§6Top Referrers§r§f-------------------------------");
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            OfflinePlayer offlinePlayer =  Bukkit.getOfflinePlayer((UUID) pair.getKey());
            String playerName = offlinePlayer.getName();
            player.sendMessage("§d" + ++Counti + "§7: " + playerName + "§f| Players Referred - §7[§2" + pair.getValue() + "§7]");
            it.remove();
        }
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

    private void checkOther(Object referrerName){

    }

    private void thankOther(UUID referredUUID, Object referrerName){

    }
}
