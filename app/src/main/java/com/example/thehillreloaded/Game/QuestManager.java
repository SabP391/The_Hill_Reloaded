package com.example.thehillreloaded.Game;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.Toast;

import com.example.thehillreloaded.GameActivity;
import com.example.thehillreloaded.Model.FirebaseUserDataAccount;
import com.example.thehillreloaded.Model.GameEnded;
import com.example.thehillreloaded.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.logging.ConsoleHandler;

public class QuestManager{

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

    private SoundFX sfx;
    private Context context;
    Handler handler;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Gson gson = new Gson();
    //FIREBASE
    FirebaseDatabase mDatabase;

    private QuestManager(){
    }

    public static QuestManager getInstance(){
        if(instance == null){
            instance = new QuestManager();
        }
        return instance;
    }

    public void questManagerReload(boolean quest1, boolean quest2, boolean quest3, int counterQuest3, boolean quest4,
                                   int counterQuest4, boolean quest5, boolean quest6, int counterQuest6){
        this.quest1 = quest1;
        this.quest2 = quest2;
        this.quest3 = quest3;
        this.counterQuest3 = counterQuest3;
        this.quest4 = quest4;
        this.counterQuest4 = counterQuest4;
        this.quest5=quest5;
        this.quest6=quest6;
        this.counterQuest6= counterQuest6;
    }

    //init per salvataggio su db in caso di vittoria
    public void initInstance(Context context){
        this.context = context;
        handler = new android.os.Handler();
        this.sfx = (QuestManager.SoundFX) context;
        //inizializzo gli shared
        pref = this.context.getSharedPreferences("HillR_pref", 0);
        //Inizializzo istanza di firebase
        mDatabase = FirebaseDatabase.getInstance("https://the-hill-reloaded-f6f3b-default-rtdb.europe-west1.firebasedatabase.app");
    }

    public void destroy(){
        instance = null;
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

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, R.string.missione_completata, Toast.LENGTH_SHORT).show();
                        }
                    });

                    sfx.missionFX();
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

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, R.string.missione_completata, Toast.LENGTH_SHORT).show();
                        }
                    });

                    sfx.missionFX();
                    return true;
                } else return false;
            }
        }else return true;
    }

    public boolean isQuest2Complete(){
        if(!quest2) {
            if (GameManager.getInstance().getSunnyPoints() >= Q2_COMPLETION) {
                quest2 = true;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, R.string.missione_completata, Toast.LENGTH_SHORT).show();
                    }
                });

                sfx.missionFX();
                return true;
            } else return false;
        }else return true;
    }

    public boolean isQuest3Complete(){
        if(!quest3) {
            if (counterQuest3 >= 3) {
                quest3 = true;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, R.string.missione_completata, Toast.LENGTH_SHORT).show();
                    }
                });

                sfx.missionFX();
                return true;
            }else return false;
        }else return true;
    }

    public boolean isQuest4Complete(){
        if(!quest4) {
            if (counterQuest4 >= Q4_COMPLETION) {
                quest4 = true;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, R.string.missione_completata, Toast.LENGTH_SHORT).show();
                    }
                });

                sfx.missionFX();
                return true;
            }else return false;
        }else return true;
    }

    public boolean isQuest5Complete(){
        if(!quest5){
            if(RecycleUnitsManager.getInstance().getGlassUnit().getUnitStatus() == RecycleUnitStatus.UPGRADED_TWICE){
                quest5 = true;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, R.string.missione_completata, Toast.LENGTH_SHORT).show();
                    }
                });

                sfx.missionFX();
                return true;
            }else return false;
        }return true;
    }

    public boolean isQuest6Complete(){
        if(!quest6) {
            if (counterQuest6 >= Q6_COMPLETION) {
                quest6 = true;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, R.string.missione_completata, Toast.LENGTH_SHORT).show();
                    }
                });

                sfx.missionFX();
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
            if(isQuest1Complete() && isQuest2Complete() && isQuest3Complete()){
                saveFirebaseGame();
                return true;
            }else return false;
        } else{
            if(isQuest1Complete() && isQuest2Complete() && isQuest3Complete()
                    && isQuest4Complete() && isQuest5Complete() && isQuest6Complete()){
                saveFirebaseGame();
                return true;
            }else return false;
        }
    }

    public void saveFirebaseGame() {
        if(pref.getAll().containsKey("account-utente-loggato")) {
            GameEnded gameEnded = new GameEnded(GameManager.getInstance().getSunnyPoints(),
                    (System.nanoTime() - GameManager.getInstance().getTimeAtGameStart()), System.nanoTime(),
                    gson.fromJson(pref.getAll().get("account-utente-loggato").toString(), FirebaseUserDataAccount.class).getEmail());
            //Scrivo sul db prendendo il riferimento a tutti i nodi (non a uno specifico)
            DatabaseReference myRef= mDatabase.getReference();
            //con il primo child punto al nodo Utenti - che rappresenta il nome della Tabella -
            //col secondo child punto al valore  chiave quindi creo un nuovo record email
            //col terzo child scrivo l'oggetto
            myRef.child("Utenti").child(gson.fromJson(pref.getAll().get("account-utente-loggato").toString(), FirebaseUserDataAccount.class).getuId()).push().setValue(gameEnded);
        }
    }

    public interface SoundFX{
        void missionFX();
    }
}
