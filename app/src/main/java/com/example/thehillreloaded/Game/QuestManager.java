package com.example.thehillreloaded.Game;

public class QuestManager {

    private static QuestManager instance;
    private boolean quest1 = false; //1 - costruisci tutte le unità di riciclo
    private boolean quest2 = false; //2 - guadagna almeno 100 Sunny Points
    private boolean quest3 = false; //3 - sblocca 3 oggetti golden da Ewaste
    private int counterQuest3 = 0;
    private boolean quest4 = false; //4 - ricicla 50 oggetti di organico
    private int counterQuest4 = 0;
    private boolean quest5 = false; //5 - sblocca livello 3 della unità Vetro
    private boolean quest6 = false; //6 - guadagna 20 Unit Points sull'unità della Carta
    private int counterQuest6 = 0;

    protected static final int Q2_COMPLETION = 100;
    protected static final int Q4_COMPLETION = 35;
    protected static final int Q6_COMPLETION = 20;

    private QuestManager(){
    }

    public static QuestManager getInstance(){
        if(instance == null){
            instance = new QuestManager();
        }
        return instance;
    }

    public void increaseCounterQuest3() {
        this.counterQuest3 ++;
    }

    public void increaseCounterQuest4() {
        this.counterQuest4 ++;
    }

    public void increaseCounterQuest6() {
        this.counterQuest6 ++;
    }

    //Chiamato quando viene costruita una centrale
    public boolean isQuest1Complete(){
        if(!quest1) {
            if (GameManager.getInstance().getGameMode() == GameMode.CLASSIC) {
                if (RecycleUnitsManager.getInstance().isAluminiumUnitUnlocked()
                        && RecycleUnitsManager.getInstance().isEwasteUnitUnlocked()
                        && RecycleUnitsManager.getInstance().isGlassUnitUnlocked()
                        && RecycleUnitsManager.getInstance().isPaperUnitUnlocked()
                        && RecycleUnitsManager.getInstance().isPlasticUnitUnlocked()
                        && RecycleUnitsManager.getInstance().isSteelUnitUnlocked()) {
                    quest1 = true;
                    return true;
                } else return false;
            } else {
                if (RecycleUnitsManager.getInstance().isAluminiumUnitUnlocked()
                        && RecycleUnitsManager.getInstance().isCompostUnlocked()
                        && RecycleUnitsManager.getInstance().isEwasteUnitUnlocked()
                        && RecycleUnitsManager.getInstance().isGlassUnitUnlocked()
                        && RecycleUnitsManager.getInstance().isPaperUnitUnlocked()
                        && RecycleUnitsManager.getInstance().isPlasticUnitUnlocked()
                        && RecycleUnitsManager.getInstance().isSteelUnitUnlocked()) {
                    quest1 = true;
                    return true;
                } else return false;
            }
        }else return true;
    }

    public boolean isQuest2Complete(){
        if(!quest2) {
            if (GameManager.getInstance().getSunnyPoints() >= Q2_COMPLETION) {
                quest2 = true;
                return true;
            } else return false;
        }else return true;
    }

    public boolean isQuest3Complete(){
        if(!quest3) {
            if (counterQuest3 >= 3) {
                quest3 = true;
                return true;
            }else return false;
        }else return true;
    }

    public boolean isQuest4Complete(){
        if(!quest4) {
            if (counterQuest4 >= Q4_COMPLETION) {
                quest4 = true;
                return true;
            }else return false;
        }else return true;
    }

    public boolean isQuest5Complete(){
        if(!quest5){
            if(RecycleUnitsManager.getInstance().getGlassUnit().getUnitStatus() == RecycleUnitStatus.UPGRADED_TWICE){
                quest5 = true;
                return true;
            }else return false;
        }return true;
    }

    public boolean isQuest6Complete(){
        if(!quest6) {
            if (counterQuest6 >= Q6_COMPLETION) {
                quest6 = true;
                return true;
            }return false;
        }else return true;
    }

    public int getCounterQuest3() {
        return counterQuest3;
    }

    public int getCounterQuest4() {
        return counterQuest4;
    }

    public int getCounterQuest6() {
        return counterQuest6;
    }

    public boolean isGameWon(){
        if(GameManager.getInstance().getGameMode() == GameMode.CLASSIC){
            if(quest1 && quest2 && quest3){
                return true;
            }else return false;
        } else{
            if(quest1 && quest2 && quest3 && quest4 && quest5 && quest6){
                return true;
            }else return false;
        }
    }
}
