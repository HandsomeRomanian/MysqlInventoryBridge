package ca.matai.crossinvent.inventory;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import ca.matai.crossinvent.inventory.objects.DatabaseInventoryData;
import ca.matai.crossinvent.inventory.objects.InventorySyncData;
import ca.matai.crossinvent.inventory.objects.InventorySyncTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryDataHandler {

    private Inv pd;
    private Set<Player> playersInSync = new HashSet<Player>();
    private Set<Player> playersDisconnectSave = new HashSet<Player>();

    public InventoryDataHandler(Inv pd) {
        this.pd = pd;
    }

    public boolean isSyncComplete(Player p) {
        return playersInSync.contains(p);
    }

    private void dataCleanup(Player p) {
        playersInSync.remove(p);
        playersDisconnectSave.remove(p);
    }

    public void setPlayerData(final Player p, DatabaseInventoryData data, InventorySyncData syncData, boolean cancelTask) {
        if (!playersInSync.contains(p)) {
            //Inventory and Armor sync for lower mc versions then 1.9
            if (!Inv.is19Server) {
                setInventory(p, data, syncData);
                if (pd.getConfigHandler().getBoolean("General.syncArmorEnabled")) {
                    setArmor(p, data, syncData);
                }
            } else {
                //Inventory and Armor sync for 1.9 and up.
                //Inventory and Armor sync for 1.9 and up.
                if (pd.getConfigHandler().getBoolean("General.syncArmorEnabled")) {
                    setInventory(p, data, syncData);
                } else if (!pd.getConfigHandler().getBoolean("General.syncArmorEnabled")) {
                    //TODO fix
                    setInventoryNew(p, data, syncData);
                }
                if (pd.getConfigHandler().getBoolean("General.syncArmorEnabled")) {
                    try {//todo
                        p.getInventory().setArmorContents(decodeItems(data.getRawArmor()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (pd.getConfigHandler().getBoolean("General.syncEnderChestEnabled")) {
                    try {
                        p.getEnderChest().setContents(decodeItems(data.getRawEnderChest()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (pd.getConfigHandler().getBoolean("General.syncHealthEnabled")) {
                    try {
                        p.setHealth(Double.parseDouble(data.getRawHealth()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (pd.getConfigHandler().getBoolean("General.syncExperienceEnabled")) {
                    try {
                        System.out.println("Setting xp to : " + data.getRawEXP());
                        p.setTotalExperience(Integer.parseInt(data.getRawEXP()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (pd.getConfigHandler().getBoolean("General.syncFoodEnabled")) {
                    try {
                        p.setFoodLevel(Integer.parseInt(data.getRawFood()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (pd.getConfigHandler().getBoolean("General.syncSaturationEnabled")) {
                    try {
                        p.setSaturation(Float.parseFloat(data.getRawSaturation()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            pd.getInvMysqlInterface().setSyncStatus(p, "false");
            p.updateInventory();
            Bukkit.getScheduler().runTaskLaterAsynchronously(pd, new Runnable() {

                @Override
                public void run() {
                    playersInSync.add(p);
                }

            }, 2L);
            data = null;
        }
    }


    public void onDataSaveFunction(Player p, Boolean datacleanup, String syncStatus, ItemStack[] inventoryDisconnect, ItemStack[] armorDisconnect, ItemStack[] echestDisconnect) {
        if (playersDisconnectSave.contains(p)) {
            if (pd.getConfigHandler().getBoolean("Debug.InventorySync")) {
                Inv.log.info("Inventory Debug - Save Data - Canceled - " + p.getName());
            }
            return;
        }
        if (datacleanup) {
            playersDisconnectSave.add(p);
        }
        boolean isPlayerInSync = playersInSync.contains(p);
        if (isPlayerInSync) {
            String inv = "none";
            String armor = "none";
            String echest = "none";
            String hp = "none";
            String xp = "none";
            String food = "none";
            String sat = "none";
            if (pd.getConfigHandler().getBoolean("Debug.InventorySync")) {
                Inv.log.info("Inventory Debug - Save Data - Start - " + p.getName());
            }
            try {
                if (inventoryDisconnect != null) {
                    if (pd.getConfigHandler().getBoolean("Debug.InventorySync")) {
                        Inv.log.info("Inventory Debug - Set Data - Saving disconnect inventory - " + p.getName());
                    }
                    inv = encodeItems(inventoryDisconnect);
                } else {
                    if (pd.getConfigHandler().getBoolean("Debug.InventorySync")) {
                        Inv.log.info("Inventory Debug - Set Data - Saving inventory - " + p.getName());
                    }
                    inv = encodeItems(p.getInventory().getContents());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (pd.getConfigHandler().getBoolean("General.syncArmorEnabled")) {
                try {
                    armor = encodeItems(p.getInventory().getArmorContents());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (pd.getConfigHandler().getBoolean("General.syncEnderChestEnabled")) {
                try {
                    echest = encodeItems(p.getEnderChest().getContents());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (pd.getConfigHandler().getBoolean("General.syncHealthEnabled")) {
                try {
                    hp = String.valueOf(p.getHealth());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (pd.getConfigHandler().getBoolean("General.syncExperienceEnabled")) {
                try {
                    System.out.println("getTotalExperience : " + p.getTotalExperience());
                    System.out.println("getExp : " + p.getExp());
                    System.out.println("getExpToLevel : " + p.getExpToLevel());
                    xp = String.valueOf(p.getTotalExperience());
                    System.out.println("Saving total XP: " + xp);

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            if (pd.getConfigHandler().getBoolean("General.syncFoodEnabled")) {
                try {
                    food = String.valueOf(p.getFoodLevel());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (pd.getConfigHandler().getBoolean("General.syncSaturationEnabled")) {
                try {
                    sat = String.valueOf(p.getSaturation());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            DatabaseInventoryData data = null;
            try {
                data = new DatabaseInventoryData(inv, armor, echest, hp, xp, food, sat, syncStatus, null);
            } catch (Exception e) {
                Inv.log.log(Level.SEVERE, "MAT SUCKS AT CODING");
            }
            pd.getInvMysqlInterface().setData(p, data);
        }
        if (datacleanup) {
            dataCleanup(p);
        }
    }

    public void onJoinFunction(final Player p) {
        if (!Inv.isDisabling && !playersInSync.contains(p)) {
            if (pd.getInvMysqlInterface().hasAccount(p)) {
                final InventorySyncData syncData = new InventorySyncData();
                backupAndReset(p, syncData);
                DatabaseInventoryData data = pd.getInvMysqlInterface().getData(p);
                if (data.getSyncStatus().matches("true")) {
                    setPlayerData(p, data, syncData, false);
                } else {
                    new InventorySyncTask(pd, System.currentTimeMillis(), p, syncData).runTaskTimerAsynchronously(pd, 10L, 10L);
                }
            } else {
                playersInSync.add(p);
                onDataSaveFunction(p, false, "false", null, null, null);
            }
        }

    }


    /**
     * @param p
     * @param syncData
     */
    private void backupAndReset(Player p, InventorySyncData syncData) {

        syncData.setBackupInventory(p.getInventory().getContents());
        syncData.setBackupArmor(p.getInventory().getArmorContents());
        syncData.setBackupEnderChest(p.getEnderChest().getContents());
        syncData.setBackupExperience(p.getTotalExperience());
        syncData.setBackupHealth(p.getHealth());
        syncData.setBackupFood(p.getFoodLevel());
        syncData.setBackupSaturation(p.getSaturation());
        p.setItemOnCursor(null);
        p.getInventory().clear();
        p.updateInventory();
        if (pd.getConfigHandler().getBoolean("General.syncArmorEnabled")) {
            syncData.setBackupArmor(p.getInventory().getArmorContents());
            p.getInventory().setHelmet(null);
            p.getInventory().setChestplate(null);
            p.getInventory().setLeggings(null);
            p.getInventory().setBoots(null);
            p.updateInventory();
        }
    }

    public ItemStack[] getInventory(Player p) {
        return p.getInventory().getContents();
    }

    public ItemStack[] getArmor(Player p) {
        if (pd.getConfigHandler().getBoolean("General.syncArmorEnabled")) {
            return p.getInventory().getArmorContents();
        } else {
            return null;
        }
    }

    private void setInventory(final Player p, DatabaseInventoryData data, InventorySyncData syncData) {
        if (pd.getConfigHandler().getBoolean("Debug.InventorySync")) {
            Inv.log.info("Inventory Debug - Set Data - Start- " + p.getName());
        }
        if (!data.getRawInventory().matches("none")) {
            if (pd.getConfigHandler().getBoolean("Debug.InventorySync")) {
                Inv.log.info("Inventory Debug - Set Data - Loading inventory - " + p.getName());
            }
            try {
                p.getInventory().setContents(decodeItems(data.getRawInventory()));
                if (pd.getConfigHandler().getBoolean("Debug.InventorySync")) {
                    Inv.log.info("Inventory Debug - Set Data - Inventory set - " + p.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (syncData.getBackupInventory() != null) {
                    if (pd.getConfigHandler().getBoolean("Debug.InventorySync")) {
                        Inv.log.info("Inventory Debug - Set Data - Loading backup inventory - " + p.getName());
                    }
                    p.getInventory().setContents(syncData.getBackupInventory());
                    p.sendMessage(pd.getConfigHandler().getStringWithColor("ChatMessage.inventorySyncError"));
                    pd.getSoundHandler().sendPlingSound(p);
                    p.sendMessage(pd.getConfigHandler().getStringWithColor("ChatMessage.inventorySyncBackup"));
                } else {
                    if (pd.getConfigHandler().getBoolean("Debug.InventorySync")) {
                        Inv.log.info("Inventory Debug - Set Data - No backup inventory found! - " + p.getName());
                    }
                }
            }
        } else {
            if (pd.getConfigHandler().getBoolean("Debug.InventorySync")) {
                Inv.log.info("Inventory Debug - Set Data - Restoring local inventory - " + p.getName());
            }
            p.getInventory().setContents(syncData.getBackupInventory());
        }
        p.updateInventory();
    }

    private void setInventoryNew(final Player p, DatabaseInventoryData data, InventorySyncData syncData) {
        if (pd.getConfigHandler().getBoolean("Debug.InventorySync")) {
            Inv.log.info("Inventory Debug - Set Data - Start- " + p.getName());
        }
        if (!data.getRawInventory().matches("none")) {
            if (pd.getConfigHandler().getBoolean("Debug.InventorySync")) {
                Inv.log.info("Inventory Debug - Set Data - Loading inventory - " + p.getName());
            }
            try {
                p.getInventory().setContents(decodeItems(data.getRawInventory()));
                p.getInventory().setArmorContents(syncData.getBackupArmor());
                if (pd.getConfigHandler().getBoolean("Debug.InventorySync")) {
                    Inv.log.info("Inventory Debug - Set Data - Inventory set - " + p.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (syncData.getBackupInventory() != null) {
                    if (pd.getConfigHandler().getBoolean("Debug.InventorySync")) {
                        Inv.log.info("Inventory Debug - Set Data - Loading backup inventory - " + p.getName());
                    }
                    p.getInventory().setContents(syncData.getBackupInventory());
                    p.sendMessage(pd.getConfigHandler().getStringWithColor("ChatMessage.inventorySyncError"));
                    pd.getSoundHandler().sendPlingSound(p);
                    p.sendMessage(pd.getConfigHandler().getStringWithColor("ChatMessage.inventorySyncBackup"));
                } else {
                    if (pd.getConfigHandler().getBoolean("Debug.InventorySync")) {
                        Inv.log.info("Inventory Debug - Set Data - No backup inventory found! - " + p.getName());
                    }
                }
            }
        } else {
            if (pd.getConfigHandler().getBoolean("Debug.InventorySync")) {
                Inv.log.info("Inventory Debug - Set Data - Restoring local inventory - " + p.getName());
            }
            p.getInventory().setContents(syncData.getBackupInventory());
        }
        p.updateInventory();
    }

    private void setArmor(final Player p, DatabaseInventoryData data, InventorySyncData syncData) {
        if (!data.getRawArmor().matches("none")) {
            try {
                p.getInventory().setArmorContents(decodeItems(data.getRawArmor()));
            } catch (Exception e) {
                e.printStackTrace();
                p.getInventory().setArmorContents(syncData.getBackupArmor());
                p.sendMessage(pd.getConfigHandler().getStringWithColor("ChatMessage.armorSyncError"));
                pd.getSoundHandler().sendPlingSound(p);
                p.sendMessage(pd.getConfigHandler().getStringWithColor("ChatMessage.armorSyncBackup"));
            }
        } else {
            p.getInventory().setArmorContents(syncData.getBackupArmor());
        }
        p.updateInventory();
    }

    public String encodeItems(ItemStack[] items) {
        if (pd.useProtocolLib && pd.getConfigHandler().getBoolean("General.enableModdedItemsSupport")) {
            return InventoryUtils.saveModdedStacksData(items);
        } else {
            return InventoryUtils.itemStackArrayToBase64(items);
        }
    }

    public ItemStack[] decodeItems(String data) throws Exception {
        if (pd.useProtocolLib && pd.getConfigHandler().getBoolean("General.enableModdedItemsSupport")) {
            ItemStack[] it = InventoryUtils.restoreModdedStacks(data);
            if (it == null) {
                it = InventoryUtils.itemStackArrayFromBase64(data);
            }
            return it;
        } else {
            return InventoryUtils.itemStackArrayFromBase64(data);
        }
    }

}
