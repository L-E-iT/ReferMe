package com.branwidth.ReferMe;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Rewards {

    public static void GiveRewards(Player player) {
        // Add rewards statements

        // Check for which rewards to give
        Boolean ItemRewards = Main.getPlugin().getConfig().getBoolean("Rewards.Item");
        Boolean CustomItemRewards = Main.getPlugin().getConfig().getBoolean("Rewards.CustomItems");
        Boolean PlayerPointsRewards = Main.getPlugin().getConfig().getBoolean("Rewards.PlayerPoints");
        Boolean MoneyRewards = Main.getPlugin().getConfig().getBoolean("Rewards.Money");

        // Check reward amounts
        int MoneyAmount = Main.getPlugin().getConfig().getInt("Money.Amount");
        int itemID = Main.getPlugin().getConfig().getInt("Item.Item");
        int itemAmount = Main.getPlugin().getConfig().getInt("Item.Amount");

        if (ItemRewards) {
            GiveItem(player, itemID, itemAmount);
        }

        if (CustomItemRewards) {
            GiveCustomItem(player);
        }

        if (PlayerPointsRewards) {
            GivePlayerPoints(player);
        }

        if (MoneyRewards) {
            GiveMoney(player, MoneyAmount);
        }
    }

    private static void GivePlayerPoints(Player player) {
    }

    private static void GiveCustomItem(Player player) {
    }

    private static void GiveItem(Player player, int itemID, int itemAccount) {
    }

    public static void GiveMoney(Player player, int moneyAmount) {
        Economy economy = Main.econ;
        EconomyResponse r = economy.depositPlayer(player, moneyAmount);
        if (r.transactionSuccess()){
            player.sendMessage("§aThanks for using §6Referme! §aHere is §6100 §a" + economy.currencyNamePlural());
        }
    }
}
