package com.example.thehillreloaded.Game;

import android.content.Context;

import java.util.LinkedList;

public class RecycleUnitsManager {

    // Costanti che determinano il prezzo in sunny points
    // delle diverse centrali ----------------------------------------------------------------------
    private static final int COST_OF_PAPER_UNIT = 12;
    private static final int COST_OF_COMPOST_UNIT = 20;
    private static final int COST_OF_ALUMINIUM_UNIT = 25;

    public static int getCostOfPaperUnit() {
        return COST_OF_PAPER_UNIT;
    }

    private static final int COST_OF_STEEL_UNIT = 30;
    private static final int COST_OF_PLASTIC_UNIT = 35;
    private static final int COST_OF_EWASTE_UNIT = 40;

    // Attributi della classe ----------------------------------------------------------------------
    private static RecycleUnitsManager instance;
    private Context context;
    private TileMap map;
    private LinkedList<RecycleUnit> unlockedUnits;

    // Metodi per l'inizializzazione della classe --------------------------------------------------
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

    // Metodi per sbloccare le centrali-------------------------------------------------------------
    public boolean unlockPlasticUnit(){
        int sunnyPoints = GameManager.getInstance().getSunnyPoints() - COST_OF_PLASTIC_UNIT;
        if(sunnyPoints > 0){
            PlasticRecycleUnit unitToAdd = new PlasticRecycleUnit(map, context);
            unlockedUnits.add(unitToAdd);
            GameManager.getInstance().setSunnyPoints(sunnyPoints);
            return true;
        }
        return false;
    }

    public boolean unlockPaperUnit(){
        int sunnyPoints = GameManager.getInstance().getSunnyPoints() - COST_OF_PAPER_UNIT;
        if(sunnyPoints > 0){
            PaperRecycleUnit unitToAdd = new PaperRecycleUnit(map, context);
            unlockedUnits.add(unitToAdd);
            GameManager.getInstance().setSunnyPoints(sunnyPoints);
            return true;
        }
        return false;
    }

    public boolean unlockAluminiumUnit(){
        int sunnyPoints = GameManager.getInstance().getSunnyPoints() - COST_OF_ALUMINIUM_UNIT;
        if(sunnyPoints > 0){
            AluminiumRecycleUnit unitToAdd = new AluminiumRecycleUnit(map, context);
            unlockedUnits.add(unitToAdd);
            GameManager.getInstance().setSunnyPoints(sunnyPoints);
            return true;
        }
        return false;
    }

    public boolean unlockSteelUnit(){
        int sunnyPoints = GameManager.getInstance().getSunnyPoints() - COST_OF_STEEL_UNIT;
        if(sunnyPoints > 0){
            SteelRecycleUnit unitToAdd = new SteelRecycleUnit(map, context);
            unlockedUnits.add(unitToAdd);
            GameManager.getInstance().setSunnyPoints(sunnyPoints);
            return true;
        }
        return false;
    }

    public boolean unlockEWasteUnit(){
        int sunnyPoints = GameManager.getInstance().getSunnyPoints() - COST_OF_EWASTE_UNIT;
        if(sunnyPoints > 0){
            EWasteRecycleUnit unitToAdd = new EWasteRecycleUnit(map, context);
            unlockedUnits.add(unitToAdd);
            GameManager.getInstance().setSunnyPoints(sunnyPoints);
            return true;
        }
        return false;
    }

    public boolean unlockCompostUnit(){
        int sunnyPoints = GameManager.getInstance().getSunnyPoints() - COST_OF_COMPOST_UNIT;
        if(sunnyPoints > 0){
            CompostRecycleUnit unitToAdd = new CompostRecycleUnit(map, context);
            unlockedUnits.add(unitToAdd);
            GameManager.getInstance().setSunnyPoints(sunnyPoints);
            return true;
        }
        return false;
    }

    // Getter e setter -----------------------------------------------------------------------------
    public LinkedList<RecycleUnit> getUnlockedUnits(){
        return unlockedUnits;
    }

    public static int getCostOfCompostUnit() {
        return COST_OF_COMPOST_UNIT;
    }

    public static int getCostOfAluminiumUnit() {
        return COST_OF_ALUMINIUM_UNIT;
    }

    public static int getCostOfSteelUnit() {
        return COST_OF_STEEL_UNIT;
    }

    public static int getCostOfPlasticUnit() {
        return COST_OF_PLASTIC_UNIT;
    }

    public static int getCostOfEwasteUnit() {
        return COST_OF_EWASTE_UNIT;
    }
}
