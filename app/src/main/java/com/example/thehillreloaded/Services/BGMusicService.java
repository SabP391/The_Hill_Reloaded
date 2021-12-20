package com.example.thehillreloaded.Services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.example.thehillreloaded.R;

public class BGMusicService extends Service {
    //binder given to clients
    private IBinder binder = new LocalBinder();

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.bg_music_track);
        mediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.stop();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    public class LocalBinder extends Binder {
        BGMusicService getService() {
            return BGMusicService.this;
        }
    }
}