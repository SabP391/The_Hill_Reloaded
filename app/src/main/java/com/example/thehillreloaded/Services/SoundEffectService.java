package com.example.thehillreloaded.Services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Binder;
import android.os.IBinder;

import com.example.thehillreloaded.R;

public class SoundEffectService extends Service {
    //Binder per clients
    private IBinder binder = new LocalBinder();

    private AudioAttributes audioAttributes;
    final int SOUND_POOL_MAX = 5;
    private SoundPool soundPool;

    //track individuate tramite int
    private int track1;
    private int track2;
    private int track3;
    private int track4;
    private int track5;

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
        track2 = soundPool.load(getApplicationContext(), R.raw.recycle_units_track, 1);
        track3 = soundPool.load(getApplicationContext(), R.raw.building_track, 1);
        track4 = soundPool.load(getApplicationContext(), R.raw.game_over_track, 1);
        track5 = soundPool.load(getApplicationContext(), R.raw.mission_success_track, 1);
    }

    //Classe usata per client binder
    public class LocalBinder extends Binder {
        public SoundEffectService getService(){
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
    public void suonoUnitaInFunzione() { soundPool.play(track2, 1.0f, 1.0f, 1, 0, 1.0f); }
    public void suonoCostruzioneUpgrade() { soundPool.play(track3, 1.0f, 1.0f, 1, 0, 1.0f); }
    public void suonoGameOver() { soundPool.play(track4, 1.0f, 1.0f, 1, 0, 1.0f); }
    public void suonoMissioneCompleta() { soundPool.play(track5, 1.0f, 1.0f, 1, 0, 1.0f); }
}