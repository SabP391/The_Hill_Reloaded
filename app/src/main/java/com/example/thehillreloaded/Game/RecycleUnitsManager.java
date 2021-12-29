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

    public void unlockPlasticUnit(){unlockedUnits.add(new PlasticRecycleUnit(map, context));}

    public void unlockPaperUnit(){unlockedUnits.add(new PaperRecycleUnit(map, context));}

    public void unlockAluminiumUnit(){unlockedUnits.add(new AluminiumRecycleUnit(map, context));}

    public void unlockSteelUnit(){unlockedUnits.add(new SteelRecycleUnit(map, context));}

    public void unlockEWasteUnit(){unlockedUnits.add(new EWasteRecycleUnit(map, context));}

    public void unlockCompostUnit(){unlockedUnits.add(new CompostRecycleUnit(map, context));}

}
