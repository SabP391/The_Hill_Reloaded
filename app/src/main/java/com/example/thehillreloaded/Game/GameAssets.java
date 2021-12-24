package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;

import androidx.core.content.ContextCompat;

import com.example.thehillreloaded.R;

import java.util.Random;

public class GameAssets {
    private static GameAssets instance;
    private static Context context;
    private Bitmap gameBackGround;
    private Bitmap vetro[];
    private Bitmap alluminio[];
    private Bitmap acciaio[];
    private Bitmap plastica[];
    private Bitmap carta[];
    private Bitmap eWaste[];
    private Bitmap umido[];
    private Bitmap misto;
    private Random rand;

    private GameAssets(){
        gameBackGround = getBitmap(context, R.drawable.ic_bg_ingame);

        rand = new Random();
        // Creazione dell'array di sprite per il vetro ---------------------------------------------
        vetro = new Bitmap[3];
            vetro[0] = getBitmap(context, R.drawable.asset_vetro_1);
            vetro[1] = getBitmap(context, R.drawable.asset_vetro_2);
            vetro[2] = getBitmap(context, R.drawable.asset_vetro_3);
        // Creazione dell'array di sprite per il alluminio -----------------------------------------
        alluminio = new Bitmap[3];
            alluminio[0] = getBitmap(context, R.drawable.asset_alluminio_1);
            alluminio[1] = getBitmap(context, R.drawable.asset_alluminio_2);
            alluminio[2] = getBitmap(context, R.drawable.asset_alluminio_3);
        // Creazione dell'array di sprite per il acciao --------------------------------------------
        acciaio = new Bitmap[2];
            acciaio[0] = getBitmap(context, R.drawable.asset_acciaio_1);
            acciaio[1] = getBitmap(context, R.drawable.asset_acciaio_2);
        // Creazione dell'array di sprite per il plastica ------------------------------------------
        plastica =  new Bitmap[4];
            plastica[0] = getBitmap(context, R.drawable.asset_plastica_1);
            plastica[1] = getBitmap(context, R.drawable.asset_plastica_2);
            plastica[2] = getBitmap(context, R.drawable.asset_plastica_3);
            plastica[3] = getBitmap(context, R.drawable.asset_plastica_4);
        // Creazione dell'array di sprite per il carta ---------------------------------------------
        carta = new Bitmap[2];
            carta[0] = getBitmap(context, R.drawable.asset_carta_1);
            carta[1] = getBitmap(context, R.drawable.asset_carta_2);
        // Creazione dell'array di sprite per il ewaste---------------------------------------------
        eWaste = new Bitmap[2];
            eWaste[0] = getBitmap(context, R.drawable.asset_ewaste_1);
            eWaste[1] = getBitmap(context, R.drawable.asset_ewaste_2);
        // Creazione dell'array di sprite per il umido ---------------------------------------------
        umido = new Bitmap[2];
            umido[0] = getBitmap(context, R.drawable.asset_umido_1);
            umido[1] = getBitmap(context, R.drawable.asset_umido_2);
        // Creazione di un bitmap per il misto------------------------------------------------------
        misto = getBitmap(context, R.drawable.asset_misto);
    };

    public static GameAssets getInstance(Context context2){
        if(instance == null){
            context = context2;
            instance = new GameAssets();
        }
        return instance;
    }

    // Metodi per ritornare le sprite --------------------------------------------------------------
    Bitmap getMixed(Point size){
        return(Bitmap.createScaledBitmap(misto, size.x, size.y, false));
    }

    Bitmap getRandGlass(Point size){
        return(Bitmap.createScaledBitmap(vetro[rand.nextInt(vetro.length)], size.x, size.y, false));
    }

    Bitmap getRandAl(Point size){
        return(Bitmap.createScaledBitmap(alluminio[rand.nextInt(alluminio.length)], size.x, size.y, false));
    }

    Bitmap getRandSteel(Point size){
        return(Bitmap.createScaledBitmap(acciaio[rand.nextInt(acciaio.length)], size.x, size.y, false));
    }

    Bitmap getRandPlastic(Point size){
        return(Bitmap.createScaledBitmap(plastica[rand.nextInt(plastica.length)], size.x, size.y, false));
    }

    Bitmap getRandPaper(Point size){
        return(Bitmap.createScaledBitmap(carta[rand.nextInt(carta.length)], size.x, size.y, false));
    }

    Bitmap getRandEWaste(Point size){
        return(Bitmap.createScaledBitmap(eWaste[rand.nextInt(eWaste.length)], size.x, size.y, false));
    }

    Bitmap getRandCompost(Point size){
        return(Bitmap.createScaledBitmap(umido[rand.nextInt(umido.length)], size.x, size.y, false));
    }

    Bitmap getDefaultGlass(Point size){
        return(Bitmap.createScaledBitmap(vetro[0], size.x, size.y, false));
    }

    Bitmap getDefaultAl(Point size){
        return(Bitmap.createScaledBitmap(alluminio[0], size.x, size.y, false));
    }

    Bitmap getDefaultSteel(Point size){
        return(Bitmap.createScaledBitmap(acciaio[0], size.x, size.y, false));
    }

    Bitmap getDefaultPlastic(Point size){
        return(Bitmap.createScaledBitmap(plastica[0], size.x, size.y, false));
    }

    Bitmap getDefaultPaper(Point size){
        return(Bitmap.createScaledBitmap(carta[0], size.x, size.y, false));
    }

    Bitmap getDefaultEWaste(Point size){
        return(Bitmap.createScaledBitmap(eWaste[0], size.x, size.y, false));
    }

    Bitmap getDefaultCompost(Point size){
        return(Bitmap.createScaledBitmap(umido[0], size.x, size.y, false));
    }


    Bitmap getGameBackGround(Point size){
        return Bitmap.createScaledBitmap(gameBackGround, size.x, size.y, false);
    }

    // Metodi per convertire i file vettoriali in bitmap--------------------------------------------
    private Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    private Bitmap getBitmap(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable instanceof BitmapDrawable) {
            return BitmapFactory.decodeResource(context.getResources(), drawableId);
        } else if (drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }
}
