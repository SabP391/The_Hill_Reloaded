package com.example.thehillreloaded.Game;

import android.content.Context;

import java.util.LinkedList;

public class RecycleUnitsManager {
    private static RecycleUnitsManager instance;
    private Context context;
    private TileMap map;
    private LinkedList<RecycleUnit> unlockedUnits;

    private RecycleUnitsManager(){
        this.unlockedUnits = new LinkedList<RecycleUnit>();
    }

    public static RecycleUnitsManager getInstance(){
        if(instance == null){
            instance = new RecycleUnitsManager();
        }
        return instance;
    }

    public void initInstance(Context context, TileMap map){
        this.context = context;
        this.map = map;
        unlockedUnits.add(new GlassRecycleUnit(map, context));
        unlockedUnits.add(new IncineratorUnit(map, context));
    }

    public LinkedList<RecycleUnit> getUnlockedUnits(){
        return unlockedUnits;
    }


    // Metodi per sbloccare le centrali-------------------------------------------------------------
    public void unlockPlasticUnit(){
        PlasticRecycleUnit unitToAdd = new PlasticRecycleUnit(map, context);
        unlockedUnits.add(unitToAdd);
        GameManager.getInstance().setSunnyPoints(GameManager.getInstance().getSunnyPoints() - unitToAdd.getCost());
    }

    public void unlockPaperUnit(){
        PaperRecycleUnit unitToAdd = new PaperRecycleUnit(map, context);
        unlockedUnits.add(unitToAdd);
        GameManager.getInstance().setSunnyPoints(GameManager.getInstance().getSunnyPoints() - unitToAdd.getCost());
    }

    public void unlockAluminiumUnit(){
        AluminiumRecycleUnit unitToAdd = new AluminiumRecycleUnit(map, context);
        unlockedUnits.add(unitToAdd);
        GameManager.getInstance().setSunnyPoints(GameManager.getInstance().getSunnyPoints() - unitToAdd.getCost());
    }

    public void unlockSteelUnit(){
        SteelRecycleUnit unitToAdd = new SteelRecycleUnit(map, context);
        unlockedUnits.add(unitToAdd);
        GameManager.getInstance().setSunnyPoints(GameManager.getInstance().getSunnyPoints() - unitToAdd.getCost());
    }

    public void unlockEWasteUnit(){
        EWasteRecycleUnit unitToAdd = new EWasteRecycleUnit(map, context);
        unlockedUnits.add(unitToAdd);
        GameManager.getInstance().setSunnyPoints(GameManager.getInstance().getSunnyPoints() - unitToAdd.getCost());
    }

    public void unlockCompostUnit(){
        CompostRecycleUnit unitToAdd = new CompostRecycleUnit(map, context);
        unlockedUnits.add(unitToAdd);
        GameManager.getInstance().setSunnyPoints(GameManager.getInstance().getSunnyPoints() - unitToAdd.getCost());
    }

}
