package com.example.thehillreloaded.Model;

import com.example.thehillreloaded.Game.ItemType;
import com.example.thehillreloaded.Game.RecycleUnitStatus;

public class RecycleUnitSave {

    protected RecycleUnitStatus unitStatus;
    protected ItemType acceptedItemType;
    protected int currentWearLevel;
    protected int unitPoints;

    public RecycleUnitSave(RecycleUnitStatus unitStatus, ItemType acceptedItemType, int currentWearLevel, int unitPoints) {
        this.unitStatus = unitStatus;
        this.acceptedItemType = acceptedItemType;
        this.currentWearLevel = currentWearLevel;
        this.unitPoints = unitPoints;
    }

    //accesso
    public RecycleUnitStatus getUnitStatus() { return unitStatus;}
    public ItemType getAcceptedItemType() {return acceptedItemType;}
    public int getCurrentWearLevel() {return currentWearLevel;}
    public int getUnitPoints() {return unitPoints;}
}
