package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import java.util.Random;

public class Game extends SurfaceView implements SurfaceHolder.Callback, Runnable{

    // Membri della classe -------------------------------------------------------------------------

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
    private int x = 0, y = 0;

    private Bitmap bitmap;
    private GameItem obj[];
    private Bitmap backGround;
    private Point size;
    private TileMap tileMap;
    private int currentIndex = 0;



    // Creazione e inizializzazione della classe Game ----------------------------------------------

    // Costruttore per la classe Game
    public Game(Context context) {
        super(context);
        init(context);
    }

    // Inizializza la classe Game
    public void init(Context context){
        this.context = context;
        this.holder = getHolder();
        this.holder.addCallback(this);
        setFocusable(true);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        tileMap = new TileMap(10, size);
        Point a = new Point((int)tileMap.getTileSize(), (int)tileMap.getTileSize());
        bitmap = GameAssets.getInstance(context).getRandAsset(a);
        obj = new GameItem[10];
        Random rand = new Random();
        ItemType values[] = ItemType.values();
        for(int i = 0; i < 10; i++){
            obj[i] = new GameItem(rand.nextInt(15), tileMap, context, values[rand.nextInt(5)]);
        }
        backGround = GameAssets.getInstance(context).getGameBackGround(size);
    }

    // Metodi per la gestione del rendering e della logica di gioco --------------------------------

    // metodo per il rendering, in questo metodo vanno
    // inserite le cose da mostrare a schermo
    public void render(@NonNull Canvas c){
        c.drawBitmap(backGround, 0, 0, null);
        tileMap.drawTilemap(c);
        for(int i = 0; i < 10; i++){
            obj[i].drawObject(c);
        }
    }

    // metodo che gestisce la logica di gioco
    public void gameLogic(){
        for(int i = 0; i < 10; i++){
            obj[i].fall(System.nanoTime());
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
        // Per ora non fa niente, da implementare
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
        obj[currentIndex].setOnScreen(true);
        currentIndex++;
        return true;
    }
}
