package com.example.pefpr.kahaniyonkashehar.modalclasses;

/**
 * Created by PEF-2 on 27/10/2015.
 */
public class VillageList {
    public int villageId;
    public String villageName;

    public VillageList(int id, String name) {
        villageId = id;
        villageName = name;
    }

    public VillageList(int id) {
        villageId = id;
    }


    @Override
    public String toString() {
        return this.villageName;
    }

    public int getVillageId() {
        return villageId;
    }

    public String getVillageName() {
        return villageName;
    }
}
