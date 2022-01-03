package com.example.thehillreloaded.Game;

public class QuestManager {
    /*
    DA FARE
    4 - ricicla 50 oggetti di organico
    5 - sblocca livello 3 della unità Vetro
    6 - guadagna 20 Unit Points sull'unità della Carta
     */

    private static QuestManager instance;
    private boolean quest1; //1 - costruisci tutte le unità di riciclo
    private boolean quest2; //2 - guadagna almeno 100 Sunny Points
    private boolean quest3; //3 - sblocca 3 oggetti golden da Ewaste
    private int counterQuest3 = 0;
    private boolean quest4;
    private int counterQuest4;
    private boolean quest5;
    private boolean quest6;
    private int counterQuest6;

    private QuestManager(){
    }

    public static QuestManager getInstance(){
        if(instance == null){
            instance = new QuestManager();
        }
        return instance;
    }

    public boolean isQuest1() {
        return quest1;
    }

    public boolean isQuest2() {
        return quest2;
    }

    public boolean isQuest3() {
        return quest3;
    }

    public boolean isQuest4() {
        return quest4;
    }

    public boolean isQuest5() {
        return quest5;
    }

    public boolean isQuest6() {
        return quest6;
    }

    public void isQuest1Complete(){
        if(GameManager.getInstance().getGameMode() == GameMode.CLASSIC) {
            if (RecycleUnitsManager.getInstance().isAluminiumUnitUnlocked()
                    && RecycleUnitsManager.getInstance().isEwasteUnitUnlocked()
                    && RecycleUnitsManager.getInstance().isGlassUnitUnlocked()
                    && RecycleUnitsManager.getInstance().isPaperUnitUnlocked()
                    && RecycleUnitsManager.getInstance().isPlasticUnitUnlocked()
                    && RecycleUnitsManager.getInstance().isSteelUnitUnlocked()) {
                quest1 = true;
            }
        } else {
            if (RecycleUnitsManager.getInstance().isAluminiumUnitUnlocked()
                    && RecycleUnitsManager.getInstance().isCompostUnlocked()
                    && RecycleUnitsManager.getInstance().isEwasteUnitUnlocked()
                    && RecycleUnitsManager.getInstance().isGlassUnitUnlocked()
                    && RecycleUnitsManager.getInstance().isPaperUnitUnlocked()
                    && RecycleUnitsManager.getInstance().isPlasticUnitUnlocked()
                    && RecycleUnitsManager.getInstance().isSteelUnitUnlocked()) {
                quest1 = true;
            }
        }
    }

    public void isQuest2Complete(){
            if (GameManager.getInstance().getSunnyPoints() > 100){
                quest2 = true;
            }
    }

    public void isQuest3Complete(){
        if (RecycleUnitsManager.getInstance().unlockEwasteObject(3)){
            counterQuest3++;
        }
        if(counterQuest3 >= 3){
            quest3 = true;
        }
    }

    public void isQuest4Complete(){

    }

    public void isQuest5Complete(){

    }

    public void isQuest6Complete(){

    }
}
