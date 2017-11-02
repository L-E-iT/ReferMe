package com.branwidth.ReferMe;

import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {
    public static Economy econ = null;
    public static PlayerPoints playerPoints = null;

    @Override
    public void onEnable(){
        getLogger().info("Enabled ReferMe");
        // Create Config
        createFiles();

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - no Vault dependency found! Not using PlayerPoints", getDescription().getName()));
            getConfig().set("Rewards.Money", false);
            saveResource("config.yml", true);
            return;
        }
//        if (!hookPlayerPoints() ) {
//            getLogger().warning(String.format("[%s] - no PlayerPoints dependency found! Not using PlayerPoints", getDescription().getName()));
//            getConfig().set("Rewards.PlayerPoints", false);
//            saveResource("config.yml", true);
//            return;
//        }

        // Set Command
        getCommand("ReferMe").setExecutor(new ReferMe());

    }

//    private boolean hookPlayerPoints() {
//        try {
//            final Plugin plugin = this.getServer().getPluginManager().getPlugin("PlayerPoints");
//            playerPoints = PlayerPoints.class.cast(plugin);
//            return playerPoints != null;
//        } catch (Exception e) {
//            return false;
//        }
//    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private void createFiles() {

        File configf = new File(getDataFolder(), "config.yml");

        if (!configf.exists()) {
            configf.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
    }

    @Override
    public void onDisable(){
        // Some text here for stopping the plugin
        getLogger().info("Disabled ReferMe");
    }


    public static Main getPlugin() {
        return Main.getPlugin(Main.class);
    }
}
