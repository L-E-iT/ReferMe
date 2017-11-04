package com.branwidth.ReferMe;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class Database {
    static String dbname = "data";
    private static Connection conn;


    public static Connection getConnection() {
        File dataFolder = new File(Main.getPlugin().getDataFolder(), dbname+".db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                Main.getPlugin().getLogger().severe("File write error: "+dbname+".db");
            }
        }
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return conn;
        } catch (SQLException ex) {
            Main.getPlugin().getLogger().severe("SQLite exception on initialize" + ex.toString());
        } catch (ClassNotFoundException ex) {
            Main.getPlugin().getLogger().severe( "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }


    protected static void Disconnect() {
        if (isConnected()) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected static Boolean isConnected() {
        return (conn != null);
    }


    // Create Database From Nothing
    protected static void createDatabase() {
        String sqlCreatePlayerReferredTable = "CREATE TABLE IF NOT EXISTS playersReferred (" +
                " uuid VARCHAR PRIMARY KEY," +
                " isReferred INT NOT NULL," +
                " referredBy VARCHAR )";

        String sqlCreateDataTable = "CREATE TABLE IF NOT EXISTS playersData (" +
                " uuid VARCHAR PRIMARY KEY," +
                " playersReferred INT NOT NULL)";

        try {
            Statement createStatement = conn.createStatement();
            createStatement.execute(sqlCreatePlayerReferredTable);
            createStatement.execute(sqlCreateDataTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setIsReferred() {

    }

    public boolean getIsReferred() {
        return true;
    }

    public void createUser() {

    }

    public void setReferredBy() {

    }

    public boolean getReferredBy() {
        return true;
    }

    public int getPlayersReferred() {
        return 0;
    }

    public void addToPlayersReferred() {

    }

    public void removeFromPlayersReferred() {

    }

    public ArrayList<String> getTopReferrers() {
        return new ArrayList<>();
    }
}
