package com.example.thehillreloaded.Game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class TileMap {
    // Membri della classe -------------------------------------------------------------------------
    // Dimensione delle tile. Essendo le tile quadrate,
    // viene salvato un solo valore per altezza e larghezza
    private final float tileSize;
    // Numero di tile che formeranno la mappa
    private final Point mapSize;
    // La tileMap vera è propria viene salvata in un ArrayList
    // di interi, in modo da poter ridurre le coordinate delle tile
    // ad una sola dimensione
    private ArrayList<Integer> tileMap;

    // Costante per determinare lo spessore delle linee
    private static final float LINE_THICKNESS = 5;
    //
    private static final int OFFSET_FROM_BOTTOM = 1;
    // Variabile per disegnare il rettangolo semi trasparente
    // che determina l'area della collina
    private final Paint hillRectanglePaint;
    // Variabile per disenare la linea blu che delimita
    // inferiormente l'area della collina
    private final Paint blueLinePaint;
    // Variabile per disenare la linea rossa che delimita
    // superiormente l'area della collina
    private final Paint redLinePaint;
    // Variabili necessarie per disegnare la tileMap in fase di debug
    private final Paint tileGridPaint;
    private final Paint occupiedTilePaint;

    // Costruttore della classe, prende in input il numero di tile
    // orizzontali richieste (cioé il numero di righe)
    // e la dimensione dello schermo
    // e inizializza la tilemap
    public TileMap(int horizontalTileCount, Point screenSize) {
        mapSize = new Point();
        tileSize = (float) ((screenSize.y / horizontalTileCount) - OFFSET_FROM_BOTTOM);
        this.mapSize.y = horizontalTileCount;
        this.mapSize.x = Math.round((screenSize.x / tileSize));
        tileMap = new ArrayList<Integer>(Collections.nCopies(mapSize.x * mapSize.y, 0));

        // Inizializzazione delle costanti di tipo Paint necessarie
        // a disegnare gli elementi statici della collina
        hillRectanglePaint = new Paint();
        hillRectanglePaint.setColor(Color.DKGRAY);
        hillRectanglePaint.setStyle(Paint.Style.FILL);
        hillRectanglePaint.setAlpha(50);
        blueLinePaint = new Paint();
        blueLinePaint.setColor(Color.BLUE);
        blueLinePaint.setStrokeWidth(LINE_THICKNESS);
        redLinePaint = new Paint();
        redLinePaint.setColor(Color.RED);
        redLinePaint.setStrokeWidth(LINE_THICKNESS);
        // Variabili necessare per il metodo drawTilemap,
        // utili principalmente in fase di debug
        tileGridPaint = new Paint();
        tileGridPaint.setStyle(Paint.Style.STROKE);
        occupiedTilePaint = new Paint();
        occupiedTilePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    // Metodi utili --------------------------------------------------------------------------------

    // Metodo che prende in input l'indice di una tile
    // e ritorna quello della tile subito sotto di essa
    public int getNextTileIndex(int currentTile){
        return currentTile + mapSize.x;
    }

    // Metodo che prende in input l'indice di una tile
    // e ritorna true se la tile subito sotto di essa è libera
    public boolean isNextTileFree(int currentTile){
        return (tileMap.get((currentTile + mapSize.x)) == 0);
    }

    // Metodo che prende in input una posizione e calcola
    // l'indice della tile con quella posizione
    public int getTileIndexFromPosition(Point position){
        Point tile = new Point((int) (position.x / tileSize), (int) (position.y /tileSize));
        return tile.y * mapSize.x + tile.x;
    }

    // Metodo che prende in input un indice e ritorna
    // la posizione della tile con tale indice
    public Point getPositionFromTileIndex(int index){
        return new Point((int)((index % mapSize.x) * tileSize), (int)((index / mapSize.x) * tileSize));
    }

    // Metodo che disegna un rettangolo semi trasparente
    // per evidenziare la zona della collina in cui cadranno gli oggetti.
    // Questo metofo prende come input la canvas su cui disegnare il rettangolo,
    // l'idice della tile da cui far partire il rettangolo
    // e il numero di colonne per cui si vuole che il rettangolo si estenda
    public void drawHillAreaRectangle(Canvas c, int inialTileIndex, int numberOfTiles){
        int rectWidth = (int) (numberOfTiles * tileSize);
        int rectHeight = (int) (tileSize) * mapSize.y;
        // Disegna il rettangolo semi trasparente che delimita la collina
        c.drawRect(inialTileIndex * tileSize,
                0,
                (inialTileIndex * tileSize) + rectWidth,
                rectHeight,
                hillRectanglePaint);
        // Disegna la linea blu che delimita inferiormente la collina
        c.drawLine((inialTileIndex * tileSize),
                rectHeight + 4,
                (inialTileIndex * tileSize) + rectWidth,
                rectHeight + 4,
                blueLinePaint);
        // Disegna la linea rossa che delimita superiormente la collina
        c.drawLine((inialTileIndex * tileSize),
                0 + tileSize,
                (inialTileIndex * tileSize) + rectWidth,
                0 + tileSize,
                redLinePaint);
    }

    // Getter e setter -----------------------------------------------------------------------------
    public float getTileSize() {
        return this.tileSize;
    }

    public Point getMapSize() {
        return this.mapSize;
    }

    public void setTileValue(int index, int tileValue){
        tileMap.set(index, tileValue);
    }

    public int getTileValue(int index){
        return tileMap.get(index);
    }

    // Metodi di utility per il debug --------------------------------------------------------------
    // Metodo di debug, serve a disegnare a schermo la tilemap.
    // Disegna solo i bordi della tilemap quando le tile sono libere,
    // mentre riempe la tile quando è occupata
    public void drawTilemap(Canvas c) {
        for (int i = 0; i < mapSize.y; i++) {
            for (int j = 0; j < mapSize.x; j++) {
                // Controlla che la tile che sta disegnando a schermo sia libera
                // e, se lo è, la disegna a schermo con lo stile con riempimento
                int tile = tileMap.get(i * mapSize.x + j);
                if (tile == 1) {
                    c.drawRect(j * tileSize, i * tileSize,
                            (j * tileSize) + tileSize,
                            (i * tileSize) + tileSize,
                            occupiedTilePaint);
                }
                // Se non lo è, usa lo stile senza riempimento
                else {
                    c.drawRect(j * tileSize, i * tileSize,
                            (j * tileSize) + tileSize,
                            (i * tileSize) + tileSize,
                            tileGridPaint);
                }
            }

        }
    }
}
