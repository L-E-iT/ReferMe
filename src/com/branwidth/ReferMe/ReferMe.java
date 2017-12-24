package com.branwidth.ReferMe;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;


//TODO fix money reward bonuses
//TODO Add more config options
//TODO fix top player rankings

public class ReferMe implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        String playerName = player.getName();
        UUID playerUUID = player.getUniqueId();
        Integer argsLength = args.length;

        try {
            if (!Database.isUser(playerUUID)) {
                Database.createUser(playerUUID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (argsLength == 0) {
            sendMenu(player);
        } else if (argsLength == 1) {
            String argCommand = args[0];
            switch (argCommand.toLowerCase()) {
                case "help":
                    sendMenu(player);
                    break;
                case "check":
                    try {
                        checkSelf(playerUUID, player);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    sendMenu(player);
            }
        } else if (argsLength == 2) {
            String argCommand = args[0];
            String argPlayer = args[1];
            switch (argCommand.toLowerCase()){
                case "thanks":
                    try {
                        thankOther(playerUUID, argPlayer, player);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "check":
                    checkOther(argPlayer, player);
                    break;
                case "top":
                    try {
                        checkTop(Integer.parseInt(argPlayer), player);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    sendMenu(player);
            }
        } else {
            sendMenu(player);
        }
        return true;
    }

    private void checkTop(int countNumber, Player player) throws SQLException {
        int Count = countNumber;
        int Counti = 0;
        Map<String, Integer> topReferrers = Database.getTopReferrers(Count);
        Iterator it = topReferrers.entrySet().iterator();

        player.sendMessage("§7----§o§6Top Referrers§r§7----------");
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            OfflinePlayer offlinePlayer =  Bukkit.getOfflinePlayer(UUID.fromString((String) pair.getKey()));
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

    private void checkSelf(UUID uuid, Player player) throws SQLException {
        int Count = Database.getPlayersReferred(uuid);
        if (Count == 1) {
            player.sendMessage("You have referred §2" + Count + " §fplayer");
        } else {
            player.sendMessage("You have referred §2" + Count + " §fplayers");
        }
    }

    private void checkOther(String referrerName, Player player){
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(referrerName);
        try {
            int Count = Database.getPlayersReferred(offlinePlayer.getUniqueId());
            if (Count == 1) {
                player.sendMessage(referrerName + " has referred §2" + Count + " §fplayer");
            } else {
                player.sendMessage(referrerName + " has referred §2" + Count + " §fplayers");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage("§cThere was an error looking up this user.");
        }
    }

    private void thankOther(UUID referredUUID, String referrerName, Player player) throws SQLException {
        try {
            if (!Database.isUser(Bukkit.getOfflinePlayer(referrerName).getUniqueId())){
                Database.createUser(Bukkit.getOfflinePlayer(referrerName).getUniqueId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (Database.getIsReferred(referredUUID)) {
            Object referredBy = Database.getReferredBy(referredUUID);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString((String) referredBy));
            player.sendMessage("§cYou've already set §4" + offlinePlayer.getName() + " §cas your referrer!");
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(referrerName);

            if (offlinePlayer.getUniqueId() == referredUUID) {
                player.sendMessage("§cYou cannot refer yourself!");
            } else {
                // Set that the players is now referred
                Database.setIsReferred(referredUUID);

                // Set who the player is referred by
                Database.setReferredBy(referredUUID, offlinePlayer.getUniqueId());

                // Add 1 to the referrers count
                Database.addToPlayersReferred(offlinePlayer.getUniqueId());

                player.sendMessage("§2You've set §6" + referrerName + " §2as your referrer!");

                Rewards.GiveRewards(player);
                if (offlinePlayer.isOnline()) {
                    Rewards.GiveRewards(offlinePlayer.getPlayer());
                } else {
                    OfflineRewards.GiveOfflineRewards(offlinePlayer);
                }

            }
        }

    }
}
