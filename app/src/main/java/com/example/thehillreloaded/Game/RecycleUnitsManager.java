package com.example.thehillreloaded.Game;

import android.content.Context;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RecycleUnitsManager {

    // Costanti che determinano il prezzo in sunny points
    // delle diverse centrali ----------------------------------------------------------------------
    private static final int COST_OF_PAPER_UNIT = 12;
    private static final int COST_OF_COMPOST_UNIT = 20;
    private static final int COST_OF_ALUMINIUM_UNIT = 25;
    private static final int COST_OF_STEEL_UNIT = 30;
    private static final int COST_OF_PLASTIC_UNIT = 35;
    private static final int COST_OF_EWASTE_UNIT = 40;

    // Variabili che controllano quali centrali
    // siano state sbloccate -----------------------------------------------------------------------
    private boolean isPaperUnitUnlocked = false;
    private boolean isCompostUnlocked = false;
    private boolean isAluminiumUnitUnlocked = false;
    private boolean isSteelUnitUnlocked = false;
    private boolean isPlasticUnitUnlocked = false;
    private boolean isEwasteUnitUnlocked = false;
    private boolean isGlassUnitUnlocked = true;

    // Attributi della classe ----------------------------------------------------------------------
    private static RecycleUnitsManager instance;
    private Context context;
    private TileMap map;
    private ConcurrentLinkedQueue<RecycleUnit> unlockedUnits;

    // Metodi per l'inizializzazione della classe --------------------------------------------------
    private RecycleUnitsManager(){
        this.unlockedUnits = new ConcurrentLinkedQueue<RecycleUnit>();
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
        if(sunnyPoints >= 0){
            PlasticRecycleUnit unitToAdd = new PlasticRecycleUnit(map, context);
            unlockedUnits.add(unitToAdd);
            GameManager.getInstance().subtractSunnyPoints(COST_OF_PLASTIC_UNIT);
            isPlasticUnitUnlocked = true;
            return true;
        }
        return false;
    }

    public boolean unlockPaperUnit(){
        int sunnyPoints = GameManager.getInstance().getSunnyPoints() - COST_OF_PAPER_UNIT;
        if(sunnyPoints >= 0){
            PaperRecycleUnit unitToAdd = new PaperRecycleUnit(map, context);
            unlockedUnits.add(unitToAdd);
            GameManager.getInstance().subtractSunnyPoints(COST_OF_PAPER_UNIT);
            isPaperUnitUnlocked = true;
            return true;
        }
        return false;
    }

    public boolean unlockAluminiumUnit(){
        int sunnyPoints = GameManager.getInstance().getSunnyPoints() - COST_OF_ALUMINIUM_UNIT;
        if(sunnyPoints >= 0){
            AluminiumRecycleUnit unitToAdd = new AluminiumRecycleUnit(map, context);
            unlockedUnits.add(unitToAdd);
            GameManager.getInstance().subtractSunnyPoints(COST_OF_ALUMINIUM_UNIT);
            isAluminiumUnitUnlocked = true;
            return true;
        }
        return false;
    }

    public boolean unlockSteelUnit(){
        int sunnyPoints = GameManager.getInstance().getSunnyPoints() - COST_OF_STEEL_UNIT;
        if(sunnyPoints >= 0){
            SteelRecycleUnit unitToAdd = new SteelRecycleUnit(map, context);
            unlockedUnits.add(unitToAdd);
            GameManager.getInstance().subtractSunnyPoints(COST_OF_STEEL_UNIT);
            isSteelUnitUnlocked = true;
            return true;
        }
        return false;
    }

    public boolean unlockEWasteUnit(){
        int sunnyPoints = GameManager.getInstance().getSunnyPoints() - COST_OF_EWASTE_UNIT;
        if(sunnyPoints >= 0){
            EWasteRecycleUnit unitToAdd = new EWasteRecycleUnit(map, context);
            unlockedUnits.add(unitToAdd);
            GameManager.getInstance().subtractSunnyPoints(COST_OF_EWASTE_UNIT);
            isEwasteUnitUnlocked = true;
            return true;
        }
        return false;
    }

    public boolean unlockCompostUnit(){
        int sunnyPoints = GameManager.getInstance().getSunnyPoints() - COST_OF_COMPOST_UNIT;
        if(sunnyPoints >= 0){
            CompostRecycleUnit unitToAdd = new CompostRecycleUnit(map, context);
            unlockedUnits.add(unitToAdd);
            GameManager.getInstance().subtractSunnyPoints(COST_OF_COMPOST_UNIT);
            isCompostUnlocked = true;
            return true;
        }
        return false;
    }

    // Getter e setter -----------------------------------------------------------------------------

    public ConcurrentLinkedQueue<RecycleUnit> getUnlockedUnits(){ return unlockedUnits; }

    public static int getCostOfCompostUnit() { return COST_OF_COMPOST_UNIT; }

    public static int getCostOfAluminiumUnit() { return COST_OF_ALUMINIUM_UNIT; }

    public static int getCostOfSteelUnit() { return COST_OF_STEEL_UNIT; }

    public static int getCostOfPlasticUnit() { return COST_OF_PLASTIC_UNIT; }

    public static int getCostOfEwasteUnit() { return COST_OF_EWASTE_UNIT; }

    public static int getCostOfPaperUnit() { return COST_OF_PAPER_UNIT; }

    public boolean isPaperUnitUnlocked() { return isPaperUnitUnlocked; }

    public boolean isCompostUnlocked() { return isCompostUnlocked; }

    public boolean isAluminiumUnitUnlocked() { return isAluminiumUnitUnlocked; }

    public boolean isSteelUnitUnlocked() { return isSteelUnitUnlocked; }

    public boolean isPlasticUnitUnlocked() { return isPlasticUnitUnlocked; }

    public boolean isEwasteUnitUnlocked() { return isEwasteUnitUnlocked; }

    public boolean isGlassUnitUnlocked() { return isGlassUnitUnlocked; }

    public CompostRecycleUnit getCompostUnit(){
        for(RecycleUnit i : unlockedUnits){
            if(i instanceof CompostRecycleUnit){
                return (CompostRecycleUnit) i;
            }
        }
        return null;
    }

    public AluminiumRecycleUnit getAluminiumUnit(){
        for(RecycleUnit i : unlockedUnits){
            if(i instanceof AluminiumRecycleUnit){
                return (AluminiumRecycleUnit) i;
            }
        }
        return null;
    }

    public SteelRecycleUnit getSteelUnit(){
        for(RecycleUnit i : unlockedUnits){
            if(i instanceof SteelRecycleUnit){
                return (SteelRecycleUnit) i;
            }
        }
        return null;
    }

    public PlasticRecycleUnit getPlasticUnit(){
        for(RecycleUnit i : unlockedUnits){
            if(i instanceof PlasticRecycleUnit){
                return (PlasticRecycleUnit) i;
            }
        }
        return null;
    }

    public EWasteRecycleUnit getEWasteUnit(){
        for(RecycleUnit i : unlockedUnits){
            if(i instanceof EWasteRecycleUnit){
                return (EWasteRecycleUnit) i;
            }
        }
        return null;
    }

    public GlassRecycleUnit getGlassUnit(){
        for(RecycleUnit i : unlockedUnits){
            if(i instanceof GlassRecycleUnit){
                return (GlassRecycleUnit) i;
            }
        }
        return null;
    }

    public IncineratorUnit getIncineratorUnit(){
        for(RecycleUnit i : unlockedUnits){
            if(i instanceof IncineratorUnit){
                return (IncineratorUnit) i;
            }
        }
        return null;
    }
}
