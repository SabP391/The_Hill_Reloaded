package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

public class SunnyPointsCounter {
    private PlayTime playTime;
    private Bitmap sunnyPointsIcon;
    private int sunnyPoints;
    private Paint textPaint;
    private Paint playTimePaint;

    public SunnyPointsCounter(TileMap map, Context context){
        Point sunIconSize = new Point((int)(map.getTileSize() / 1.5) , (int)(map.getTileSize() / 1.5));
        sunnyPointsIcon = GameAssets.getInstance(context).getSunnyPointsIcon(sunIconSize);
        sunnyPoints = GameManager.getInstance().getSunnyPoints();
        playTime = GameManager.getInstance().getPlayTime();
        textPaint = new Paint();
        textPaint.setTextSize(75);
        textPaint.setColor(Color.argb(175,190,122,61));
        playTimePaint = new Paint();
        playTimePaint.setTextSize(50);
        playTimePaint.setColor(Color.argb(175,190,122,61));
    }

    public void draw(Canvas c){
        c.drawBitmap(sunnyPointsIcon, 15, 15, null);
        c.drawText(String.valueOf(sunnyPoints), (sunnyPointsIcon.getWidth()+21), (sunnyPointsIcon.getHeight()/2)+42, textPaint);
        c.drawText(playTime.toString(), (sunnyPointsIcon.getWidth()+250), (sunnyPointsIcon.getHeight()/2)+42, playTimePaint);
    }

    public void updateCounter(int newSunnyPoints){
        sunnyPoints = newSunnyPoints;
    }
}
