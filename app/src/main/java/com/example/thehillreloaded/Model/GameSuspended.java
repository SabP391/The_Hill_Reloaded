package com.example.thehillreloaded.Model;

import com.example.thehillreloaded.Game.GameItem;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

//classe model che dovrà contenere tutti dati da salvare poi come oggetto su shared preferences
//è' una bozza
public class GameSuspended {
    //dati dinamici - verranno sovrascritti al momento in cui la prtita verrà ripresa -

    //GoogleSignIn
    private String mailUtente;
    //GameManager
    private int temporaryScore;
    private long temporaryGameTime;
    //RecycleUnitsManager
    private ConcurrentLinkedQueue<GameItem> itemsOnScreen;
    private ArrayList<Integer> tm;
    private boolean isPaperUnitUnlocked;
    private boolean isCompostUnlocked;
    private boolean isAluminiumUnitUnlocked;
    private boolean isSteelUnitUnlocked;
    private boolean isPlasticUnitUnlocked;
    private boolean isEwasteUnitUnlocked;
    private boolean isGlassUnitUnlocked;
    //QuestManager
    private boolean quest1;
    private boolean quest2;
    private boolean quest3;
    private int counterQuest3;
    private boolean quest4;
    private int counterQuest4;
    private boolean quest5;
    private boolean quest6;
    private int counterQuest6;

}
