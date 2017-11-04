package com.branwidth.ReferMe;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;


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

    /*
    * Table Reference:
    * Table: playersReferred
    * Columns: uuid, isReferred, referredBy
    *
    * Table: playersData
    * Columns: uuid, playersReferred
    * */

    /*
    * Set if a user is Referred
    *
    * @param uuid
    * */
    public static void setIsReferred(UUID uuid) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("UPDATE playersReferred SET isReferred = ? WHERE uuid = ?");
        pstmt.setInt(1, 1);
        pstmt.setString(2, uuid.toString());
        pstmt.execute();
    }

    /*
    * Set if a user is not Referred
    *
    * @param uuid
    * */
    public static void setIsNotReferred(UUID uuid) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("UPDATE playersReferred SET isReferred = ? WHERE uuid = ?");
        pstmt.setInt(1, 0);
        pstmt.setString(2, uuid.toString());
        pstmt.execute();
    }

    /*
    * Get if a user is Referred
    *
    * @param uuid
    * */
    public static boolean getIsReferred(UUID uuid) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("SELECT isReferred FROM playersReferred WHERE uuid = ?");
        pstmt.setString(1, uuid.toString());

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            if (rs.getInt("isReferred") == 1) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /*
    * Get if a user exists
    *
    * @param uuid
    * */
    public static boolean isUser(UUID uuid) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM playersReferred WHERE uuid = ?");
        pstmt.setString(1, uuid.toString());

        if (pstmt.executeQuery().next()) {
            return true;
        } else {
            return false;
        }
    }

    /*
    * Create a user, set isReferred to 0, no Referrer
    *
    * @param uuid
    * */
    public static void createUser(UUID uuid) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO playersReferred(uuid, isReferred, referredBy) VALUES(?,?,?)");
        pstmt.setString(1, uuid.toString());
        pstmt.setInt(2, 0);
        pstmt.setString(3, null);

        pstmt.execute();
    }

    /*
    * Set who a user is ReferredBy
    *
    * @param uuidReferred
    * @param uuidReferrer
    * */
    public static void setReferredBy(UUID uuidReferred, UUID uuidReferrer) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("UPDATE playersReferred SET referredBy = ? WHERE uuid = ?");
        pstmt.setString(1, uuidReferrer.toString());
        pstmt.setString(2, uuidReferred.toString());

        pstmt.execute();
    }

    /*
    * Get who a user is Referred by
    *
    * @param uuid
    * */
    public static String getReferredBy(UUID uuid) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("SELECT referredBy FROM playersReferred WHERE uuid = ?");
        pstmt.setString(1, uuid.toString());

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            if (!rs.getString("referredBy").equals(null)) {
                return rs.getString("referredBy");
            } else {
                return null;
            }
        }
        return null;
    }

    /*
    * Get the amount of users a player has referred
    *
    * @param uuid
    * */
    public static int getPlayersReferred(UUID uuid) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("SELECT playersReferred FROM playersData WHERE uuid = ?");
        pstmt.setString(1, uuid.toString());

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            return rs.getInt("playersReferred");
        }
        return 0;
    }

    /*
    * Add 1 to the amount a user has referred
    *
    * @param uuid
    * */
    public static void addToPlayersReferred(UUID uuid) throws SQLException {
        int currentReferredCount = getPlayersReferred(uuid);
        PreparedStatement pstmt = conn.prepareStatement("UPDATE playersData SET playersReferred = ? WHERE uuid = ?");

        pstmt.setInt(1, ++currentReferredCount);
        pstmt.setString(2, uuid.toString());

        pstmt.execute();
    }

    /*
    * subtract 1 to the amount a user has referred
    *
    * @param uuid
    * */
    public static void removeFromPlayersReferred(UUID uuid) throws SQLException {
        int currentReferredCount = getPlayersReferred(uuid);
        PreparedStatement pstmt = conn.prepareStatement("UPDATE playersData SET playersReferred = ? WHERE uuid = ?");

        pstmt.setInt(1, --currentReferredCount);
        pstmt.setString(2, uuid.toString());

        pstmt.execute();
    }

    /*
    * Get a list of the top Referrers, specified amount in list by int
    *
    * @param count
    * */
    public static Map<String, Integer> getTopReferrers(int count) throws SQLException {
        Map<String, Integer> playerList = new HashMap<>();
        int i = 0;
        PreparedStatement pstmt = conn.prepareStatement("SELECT playersReferred, uuid FROM playersData ORDER BY playersReferred DESC");
        ResultSet rs = pstmt.executeQuery();

        while (rs.next())
            if (i < count) {
                playerList.put(rs.getString("uuid"), rs.getInt("playersReferred"));
                i++;
            } else {
                break;
            }
        return playerList;
    }
}
