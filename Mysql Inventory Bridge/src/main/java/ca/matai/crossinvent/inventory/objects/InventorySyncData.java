package ca.matai.crossinvent.inventory.objects;

import org.bukkit.inventory.ItemStack;

public class InventorySyncData {

    private ItemStack[] backupInv;
    private ItemStack[] backupAr;
    private ItemStack[] backupEChest;
    private double backupHP;
    private int backupEXP;
    private int backupFood;
    private float backupSaturation;
    private Boolean syncComplete;

    public InventorySyncData() {
        this.backupEChest = null;
        this.backupHP = 20;
        this.backupEXP = 0;
        this.backupFood = 20;
        this.backupSaturation = 0;
        this.backupInv = null;
        this.backupAr = null;
        this.syncComplete = false;
    }

    public void setSyncStatus(boolean syncStatus) {
        syncComplete = syncStatus;
    }

    public Boolean getSyncStatus() {
        return syncComplete;
    }

    public ItemStack[] getBackupInventory() {
        return backupInv;
    }

    public ItemStack[] getBackupArmor() {
        return backupAr;
    }

    public ItemStack[] getBackupEnderChest() {
        return backupEChest;
    }

    public double getBackupHealth() {
        return backupHP;
    }

    public int getBackupExperience() {
        return backupEXP;
    }

    public int getBackupFood() {
        return backupFood;
    }

    public float getBackupSatiration() {
        return backupSaturation;
    }


    public void setBackupInventory(ItemStack[] _backupInventory) {
        backupInv = _backupInventory;
    }

    public void setBackupArmor(ItemStack[] _backupArmor) {
        backupAr = _backupArmor;
    }

    public void setBackupEnderChest(ItemStack[] _backupEChest) {
        backupEChest = _backupEChest;
    }

    public void setBackupHealth(double _backupHP) {
        backupHP = _backupHP;
    }

    public void setBackupExperience(int _backupEXP) {
        backupEXP = _backupEXP;
    }

    public void setBackupFood(int _backupFood) {
        backupFood = _backupFood;
    }

    public void setBackupSaturation(float _backupSaturation) {
        backupSaturation = _backupSaturation;
    }

}
