package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class SunnyPointsCounter {
    private TileMap map;
    private Bitmap sunnyPointsIcon;
    private int sunnyPoints;
    private Rect background;
    private Paint backgroundColor;
    private Paint textPaint;

    public SunnyPointsCounter(TileMap map, Context context){
        Point sunIconSize = new Point((int)map.getTileSize() / 2 , (int)map.getTileSize() / 2);
        sunnyPointsIcon = GameAssets.getInstance(context).getSunnyPointsIcon(sunIconSize);
        sunnyPoints = GameManager.getInstance().getSunnyPoints();
        background = new Rect(0, 0, sunIconSize.x, sunIconSize.y);
        backgroundColor = new Paint();
        backgroundColor.setColor(Color.WHITE);
        textPaint = new Paint();
    }

    public void draw(Canvas c){
        c.drawRect(background, backgroundColor);
        c.drawBitmap(sunnyPointsIcon, 0, 0, null);
        c.drawText(String.valueOf(sunnyPoints), sunnyPointsIcon.getWidth(), sunnyPointsIcon.getHeight()/2, textPaint);
    }
}
