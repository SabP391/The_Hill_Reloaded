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
    private Paint textPaint;

    public SunnyPointsCounter(TileMap map, Context context){
        Point sunIconSize = new Point((int)(map.getTileSize() / 1.5) , (int)(map.getTileSize() / 1.5));
        sunnyPointsIcon = GameAssets.getInstance(context).getSunnyPointsIcon(sunIconSize);
        sunnyPoints = GameManager.getInstance().getSunnyPoints();
        textPaint = new Paint();
        textPaint.setTextSize(75);
        textPaint.setColor(Color.argb(175,190,122,61));
    }

    public void draw(Canvas c){
        c.drawBitmap(sunnyPointsIcon, 15, 15, null);
        c.drawText(String.valueOf(sunnyPoints), (sunnyPointsIcon.getWidth()+21), (sunnyPointsIcon.getHeight()/2)+42, textPaint);
    }

    public void updateCounter(int newSunnyPoints){
        sunnyPoints = newSunnyPoints;
    }
}
