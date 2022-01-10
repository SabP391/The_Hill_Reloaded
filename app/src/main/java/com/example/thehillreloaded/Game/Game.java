package com.example.thehillreloaded.Game;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.thehillreloaded.GameActivity;
import com.example.thehillreloaded.GameOverActivity;
import com.example.thehillreloaded.GameWonActivity;
import com.example.thehillreloaded.Model.GameEnded;
import com.example.thehillreloaded.Model.GoogleLoggedDataAccount;
import com.example.thehillreloaded.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Game extends SurfaceView implements SurfaceHolder.Callback, Runnable{

    // Variabili necessarie per il rendering e il gamloop ------------------------------------------

    // Holder necessario per accedere alla surfaceView in cui verrà renderizzato il gioco
    private SurfaceHolder holder;
    // Thread per il rendering
    private Thread drawThread;
    // Variabile booleana per controllare che la superficie sia pronta per essere aggiornata
    private boolean isSurfaceReady = false;
    // Variabile booleana per tenere sotto controllo lo stato del renderer
    private boolean isDrawing = false;
    // Tempo per frame per ottenere i 60 FPS
    private static final int MAX_FRAME_TIME = (int) (1000.0 / 60.0);
    // Variabile per il context
    private Context context;
    private static final String LOGTAG = "surface";
    private boolean timeToDestroy = false;
    private Handler messageHandler;

    // Variabili relative al gioco e alla sua logica -----------------------------------------------
    private TileMap map;
    private GameItem movingItem = null;
    private Bitmap mixedArray[];
    private ConcurrentLinkedQueue<GameItem> itemsOnScreen;
    private long elapsedTime;
    private Random rand;
    private int currentIndex = 0;
    private Point spriteSize;
    private ConcurrentLinkedQueue<RecycleUnit> unitsOnScreen;
    private long lastUpdate;
    private SoundFx sFX;
    private Bundle info;

    private final static int SHAKE_SENSITIVITY = 10;
    private float accelerationVal, accelerationLast, shake;

    //Inizializzo gli shared e il Gson
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Gson gson = new Gson();
    //FIREBASE
    FirebaseDatabase mDatabase;

    // Creazione e inizializzazione della classe Game ----------------------------------------------

    // Costruttore per la classe Game
    public Game(Context context, TileMap map, Bundle bundle) {
        super(context);
        this.map = map;
        this.context = context;
        sFX = (Game.SoundFx) context;
        init();
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), sensorManager.SENSOR_DELAY_GAME);

        accelerationVal = SensorManager.GRAVITY_EARTH;
        accelerationLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;
        info = new Bundle();
        info.putInt("GAME_MODE", bundle.getInt("GAME_MODE"));
        info.putInt("GAME_DIFF", bundle.getInt("GAME_DIFF"));
    }

    // Inizializza la classe Game
    public void init(){
        this.holder = getHolder();
        this.holder.addCallback(this);
        setFocusable(true);

        mixedArray = new Bitmap[60];
        itemsOnScreen = GameItemsManager.getInstance().getItemsOnScreen();
        Point tileSize = new Point((int)map.getTileSize(), (int)map.getTileSize());
        for(int i = 0; i < Array.getLength(mixedArray); i++){
            mixedArray[i] = GameAssets.getInstance(context).getMixed(tileSize);
        }
        elapsedTime = 0;
        rand = new Random();
        spriteSize = new Point((int) (map.getTileSize()), (int) (map.getTileSize()));
        unitsOnScreen = RecycleUnitsManager.getInstance().getUnlockedUnits();

        messageHandler = new Handler();

        //inizializzo gli shared
        pref = this.context.getSharedPreferences("HillR_pref", 0);
        //Inizializzo istanza di firebase
        mDatabase = FirebaseDatabase.getInstance("https://the-hill-reloaded-f6f3b-default-rtdb.europe-west1.firebasedatabase.app");
    }

    // Metodi per la gestione del rendering e della logica di gioco --------------------------------

    // metodo per il rendering, in questo metodo vanno
    // inserite le cose da mostrare a schermo
    public void render(@NonNull Canvas c){
        map.drawBackground(c);
        GameManager.getInstance().getSunnyPointsCounter().draw(c);
        for(RecycleUnit i : unitsOnScreen){
            i.drawUnit(c, System.nanoTime());
        }
        if(GameManager.getInstance().isPaused()){
            int index = 0;
            for(GameItem i : itemsOnScreen){
                c.drawBitmap(mixedArray[index], i.getPosition().x, i.getPosition().y, null);
                index++;
            }
        }else{
            //map.drawTilemap(c);
            for(GameItem i : itemsOnScreen){
                i.drawObject(c);
            }
        }
    }

    // metodo che gestisce la logica di gioco
    public void gameLogic(){
        if(QuestManager.getInstance().isGameWon()){
            Intent gameWon = new Intent(context, GameWonActivity.class);
            stopDrawThread();
            context.startActivity(gameWon);
        }
        if(!GameManager.getInstance().isPaused()){
            if(GameManager.getInstance().isTimeToSpawn(System.nanoTime())){
                GameItemsManager.getInstance().spawnNewObject();
            }
            for(GameItem i : itemsOnScreen){
                if(i != movingItem){
                    if(i.checkForGameOverPosition()){
                        Intent gameLost = new Intent(context, GameOverActivity.class);
                        gameLost.putExtras(info);
                        stopDrawThread();
                        sFX.suonoGameOver();
                        context.startActivity(gameLost);

                        //Verifico se l'utente è loggato, se si procedo a salvare i dati su firebase
                        if(pref.getAll().containsKey("account-utente-loggato")) {
                            GameEnded gameEnded = new GameEnded(GameManager.getInstance().getSunnyPoints(),
                                    (System.nanoTime() - GameManager.getInstance().getTimeAtGameStart()), System.nanoTime(),
                                    gson.fromJson(pref.getAll().get("account-utente-loggato").toString(), GoogleLoggedDataAccount.class).getPersonEmail());
                            //Scrivo sul db prendendo il riferimento a tutti i nodi (non a uno specifico)
                            DatabaseReference myRef= mDatabase.getReference();
                            //con il primo child punto al nodo Utenti - che rappresenta il nome della Tabella -
                            //col secondo child punto al valore  chiave quindi creo un nuovo record email
                            //col terzo child scrivo l'oggetto
                            myRef.child("Utenti").child(gson.fromJson(pref.getAll().get("account-utente-loggato").toString(), GoogleLoggedDataAccount.class).getPersonId()).push().setValue(gameEnded);
                        }
                    }
                    i.fall(System.nanoTime());
                    if(i.checkForBuffDestruction()){
                        map.setTileValue(i.getCurrentTile(), 0);
                        itemsOnScreen.remove(i);
                    }
                }
            }
        }
    }

    // Override dei metodi di SurfaceView ----------------------------------------------------------

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        // Recupera il surfaceHolder
        this.holder = surfaceHolder;

        // Se c'è già un thread di rendering attivo tenta di
        // eseguire il join del thread. In questo modo il thread viene
        // disattivato e la superficie viene impostata come nuovamente
        // pronta al rendering
        if (drawThread != null){
            Log.d(LOGTAG, "Il thread di rendering è già attivo");
            isDrawing = false;
            try{
                drawThread.join();
            } catch(InterruptedException e){}
        }

        isSurfaceReady = true;
        // Viene lanciato un nuovo thread di rendering
        startDrawThread();
        Log.d(LOGTAG, "Superficie creata");

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // Metodo non necessario, poichè la rotazione del telefono è bloccata
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        // Interrompe il thread di rendering, restituisce la superficie
        // e l'holder della superficie
        stopDrawThread();
        holder.getSurface().release();
        this.holder = null;
        isSurfaceReady = false;
        Log.d(LOGTAG, "Superficie distrutta");
    }


    // Metodi per la gestione del thread di rendering ----------------------------------------------

    public void startDrawThread(){
        // Se la superficie è pronta per il rendering e non
        // ci sono thread già attivi crea un nuovo thread di rendering
        if(isSurfaceReady && drawThread == null){
            drawThread = new Thread(this, "Draw thread");
            isDrawing = true;
            drawThread.start();
        }
    }

    public void stopDrawThread(){
        // Se non c'è nessun thread attivo non viene eseguito nulla
        if(drawThread == null){
            Log.d(LOGTAG, "Non c'è nessun thread di rendering attivo al momento");
            return;
        }
        // Se vi è un thread attivo viene effetutato il join
        // e reimpostata la variabile isDrawing a false per permettere
        // una successiva riattivazione
        isDrawing = false;
        while(true){
            try{
                Log.d(LOGTAG, "In attesa dell'ultimo frame");
                drawThread.join(5000);
                break;
            } catch(Exception e){
                Log.e(LOGTAG, "Non è stato possibile eseguire il join del thread");
            }
        }
        drawThread = null;
    }


    // Override di run() : in questo metodo inizia il game loop vero e proprio
    @Override
    public void run() {
        Log.d(LOGTAG, "Inizio del gameloop");
        // Variabili per mantenere il framerate più costante possibile
        long frameStartTime;
        long frameTime;

        // Inizio del loop
        while(isDrawing){
            if(holder == null){
                return;
            }
            // Tempo di inizio del frame
            frameStartTime = System.nanoTime();
            // Viene inizializzata la canvas da renderizzare
            Canvas canvas = holder.lockCanvas();
            // Se la canvas è stata inizializzata corettamente vengono
            // eseguiti i metodi gameLogic e render, dove vengono gestite
            // effettivamente le logiche di gioco e il rendering
            if(canvas != null){
                try{
                    synchronized (holder){
                        gameLogic();
                        render(canvas);
                    }
                } finally {
                    // Alla fine del frame viene in ogni caso rilasciata la canvas
                    // ed effettuato l'aggiornamento dello schermo
                    holder.unlockCanvasAndPost(canvas);
                }
            }

            // Calcolo della durata del frame attuale
            frameTime = (System.nanoTime() / frameStartTime) / 1000000;

            // Se il frame è stato troppo breve si attende per mantenere
            // il framerate costante
            if(frameTime < MAX_FRAME_TIME){
                try {
                    Thread.sleep(MAX_FRAME_TIME - frameTime);
                } catch (InterruptedException e) {}
            }
        }
        Log.d(LOGTAG, "fine del gameloop");
    }

    // Metodi per la gestione degli input ----------------------------------------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event){
       if(!GameManager.getInstance().isPaused()){
           // Recupera le coordinate su cui è stato effettuato il tocco
           float x = event.getRawX();
           float y = event.getRawY();
           // Incapsula le coordinate del tocco in un point
           Point touchPosition = new Point((int) x, (int) y);
           // Trova l'indice della tile all'interno della quale è
           // stato rilevto il tocco
           int tile;
           if(isEmulator()){
               tile = map.getTileIndexFromPosition(touchPosition);
           }else{
               tile = map.getTileIndexFromPosition(touchPosition) - 1;
           } // In questo punto bisogna togliere -1 se si vuole avere il touch corretto su EMULATORE, -1 è necessario per avere il touch preciso su smartphone
           switch(event.getAction()){
               // Al primo tocco sullo schermo controlla che il tocco
               // sia avvenuto su uno degli oggetti in gioco
               case MotionEvent.ACTION_DOWN:
                   for(GameItem i : itemsOnScreen){
                       // Il controllo viene effettuato confrontando
                       // la tile in cui è effettuato il tocco con la current tile
                       // di ogniuno degli oggetti in gioco
                       if(i.getCurrentTile() == tile){

                           // Se viene trovato un oggetto viene assegnato alla
                           // variabile moving item
                           movingItem = i;
                       }
                   }
                   break;
               // Se non è il primo tocco ma c'è un movimento del dito
               // sullo schermo controlla che sia stato preso un oggetto
               // nella fase precedente
               case MotionEvent.ACTION_MOVE:
                   if(movingItem != null){
                       Point movPos = new Point((int) (x - (map.getTileSize()/2)), (int) (y - (map.getTileSize()/2)));
                       // Se un oggetto è in movimento, modifica le sue coordinate
                       // in base alla posizione del dito sullo schermo
                       movingItem.setPosition(movPos);
                   }
                   break;
               // Quando il dito viene sollevato o viene portato fuori dallo schermo
               // controlla che un oggetto si stesse spostando
               case MotionEvent.ACTION_UP:
               case MotionEvent.ACTION_CANCEL:
               case MotionEvent.ACTION_OUTSIDE:
                   if(movingItem != null){
                       // Se c'era un oggetto in movimento,
                       // lo riporta alla tile in cui si trovava prima del tocco
                       // e reimposta a null la variabile movingItem

                       if(RecycleUnitsManager.getInstance().processItemOnScreen(movingItem)){
                           map.setTileValue(movingItem.getCurrentTile(), 0);
                           itemsOnScreen.remove(movingItem);
                           movingItem = null;
                       }else{
                           movingItem.setPosition(map.getPositionFromTileIndex(movingItem.getCurrentTile()));
                           movingItem = null;
                       }
                   }
                   break;
           }
       }
        return true;
    }

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            accelerationLast = accelerationVal;
            accelerationVal = (float) Math.sqrt((double) (x * x) + (y * y) + (z * z));
            float delta = accelerationVal - accelerationLast;
            shake = shake * 0.9f + delta;
            IncineratorUnit incinerator = RecycleUnitsManager.getInstance().getIncineratorUnit();
            if (shake > SHAKE_SENSITIVITY) {
                int inc = incinerator.destroyFirstLine(itemsOnScreen);
                if (inc == 0) {
                    Toast.makeText(context, R.string.sunny_non_sufficienti, Toast.LENGTH_SHORT).show();
                } else if (inc == 1) {
                    Toast.makeText(context, R.string.nessun_rifiuto, Toast.LENGTH_SHORT).show();
                }
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) { }
    };

    // Metodo per controllare se l'applicazione gira su emulatore
    // per cambiare il metodo che riconosce il tocco
    private boolean isEmulator() {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("sdk_gphone64_arm64")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator");
    }

    public interface SoundFx{
        void suonoGameOver();
    }

}
