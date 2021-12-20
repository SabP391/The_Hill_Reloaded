package com.example.thehillreloaded;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Binder;
import android.os.IBinder;

public class SoundEffectService extends Service {
    //Binder per clients
    private IBinder binder = new LocalBinder();

    private AudioAttributes audioAttributes;
    final int SOUND_POOL_MAX = 1;
    private SoundPool soundPool;

    //track individuate tramite int
    private int track1;

    @Override
    public void onCreate() {
        super.onCreate();

        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(SOUND_POOL_MAX)
                .build();

        //LOAD TRACKS HERE
        track1 = soundPool.load(getApplicationContext(), R.raw.button_track, 1);
    }

    //Classe usata per client binder
    public class LocalBinder extends Binder {
        SoundEffectService getService(){
            //Restituisce l'istanza del Service per permettere l'uso dei metodi pubblici
            return SoundEffectService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) { return binder; }

    //qui sotto sono da mettere i metodi da chiamare per far partire il suono
    //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate);
    public void suonoBottoni() {
        soundPool.play(track1, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}