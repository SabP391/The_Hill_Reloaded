package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

// Classe per disegnare la bubble di feedback sulle centrali
public class FeedbackBubble {
    private Bitmap bubble;
    private Bitmap unitPointsIcon;
    private Bitmap sunnyPointsIcon;
    private Bitmap textPaint;
    private Point position;
    private boolean hasToBeDrawn = false;
    private IconToBeDrawn iconToBeDrawn = IconToBeDrawn.UNIT;

    public FeedbackBubble(Point bubbleSize, Context context, Point position){
        Point innerIconsSize = new Point(bubbleSize.x/ 2, bubbleSize.y/2);
        bubble = GameAssets.getInstance(context).getBubbleBitmap(bubbleSize);
        unitPointsIcon = GameAssets.getInstance(context).getUnitPointsIcon(innerIconsSize);
        sunnyPointsIcon = GameAssets.getInstance(context).getSunnyPointsIcon(innerIconsSize);
        this.position = position;
    }

    public void drawBubble(Canvas c, long currentTime, int pointsToAdd){
        c.drawBitmap(bubble, position.x, position.y, null);
    }
}
