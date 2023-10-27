package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;

import com.example.thehillreloaded.R;

public class TutorialOverlay {
    private Bitmap arrowImage;
    private Paint textPaint;
    private Context context;
    private TileMap map;


    public TutorialOverlay(TileMap map, Context context){
        Point arrowSize = new Point((int)(map.getTileSize() * 1.5) , (int)(map.getTileSize()));
        this.context = context;
        this.map = map;
        arrowImage = GameAssets.getInstance(context).getArrowBitmap(arrowSize);
        textPaint = new Paint();
        textPaint.setTextSize(40);
        textPaint.setColor(Color.argb(255, 255, 255, 255));
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
    }

    public void draw(Canvas c){
        switch (GameManager.getInstance().getTutorialState()){
            case STARTED:{
                c.drawText((String)context.getText(R.string.benvenuto_tut), 15, 150, textPaint);
                drawTextMultiline((String)context.getText(R.string.dovrai_tut), 15,200, c);
                c.drawText((String)context.getText(R.string.proviamo_tut), 15, 300, textPaint);
                Point arrowPosition = new Point((int)map.getTileSize() + 40,
                        (int) (map.getTileSize() * map.getHorizontalTileCount() - (map.getTileSize() - 15)));
                c.drawBitmap(arrowImage,arrowPosition.x, arrowPosition.y, null);
                drawTextMultiline((String)context.getText(R.string.apri_menu_tut),
                        arrowPosition.x + 20,
                        arrowPosition.y - 30,
                        c);
                break;
            }
            case GLASS_BUILT:{
                drawTextMultiline((String) context.getText(R.string.trascina_tut),
                        25, 400, c);
                break;
            }
            case FIRST_ITEM_RECYCLED:{
                drawTextMultiline((String) context.getText(R.string.gestisci_tut),
                        25, 400, c) ;
                break;
            }
            case UPGRADE_BUILT:{
                int secondTextPosition = (int)((map.getFirstTileOfTheHill() * map.getTileSize())
                        + (map.getTileSize() * map.getNumberOfTileSOfTheHill())
                        + 20);

                drawTextMultiline((String) context.getText(R.string.sunny_tut),
                        25, 400, c) ;
                drawTextMultiline((String) context.getText(R.string.inc_tut),
                        secondTextPosition, 200, c) ;
                drawTextMultiline((String) context.getText(R.string.inc_tut_2),
                        secondTextPosition, 700, c) ;
            }
            default: break;
        }

    }

    private void drawTextMultiline(String s, int x, int y, Canvas c){
        for (String line : s.split("\n")){
            c.drawText(line, x, y, textPaint);
            y += textPaint.descent() - textPaint.ascent();
        }
    }
}


