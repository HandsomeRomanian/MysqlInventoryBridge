package ca.matai.crossinvent.inventory.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ca.matai.crossinvent.inventory.Inv;
import ca.matai.crossinvent.inventory.objects.DatabaseInventoryData;
import org.bukkit.entity.Player;

public class InvMysqlInterface {

    private Inv inv;

    public InvMysqlInterface(Inv inv) {
        this.inv = inv;
    }

    public boolean hasAccount(Player player) {
        PreparedStatement preparedUpdateStatement = null;
        ResultSet result = null;
        Connection conn = inv.getDatabaseManager().getConnection();
        if (conn != null) {
            try {
                String sql = "SELECT `player_uuid` FROM `" + inv.getConfigHandler().getString("database.mysql.tableName") + "` WHERE `player_uuid` = ? LIMIT 1";
                preparedUpdateStatement = conn.prepareStatement(sql);
                preparedUpdateStatement.setString(1, player.getUniqueId().toString());

                result = preparedUpdateStatement.executeQuery();
                while (result.next()) {
                    return true;
                }
            } catch (SQLException e) {
                Inv.log.warning("Error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    if (result != null) {
                        result.close();
                    }
                    if (preparedUpdateStatement != null) {
                        preparedUpdateStatement.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean createAccount(Player player) {
        PreparedStatement preparedStatement = null;
        Connection conn = inv.getDatabaseManager().getConnection();
        if (conn != null) {
            try {
                String sql = "INSERT INTO `" + inv.getConfigHandler().getString("database.mysql.tableName") + "`(`player_uuid`, `player_name`, `inventory`, `armor`, `ender_chest`, `health`, `experience`, `food`, `saturation`, `sync_complete`, `last_seen`) " + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, player.getUniqueId().toString());
                preparedStatement.setString(2, player.getName());
                preparedStatement.setString(3, "none");
                preparedStatement.setString(4, "none");
                preparedStatement.setString(5, "none");
                preparedStatement.setString(6, "none");
                preparedStatement.setString(7, "none");
                preparedStatement.setString(8, "none");
                preparedStatement.setString(9, "none");
                preparedStatement.setString(10, "true");
                preparedStatement.setString(11, String.valueOf(System.currentTimeMillis()));

                preparedStatement.executeUpdate();
                return true;
            } catch (SQLException e) {
                Inv.log.warning("Error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean setData(Player player, DatabaseInventoryData pData) {
        if (!hasAccount(player)) {
            createAccount(player);
        }
        PreparedStatement preparedUpdateStatement = null;
        Connection conn = inv.getDatabaseManager().getConnection();
        if (conn != null) {
            try {
                String data = "UPDATE `" + inv.getConfigHandler().getString("database.mysql.tableName") + "` " + "SET `player_name` = ?" + ", `inventory` = ?" + ", `armor` = ?" + ", `ender_chest` = ?" + ", `health` = ?" + ", `experience` = ?" + ", `food` = ?" + ", `saturation` = ?" + ", `sync_complete` = ?" + ", `last_seen` = ?" + " WHERE `player_uuid` = ?";
                preparedUpdateStatement = conn.prepareStatement(data);
                preparedUpdateStatement.setString(1, player.getName());
                preparedUpdateStatement.setString(2, pData.getRawInventory());
                preparedUpdateStatement.setString(3, pData.getRawArmor());
                preparedUpdateStatement.setString(4, pData.getRawEnderChest());
                System.out.println("HP: "+pData.getRawHealth());
                preparedUpdateStatement.setString(5, pData.getRawHealth());
                preparedUpdateStatement.setString(6, pData.getRawEXP());
                preparedUpdateStatement.setString(7, pData.getRawFood());
                preparedUpdateStatement.setString(8, pData.getRawSaturation());
                preparedUpdateStatement.setString(9, pData.getSyncStatus());
                preparedUpdateStatement.setString(10, String.valueOf(System.currentTimeMillis()));
                preparedUpdateStatement.setString(11, player.getUniqueId().toString());

                preparedUpdateStatement.executeUpdate();
                return true;
            } catch (SQLException e) {
                Inv.log.warning("Error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    if (preparedUpdateStatement != null) {
                        preparedUpdateStatement.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean setSyncStatus(Player player, String syncStatus) {
        PreparedStatement preparedUpdateStatement = null;
        Connection conn = inv.getDatabaseManager().getConnection();
        if (conn != null) {
            try {
                String data = "UPDATE `" + inv.getConfigHandler().getString("database.mysql.tableName") + "` " +
                        "SET `sync_complete` = ?" +
                        ", `last_seen` = ?" +
                        " WHERE `player_uuid` = ?";
                preparedUpdateStatement = conn.prepareStatement(data);
                preparedUpdateStatement.setString(1, syncStatus);
                preparedUpdateStatement.setString(2, String.valueOf(System.currentTimeMillis()));
                preparedUpdateStatement.setString(3, player.getUniqueId().toString());

                preparedUpdateStatement.executeUpdate();
                return true;
            } catch (SQLException e) {
                Inv.log.warning("Error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    if (preparedUpdateStatement != null) {
                        preparedUpdateStatement.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public DatabaseInventoryData getData(Player player) {
        if (!hasAccount(player)) {
            createAccount(player);
        }
        PreparedStatement preparedUpdateStatement = null;
        ResultSet result = null;
        Connection conn = inv.getDatabaseManager().getConnection();
        if (conn != null) {
            try {
                String sql = "SELECT * FROM `" + inv.getConfigHandler().getString("database.mysql.tableName") + "` WHERE `player_uuid` = ? LIMIT 1";
                preparedUpdateStatement = conn.prepareStatement(sql);
                preparedUpdateStatement.setString(1, player.getUniqueId().toString());

                result = preparedUpdateStatement.executeQuery();
                while (result.next()) {
                    return new DatabaseInventoryData(
                            result.getString("inventory"),
                            result.getString("armor"),
                            result.getString("ender_chest"),
                            result.getString("health"),
                            result.getString("experience"),
                            result.getString("food"),
                            result.getString("saturation"),
                            result.getString("sync_complete"),
                            result.getString("last_seen"));
                }
            } catch (SQLException e) {
                Inv.log.warning("Error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    if (result != null) {
                        result.close();
                    }
                    if (preparedUpdateStatement != null) {
                        preparedUpdateStatement.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
