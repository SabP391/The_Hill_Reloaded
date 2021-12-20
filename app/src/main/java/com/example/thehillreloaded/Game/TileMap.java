package com.example.thehillreloaded.Game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

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
    // Variabili necessarie per disegnare la tileMap in fase di debug
    private final Paint tileGridPaint;
    private final Paint occupiedTilePaint;

    // Costruttore della classe, prende in input il numero di tile
    // orizzontali richieste (cioé il numero di righe)
    // e la dimensione dello schermo
    // e inizializza la tilemap
    public TileMap(int horizontalTileCount, Point screenSize) {
        mapSize = new Point();
        tileSize = (float) ((screenSize.y / horizontalTileCount) - 2);
        this.mapSize.y = horizontalTileCount;
        this.mapSize.x = Math.round((screenSize.x / tileSize));
        tileMap = new ArrayList<Integer>(Collections.nCopies(mapSize.x * mapSize.y, 0));
        // Variabili necessare per il metodo drawTilemap,
        // utili principalmente in fase di debug
        tileGridPaint = new Paint();
        tileGridPaint.setStyle(Paint.Style.STROKE);
        occupiedTilePaint = new Paint();
        occupiedTilePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    // Metodi utili --------------------------------------------------------------------------------
    public int getNextTileIndex(int currentTile){
        return currentTile + mapSize.x;
    }

    public boolean isNextTileFree(int currentTile){
        return (tileMap.get((currentTile + mapSize.x)) == 0);
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
