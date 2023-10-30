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

// Classe che gestisce il caricamento delle immagini
// attraverso questa classe è possibile convertire immagini vettoriali
// in bitmap e ritornarli scalati in base alle esigenze
public class GameAssets {
    private static GameAssets instance;
    private static Context context;
    // Icone della UI
    private Bitmap sunnyPointsIcon;
    private Bitmap gameBackGround;
    private Bitmap misto;
    private Bitmap unitPointsIcon;
    private Bitmap wearWarningIcon;
    private Bitmap buffIcon;
    private Bitmap debuffIcon;

    // Sprite delle centrali
    private Bitmap inceneritore;
    private Bitmap centraleVetro;
    private Bitmap centraleAlluminio;
    private Bitmap centraleAcciaio;
    private Bitmap centralePlastica;
    private Bitmap centraleCarta;
    private Bitmap centraleEWaste;
    private Bitmap centraleUmido;

    // Sprite degli oggetti di gioco
    private Bitmap radioattivo;
    private Bitmap vetro[];
    private Bitmap alluminio[];
    private Bitmap acciaio[];
    private Bitmap plastica[];
    private Bitmap carta[];
    private Bitmap eWaste[];
    private Bitmap umido[];

    // Sprite degli oggetti con buff e debuff
    private Bitmap vetroBuff;
    private Bitmap vetroDebuff;
    private Bitmap alluminioBuff;
    private Bitmap alluminioDebuff;
    private Bitmap acciaioBuff;
    private Bitmap acciaioDebuff;
    private Bitmap plasticaBuff;
    private Bitmap plasticaDebuff;
    private Bitmap cartaBuff;
    private Bitmap cartaDebuff;
    private Bitmap eWasteBuff;
    private Bitmap eWasteDebuff;
    private Bitmap compostBuff;
    private Bitmap compostDebuff;

    // Bitmap freccia tutorial
    private Bitmap arrowImg;
    // Bitmap per le bubble
    private Bitmap bubble;

    private Random rand;

    // Costruttore della classe
    private GameAssets(){
        // Creazione delle sprite per background e icone -------------------------------------------
        gameBackGround = getBitmap(context, R.drawable.ic_bg_ingame);
        sunnyPointsIcon = getBitmap(context, R.drawable.ic_sole);
        unitPointsIcon = getBitmap(context, R.drawable.ic_unit_points);
        wearWarningIcon = getBitmap(context, R.drawable.ic_exclamation_mark);
        buffIcon = getBitmap(context, R.drawable.ic_buff);
        debuffIcon = getBitmap(context, R.drawable.ic_debuff);

        // Creazione delle sprite delle centrali ---------------------------------------------------
        inceneritore = getBitmap(context, R.drawable.asset2_inceneritore);
        centraleVetro = getBitmap(context, R.drawable.asset2_fab_verde_vetro);
        centraleAlluminio = getBitmap(context, R.drawable.asset2_fab_viola_alluminio);
        centraleAcciaio = getBitmap(context, R.drawable.asset2_fab_celeste_acciaio);
        centralePlastica = getBitmap(context, R.drawable.asset2_fab_giallo_plastica);
        centraleCarta = getBitmap(context, R.drawable.asset2_fab_blu_carta);
        centraleEWaste = getBitmap(context, R.drawable.asset2_fab_rosa_ewaste);
        centraleUmido = getBitmap(context, R.drawable.asset2_fab_marrone_umido);

        radioattivo = getBitmap(context, R.drawable.asset_radioattivo);
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
        // Creazione bitmap per gli oggetti di buff/debuff -----------------------------------------
        vetroBuff = getBitmap(context, R.drawable.asset_vetro_buff);
        vetroDebuff = getBitmap(context, R.drawable.asset_vetro_debuff);
        alluminioBuff = getBitmap(context, R.drawable.asset_alluminio_buff);
        alluminioDebuff = getBitmap(context, R.drawable.asset_alluminio_debuff);
        acciaioBuff = getBitmap(context, R.drawable.asset_acciaio_buff);
        acciaioDebuff = getBitmap(context, R.drawable.asset_acciaio_debuff);
        plasticaBuff = getBitmap(context, R.drawable.asset_plastica_buff);
        plasticaDebuff = getBitmap(context, R.drawable.asset_plastica_debuff);
        cartaBuff = getBitmap(context, R.drawable.asset_carta_buff);
        cartaDebuff = getBitmap(context, R.drawable.asset_carta_debuff);
        eWasteBuff = getBitmap(context, R.drawable.asset_ewaste_buff);
        eWasteDebuff = getBitmap(context, R.drawable.asset_ewaste_debuff);
        compostBuff = getBitmap(context, R.drawable.asset_umido_buff);
        compostDebuff = getBitmap(context, R.drawable.asset_umido_debuff);

        arrowImg = getBitmap(context, R.drawable.arrow);
        bubble = getBitmap(context, R.drawable.bubble);
    };

