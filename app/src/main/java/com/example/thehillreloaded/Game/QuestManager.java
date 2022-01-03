package com.example.thehillreloaded.Game;

public class QuestManager {
    /*
    DA FARE

    5 - sblocca livello 3 della unità Vetro
    6 - guadagna 20 Unit Points sull'unità della Carta
     */

    private static QuestManager instance;
    private boolean quest1; //1 - costruisci tutte le unità di riciclo
    private boolean quest2; //2 - guadagna almeno 100 Sunny Points
    private boolean quest3; //3 - sblocca 3 oggetti golden da Ewaste
    private int counterQuest3 = 0;
    private boolean quest4; //4 - ricicla 50 oggetti di organico
    private int counterQuest4 = 0;
    private boolean quest5;
    private boolean quest6;
    private int counterQuest6 = 0;

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
        if(GameManager.getInstance().getGameMode() == GameMode.CLASSIC) {
            if (RecycleUnitsManager.getInstance().isAluminiumUnitUnlocked()
                    && RecycleUnitsManager.getInstance().isEwasteUnitUnlocked()
                    && RecycleUnitsManager.getInstance().isGlassUnitUnlocked()
                    && RecycleUnitsManager.getInstance().isPaperUnitUnlocked()
                    && RecycleUnitsManager.getInstance().isPlasticUnitUnlocked()
                    && RecycleUnitsManager.getInstance().isSteelUnitUnlocked()) {
                quest1 = true;
                return true;
            }else return false;
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
    }

    public boolean isQuest2Complete(){
            if (GameManager.getInstance().getSunnyPoints() > 100){
                quest2 = true;
                return true;
            }else return false;
    }

    public void isQuest3Complete(){
        if (RecycleUnitsManager.getInstance().unlockEwasteObject(3)){
        }
        if(counterQuest3 >= 2){
            quest3 = true;
        }
    }

    public void isQuest4Complete(){
        if (counterQuest4 >= 49){
            quest4 = true;
        }
    }

    public void isQuest5Complete(){

    }

    public void isQuest6Complete(){
        if (counterQuest6 >= 19){
            quest6 = true;
        }
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
