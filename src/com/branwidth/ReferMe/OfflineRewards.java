package com.branwidth.ReferMe;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class OfflineRewards {

    public static void GiveOfflineRewards(OfflinePlayer player) {
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
            GiveOfflineItem(player, itemID, itemAmount);
        }

        if (CustomItemRewards) {
            GiveOfflineCustomItem(player);
        }

        if (PlayerPointsRewards) {
            GiveOfflinePlayerPoints(player);
        }

        if (MoneyRewards) {
            GiveOfflineMoney(player, MoneyAmount);
        }
    }

    private static void GiveOfflinePlayerPoints(OfflinePlayer player) {
    }

    private static void GiveOfflineCustomItem(OfflinePlayer player) {
    }

    private static void GiveOfflineItem(OfflinePlayer player, int itemID, int itemAccount) {
    }

    public static void GiveOfflineMoney(OfflinePlayer player, int moneyAmount) {
        Economy economy = Main.econ;
        EconomyResponse r = economy.depositPlayer(player, moneyAmount);
        if (r.transactionSuccess()){
        }
    }
}
