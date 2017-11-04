package com.branwidth.ReferMe;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class Database {
    static String dbname = "data";
    private static String connectionString = "jdbc:sqlite:database.db";
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
//            if(conn != null && !conn.isClosed()){
//                return conn;
//            }
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

//    protected static void Connect() {
//        if (!isConnected()) {
//            try {
//                conn = DriverManager.getConnection(connectionString);
//                Main.getPlugin().getLogger().info("ReferMe Database Connection Established");
//            } catch (SQLException e) {
//                Main.getPlugin().getLogger().severe(e.getMessage());
//            }
//        }
//    }

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

//    public static Connection getConnection() {
//        return conn;
//    }


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
}