    public static GameAssets getInstance(Context context2){
        if(instance == null){
            context = context2;
            instance = new GameAssets();
        }
        return instance;
    }

    // Metodi per ritornare le sprite --------------------------------------------------------------
    Bitmap getGameBackGround(Point size){return Bitmap.createScaledBitmap(gameBackGround, size.x, size.y, false);}

    Bitmap getBuffIcon(Point size){return Bitmap.createScaledBitmap(buffIcon, size.x, size.y, false);}

    Bitmap getDebuffIcon(Point size){return Bitmap.createScaledBitmap(debuffIcon, size.x, size.y, false);}

    Bitmap getGlassUnit(Point size){return Bitmap.createScaledBitmap(centraleVetro, size.x, size.y, false);}

    Bitmap getAlUnit(Point size){return Bitmap.createScaledBitmap(centraleAlluminio, size.x, size.y, false);}

    Bitmap getSteelUnit(Point size){return Bitmap.createScaledBitmap(centraleAcciaio, size.x, size.y, false);}

    Bitmap getPlasticUnit(Point size){return Bitmap.createScaledBitmap(centralePlastica, size.x, size.y, false);}

    Bitmap getPaperUnit(Point size){return Bitmap.createScaledBitmap(centraleCarta, size.x, size.y, false);}

    Bitmap getEwasteUnit(Point size){return Bitmap.createScaledBitmap(centraleEWaste, size.x, size.y, false);}

    Bitmap getCompostUnit(Point size){return Bitmap.createScaledBitmap(centraleUmido, size.x, size.y, false);}

    Bitmap getIncineratorUnit(Point size){return Bitmap.createScaledBitmap(inceneritore, size.x, size.y, false);}

    Bitmap getSunnyPointsIcon(Point size){return(Bitmap.createScaledBitmap(sunnyPointsIcon, size.x, size.y, false));}

    Bitmap getUnitPointsIcon(Point size){return(Bitmap.createScaledBitmap(unitPointsIcon, size.x, size.y, false));}

    Bitmap getWearWarningIcon(Point size){return(Bitmap.createScaledBitmap(wearWarningIcon, size.x, size.y, false));}

    Bitmap getMixed(Point size){
        return(Bitmap.createScaledBitmap(misto, size.x, size.y, false));
    }

    Bitmap getRadioactive(Point size){ return(Bitmap.createScaledBitmap(radioattivo, size.x, size.y, false));}

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

    Bitmap getGlassBuff(Point size){
        return(Bitmap.createScaledBitmap(vetroBuff, size.x, size.y, false));
    }

    Bitmap getGlassDebuff(Point size){
        return(Bitmap.createScaledBitmap(vetroDebuff, size.x, size.y, false));
    }

    Bitmap getAlBuff(Point size){
        return(Bitmap.createScaledBitmap(alluminioBuff, size.x, size.y, false));
    }

    Bitmap getAlDebuff(Point size){
        return(Bitmap.createScaledBitmap(alluminioDebuff, size.x, size.y, false));
    }

    Bitmap getSteelBuff(Point size){
        return(Bitmap.createScaledBitmap(acciaioBuff, size.x, size.y, false));
    }

    Bitmap getSteelDebuff(Point size){
        return(Bitmap.createScaledBitmap(acciaioDebuff, size.x, size.y, false));
    }

    Bitmap getPlasticBuff(Point size){
        return(Bitmap.createScaledBitmap(plasticaBuff, size.x, size.y, false));
    }

    Bitmap getPlasticDebuff(Point size){
        return(Bitmap.createScaledBitmap(plasticaDebuff, size.x, size.y, false));
    }

    Bitmap getPaperBuff(Point size){
        return(Bitmap.createScaledBitmap(cartaBuff, size.x, size.y, false));
    }

    Bitmap getPaperDebuff(Point size){
        return(Bitmap.createScaledBitmap(cartaDebuff, size.x, size.y, false));
    }

    Bitmap getEWasteBuff(Point size){
        return(Bitmap.createScaledBitmap(eWasteBuff, size.x, size.y, false));
    }

    Bitmap getEwasteDebuff(Point size){
        return(Bitmap.createScaledBitmap(eWasteDebuff, size.x, size.y, false));
    }

    Bitmap getCompostBuff(Point size){
        return(Bitmap.createScaledBitmap(compostBuff, size.x, size.y, false));
    }

    Bitmap getCompostDebuff(Point size){
        return(Bitmap.createScaledBitmap(compostDebuff, size.x, size.y, false));
    }

    Bitmap getArrowBitmap(Point size){
        return(Bitmap.createScaledBitmap(arrowImg, size.x, size.y, false));
    }

    Bitmap getBubbleBitmap(Point size){
        return(Bitmap.createScaledBitmap(bubble, size.x, size.y, false));
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
