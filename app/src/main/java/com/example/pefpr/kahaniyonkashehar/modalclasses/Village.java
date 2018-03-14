package com.example.pefpr.kahaniyonkashehar.modalclasses;

/**
 * Created by Administrator on 8/31/2015.
 */
public class Village {
    public int VillageID;
    public String VillageCode;
    public String VillageName;
    public String Block;
    public String District;
    public String State;
    public String CRLID;

    public int getVillageID() {
        return VillageID;
    }

    public void setVillageID(int villageID) {
        VillageID = villageID;
    }

    public String getVillageCode() {
        return VillageCode;
    }

    public void setVillageCode(String villageCode) {
        VillageCode = villageCode;
    }

    public String getVillageName() {
        return VillageName;
    }

    public void setVillageName(String villageName) {
        VillageName = villageName;
    }

    public String getBlock() {
        return Block;
    }

    public void setBlock(String block) {
        Block = block;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCRLID() {
        return CRLID;
    }

    public void setCRLID(String CRLID) {
        this.CRLID = CRLID;
    }
}