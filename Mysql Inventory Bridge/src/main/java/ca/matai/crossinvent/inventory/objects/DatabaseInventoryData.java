package ca.matai.crossinvent.inventory.objects;

import ca.matai.crossinvent.inventory.Inv;

import java.util.logging.Level;

public class DatabaseInventoryData {

    private String rawInv;
    private String rawAr;
    private String rawEChest;
    private String rawHP;
    private String rawEXP;
    private String rawFood;
    private String rawSaturation;
    private String syncComplete;
    private String lastSee;

    public DatabaseInventoryData(String rawInventory, String rawArmor, String rawEChest, String rawHP,
                                 String rawEXP, String rawFood, String rawSaturation, String syncStatus, String lastSeen) {
        this.rawInv = rawInventory;
        this.rawAr = rawArmor;
        if (!rawEChest.equals("none"))
            this.rawEChest = rawEChest;
        if (!rawHP.equals("none"))
            this.rawHP = rawHP;
        if (!rawEXP.equals("none"))
            this.rawEXP = rawEXP;
        if (!rawFood.equals("none"))
            this.rawFood = rawFood;
        if (!rawSaturation.equals("none"))
            this.rawSaturation = rawSaturation;

        try {
            if(rawHP.length() > 5)
                throw new Exception();
        }catch (Exception e){
            System.out.println(rawHP.length());
        }
        this.syncComplete = syncStatus;
        this.lastSee = lastSeen;
    }

    public String getLastSeen() {
        return lastSee;
    }

    public String getSyncStatus() {
        return syncComplete;
    }

    public String getRawArmor() {
        return rawAr;
    }

    public String getRawInventory() {
        return rawInv;
    }

    public String getRawFood() {
        return rawFood;
    }

    public String getRawSaturation() {
        return rawSaturation;
    }

    public String getRawEnderChest() {
        return rawEChest;
    }

    public String getRawHealth() {
        return rawHP;
    }

    public String getRawEXP() {
        return rawEXP;
    }


}
