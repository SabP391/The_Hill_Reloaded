package com.example.thehillreloaded.Game;

import android.content.Context;
import android.util.Log;

import com.example.thehillreloaded.Model.RecycleUnitSave;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RecycleUnitsManager {
    private UnlockableObject unlockable[];
    //variabili di controllo sullo sblocco degli oggetti
    private boolean plasticObject[];
    private boolean glassObject[];
    private boolean paperObject[];
    private boolean aluminiumObject[];
    private boolean steelObject[];
    private boolean ewasteObject[];
    private boolean compostObject[];

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
    private SoundFx sFX;
    private TileMap map;
    private ConcurrentLinkedQueue<RecycleUnit> unlockedUnits;

    private int quest3Counter = 0;

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

    public void destroy(){
        instance = null;
    }

    public void initInstance(Context context, TileMap map){
        this.context = context;
        this.map = map;
        sFX = (RecycleUnitsManager.SoundFx) context;
        unlockedUnits.add(new GlassRecycleUnit(map, context));
        unlockedUnits.add(new IncineratorUnit(map, context));
        initUnlockableObjects();
    }

    private void initUnlockableObjects() {
        this.unlockable = new UnlockableObject[4];
        unlockable[0] = new UnlockableObject(2, 1);
        unlockable[1] = new UnlockableObject(4, 3);
        unlockable[2] = new UnlockableObject(7, 6);
        unlockable[3] = new UnlockableObject(12, 11);

        this.plasticObject = new boolean[4];
        for (int x=0; x<4; x++){
            plasticObject[x] = false;
        }
        this.glassObject = new boolean[4];
        for (int x=0; x<4; x++){
            glassObject[x] = false;
        }
        this.paperObject = new boolean[4];
        for (int x=0; x<4; x++){
            paperObject[x] = false;
        }
        this.ewasteObject = new boolean[4];
        for (int x=0; x<4; x++){
            ewasteObject[x] = false;
        }
        this.steelObject = new boolean[4];
        for (int x=0; x<4; x++){
            steelObject[x] = false;
        }
        this.aluminiumObject = new boolean[4];
        for (int x=0; x<4; x++){
            aluminiumObject[x] = false;
        }
        this.compostObject = new boolean[4];
        for (int x=0; x<4; x++){
            compostObject[x] = false;
        }
    }


    public void recycleUnitsManagerReload(boolean isPaperUnitUnlocked, boolean isCompostUnlocked, boolean isAluminiumUnitUnlocked,
                                          boolean isSteelUnitUnlocked, boolean isPlasticUnitUnlocked, boolean isEwasteUnitUnlocked,
                                          boolean isGlassUnitUnlocked, Context context, TileMap map, List<RecycleUnitSave> listaRu){
        this.isPaperUnitUnlocked = isPaperUnitUnlocked;
        this.isCompostUnlocked = isCompostUnlocked;
        this.isAluminiumUnitUnlocked = isAluminiumUnitUnlocked;
        this.isSteelUnitUnlocked = isSteelUnitUnlocked;
        this.isPlasticUnitUnlocked = isPlasticUnitUnlocked;
        this.isEwasteUnitUnlocked = isEwasteUnitUnlocked;
        this.isGlassUnitUnlocked = isGlassUnitUnlocked;
        this.context = context;
        this.map = map;
        sFX = (RecycleUnitsManager.SoundFx) context;
        initUnlockableObjects();
        for(RecycleUnitSave savedUnit : listaRu){
            if(savedUnit.getAcceptedItemType() != null){
                Log.d("itemType", String.valueOf(savedUnit.getAcceptedItemType()));
            }
        }

        for(RecycleUnitSave savedUnit : listaRu){
            RecycleUnit unitToAdd;
            switch (savedUnit.getAcceptedItemType()){
                case ALUMINIUM:
                    unitToAdd = new AluminiumRecycleUnit(map,context);
                    break;
                case EWASTE:
                    unitToAdd = new EWasteRecycleUnit(map,context);
                    break;
                case GLASS:
                    unitToAdd = new GlassRecycleUnit(map,context);
                    break;
                case PAPER:
                    unitToAdd = new PaperRecycleUnit(map,context);
                    break;
                case PLASTIC:
                    unitToAdd = new PlasticRecycleUnit(map,context);
                    break;
                case STEEL:
                    unitToAdd = new SteelRecycleUnit(map,context);
                    break;
                case COMPOST:
                    unitToAdd = new CompostRecycleUnit(map,context);
                    break;
                case ALL:
                    unitToAdd = new IncineratorUnit(map,context);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + savedUnit.getAcceptedItemType());
            }
            unitToAdd.setUnitStatus(savedUnit.getUnitStatus());
            unitToAdd.setUnitPoints(savedUnit.getUnitPoints());
            unitToAdd.setCurrentWearLevel(savedUnit.getCurrentWearLevel());
            unlockedUnits.add(unitToAdd);
        }

    }



    public void initInstanceMultiplayer(Context context, TileMap map){
        initInstance(context, map);
        unlockedUnits.add(new PaperRecycleUnit(map, context));
        unlockedUnits.add(new PlasticRecycleUnit(map, context));
        unlockedUnits.add(new CompostRecycleUnit(map, context));
        unlockedUnits.add(new SteelRecycleUnit(map, context));
        unlockedUnits.add(new EWasteRecycleUnit(map, context));
        unlockedUnits.add(new AluminiumRecycleUnit(map, context));
    }



    public boolean processItemOnScreen(GameItem item){
        int tileToCheck = map.getTileIndexFromPosition(item.getPosition());
        for(RecycleUnit i : unlockedUnits){
            if(i.isOneOfMyTiles(tileToCheck)){
                if(i.processItem(item)){
                    sFX.suonoUnità();
                    return true;
                }
            }
        }
        return false;
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

    // Metodi per gli sbloccabili ------------------------------------------------------------------

    public boolean unlockPlasticObject(int index){
        PlasticRecycleUnit unit = getPlasticUnit();
        if (unlockable[index].getUpCost() <= unit.getUnitPoints()) {
            unit.setUnitPoints(unit.getUnitPoints()-unlockable[index].getUpCost());
            GameManager.getInstance().addSunnyPoint(unlockable[index].getSpReward());
            return true;
        }
        return false;
    }

    public boolean unlockPaperObject(int index){
        PaperRecycleUnit unit = getPaperUnit();
        if (unlockable[index].getUpCost() <= unit.getUnitPoints()) {
            unit.setUnitPoints(unit.getUnitPoints()-unlockable[index].getUpCost());
            GameManager.getInstance().addSunnyPoint(unlockable[index].getSpReward());
            return true;
        }
        return false;
    }

    public boolean unlockGlassObject(int index){
        GlassRecycleUnit unit = getGlassUnit();
        if (unlockable[index].getUpCost() <= unit.getUnitPoints()) {
            unit.setUnitPoints(unit.getUnitPoints()-unlockable[index].getUpCost());
            GameManager.getInstance().addSunnyPoint(unlockable[index].getSpReward());
            return true;
        }
        return false;
    }

    public boolean unlockAluminiumObject(int index){
        AluminiumRecycleUnit unit = getAluminiumUnit();
        if (unlockable[index].getUpCost() <= unit.getUnitPoints()) {
            unit.setUnitPoints(unit.getUnitPoints()-unlockable[index].getUpCost());
            GameManager.getInstance().addSunnyPoint(unlockable[index].getSpReward());
            return true;
        }
        return false;
    }

    public boolean unlockSteelObject(int index){
        SteelRecycleUnit unit = getSteelUnit();
        if (unlockable[index].getUpCost() <= unit.getUnitPoints()) {
            unit.setUnitPoints(unit.getUnitPoints()-unlockable[index].getUpCost());
            GameManager.getInstance().addSunnyPoint(unlockable[index].getSpReward());
            return true;
        }
        return false;
    }

    public boolean unlockEwasteObject(int index){
        EWasteRecycleUnit unit = getEWasteUnit();
        if (unlockable[index].getUpCost() <= unit.getUnitPoints()) {
            unit.setUnitPoints(unit.getUnitPoints()-unlockable[index].getUpCost());
            GameManager.getInstance().addSunnyPoint(unlockable[index].getSpReward());
            QuestManager.getInstance().increaseCounterQuest3();
            return true;
        }
        return false;
    }

    public boolean unlockCompostObject(int index){
        CompostRecycleUnit unit = getCompostUnit();
        if (unlockable[index].getUpCost() <= unit.getUnitPoints()) {
            unit.setUnitPoints(unit.getUnitPoints()-unlockable[index].getUpCost());
            GameManager.getInstance().addSunnyPoint(unlockable[index].getSpReward());
            return true;
        }
        return false;
    }

    //Getter costi e ricavi oggetti sbloccabili-----------------------------------------------------
    public int getCostOfUnlockable1(){
        return unlockable[0].getUpCost();
    }
    public int getCostOfUnlockable2(){
        return unlockable[1].getUpCost();
    }
    public int getCostOfUnlockable3(){
        return unlockable[2].getUpCost();
    }
    public int getCostOfUnlockable4(){
        return unlockable[3].getUpCost();
    }
    public int getGainOfUnlockable1(){
        return unlockable[0].getSpReward();
    }
    public int getGainOfUnlockable2(){
        return unlockable[1].getSpReward();
    }
    public int getGainOfUnlockable3(){
        return unlockable[2].getSpReward();
    }
    public int getGainOfUnlockable4(){
        return unlockable[3].getSpReward();
    }

    //Getter e setter controllo degli oggetti sbloccabili
    public boolean getPlasticObject(int index) { return plasticObject[index]; }
    public boolean getGlassObject(int index) { return glassObject[index]; }
    public boolean getPaperObject(int index) { return paperObject[index]; }
    public boolean getAluminiumObject(int index) { return aluminiumObject[index]; }
    public boolean getSteelObject(int index) { return steelObject[index]; }
    public boolean getEwasteObject(int index) { return ewasteObject[index]; }
    public boolean getCompostObject(int index) { return compostObject[index]; }

    public boolean setPlasticObject(int index) { return this.plasticObject[index] = true; }
    public boolean setGlassObject(int index) { return this.glassObject[index] = true; }
    public boolean setPaperObject(int index) { return this.paperObject[index] = true; }
    public boolean setAluminiumObject(int index) { return this.aluminiumObject[index] = true; }
    public boolean setSteelObject(int index) { return this.steelObject[index] = true; }
    public boolean setEwasteObject(int index) { return this.ewasteObject[index] = true; }
    public boolean setCompostObject(int index) { return this.compostObject[index] = true; }


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

    public PaperRecycleUnit getPaperUnit(){
        for(RecycleUnit i : unlockedUnits){
            if(i instanceof PaperRecycleUnit){
                return (PaperRecycleUnit) i;
            }
        }
        return null;
    }

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

    public interface SoundFx{
        void suonoUnità();
    }


}
