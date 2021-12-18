package com.example.thehillreloaded;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.Collections;

public class TileMap {
    // Membri della classe -------------------------------------------------------------------------
    // Dimensione delle tile. Essendo le tile quadrate,
    // viene salvato un solo valore per altezza e larghezza
    public float tileSize;
    // Numero di tile che formeranno la mappa
    public Point mapSize;
    // La tileMap vera è propria viene salvata in un ArrayList
    // di interi, in modo da poter ridurre le coordinate delle tile
    // ad una sola dimensione
    public ArrayList<Integer> tileMap;
    // Variabili necessarie per disegnare la tileMap in fase di debug
    private Paint tileGridPaint;
    private Paint occupiedTilePaint;

    // Costruttore della classe, prende in input il numero di tile
    // orizzontali richieste e la dimensione dello schermo
    // e inizializza la tilemap
    public TileMap(int horizontalTileCount, Point screenSize) {
        mapSize = new Point();
        tileSize = (float) screenSize.x / horizontalTileCount;
        this.mapSize.x = horizontalTileCount + 1;
        this.mapSize.y = Math.round((screenSize.y / tileSize));
        tileMap = new ArrayList<Integer>(Collections.nCopies(mapSize.x * mapSize.y, 0));
        tileGridPaint = new Paint();
        tileGridPaint.setStyle(Paint.Style.STROKE);
        occupiedTilePaint = new Paint();
        occupiedTilePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

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
