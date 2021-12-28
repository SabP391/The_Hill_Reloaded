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

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.Random;

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

    // Variabili relative al gioco e alla sua logica -----------------------------------------------
    private TileMap tileMap;
    private SunnyPointsCounter sPC;
    private int firstTileOfTheHill;
    private int numberOfTilesOfTheHill;
    private GameItem movingItem = null;
    private Bitmap backGround;
    private Bitmap mixedArray[];
    private Point size;
    private LinkedList<GameItem> itemsOnScreen;
    private long elapsedTime;
    private Random rand;
    private int currentIndex = 0;
    private Point spriteSize;
    private LinkedList<RecycleUnit> unitsOnScreen;
    ItemType values[];



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
        GameManager.getInstance().initInstance(GameMode.CLASSIC);
        mixedArray = new Bitmap[60];
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        tileMap = new TileMap(context);
        numberOfTilesOfTheHill = 5;
        firstTileOfTheHill = (int)((tileMap.getMapSize().x / 2) - (numberOfTilesOfTheHill / 2));
        itemsOnScreen = new LinkedList<GameItem>();
        backGround = GameAssets.getInstance(context).getGameBackGround(size);
        Point tileSize = new Point((int)tileMap.getTileSize(), (int)tileMap.getTileSize());
        for(int i = 0; i < Array.getLength(mixedArray); i++){
            mixedArray[i] = GameAssets.getInstance(context).getMixed(tileSize);
        }
        sPC = new SunnyPointsCounter(tileMap, context);
        elapsedTime = 0;
        rand = new Random();
        values = ItemType.values();
        spriteSize = new Point((int) (tileMap.getTileSize()), (int) (tileMap.getTileSize()));
        unitsOnScreen = new LinkedList<RecycleUnit>();
        unitsOnScreen.add(new GlassRecycleUnit(tileMap, context));
        unitsOnScreen.add(new PlasticRecycleUnit(tileMap, context));
        unitsOnScreen.add(new PaperRecycleUnit(tileMap, context));
        unitsOnScreen.add(new AluminiumRecycleUnit(tileMap, context));
        unitsOnScreen.add(new SteelRecycleUnit(tileMap, context));
        unitsOnScreen.add(new IncineratorUnit(tileMap, context));
        unitsOnScreen.add(new EWasteRecycleUnit(tileMap, context));
        unitsOnScreen.add(new CompostRecycleUnit(tileMap, context));
    }

    // Metodi per la gestione del rendering e della logica di gioco --------------------------------

    // metodo per il rendering, in questo metodo vanno
    // inserite le cose da mostrare a schermo
    public void render(@NonNull Canvas c){
        c.drawBitmap(backGround, 0, 0, null);
        tileMap.drawHillAreaRectangle(c);
        sPC.draw(c);
        for(RecycleUnit i : unitsOnScreen){
            i.drawUnit(c);
        }
        if(GameManager.getInstance().isPaused()){
            for(int i = 0; i < itemsOnScreen.size(); i++){
                c.drawBitmap(mixedArray[i], itemsOnScreen.get(i).getPosition().x, itemsOnScreen.get(i).getPosition().y, null);
            }
        }else{
            //tileMap.drawTilemap(c);
            for(GameItem i : itemsOnScreen){
                i.drawObject(c);
            }
        }
    }

    // metodo che gestisce la logica di gioco
    public void gameLogic(){
        if(!GameManager.getInstance().isPaused()){
            if(GameManager.getInstance().isTimeToSpawn(System.nanoTime())){
                int initialTile = rand.nextInt(numberOfTilesOfTheHill) + firstTileOfTheHill;
                itemsOnScreen.add(new GameItem(initialTile, tileMap, context, values[rand.nextInt(values.length)]));
            }
            for(GameItem i : itemsOnScreen){
                if(i != movingItem){
                    i.fall(System.nanoTime());
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
       if(!GameManager.getInstance().isPaused()){
           // Recupera le coordinate su cui è stato effettuato il tocco
           float x = event.getRawX();
           float y = event.getRawY();
           // Incapsula le coordinate del tocco in un point
           Point touchPosition = new Point((int) x, (int) y);
           // Trova l'indice della tile all'interno della quale è
           // stato rilevto il tocco
           int tile = tileMap.getTileIndexFromPosition(touchPosition);
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
                       // Se un oggetto è in movimento, modifica le sue coordinate
                       // in base alla posizione del dito sullo schermo
                       movingItem.setPosition(touchPosition);
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
                       movingItem.setPosition(tileMap.getPositionFromTileIndex(movingItem.getCurrentTile()));
                       movingItem = null;
                   }
                   break;
           }
       }
        return true;
    }
}
