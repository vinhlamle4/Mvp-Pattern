package com.example.lamlv.service.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.lamlv.service.R;

public class PlaySongService extends Service {

    private static String TAG = "PlaySongService";

    private MediaPlayer mediaPlayer;
    private IBinder iBinder;

    public PlaySongService() {
    }

    public class MyBinder extends Binder {
        public PlaySongService getService()
        {
            return PlaySongService.this;
        }
    }

    //region Override Method (Lifecycle Service)
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tule_fearless);
        Log.d(TAG, "On Create");
        iBinder = new MyBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "On Bind");
        return iBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "On Unbind");
        stop();
        mediaPlayer.release();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Tắt nhạc
        Log.d(TAG, "On Destroy");
    }
    //endregion

    public void play() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            Log.d(TAG, "play: ");
        }
    }

    public void pause() {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            Log.d(TAG, "pause ");
        }
    }

    public void stop() {
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            Log.d(TAG, "stop ");
        }
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public void seekto(int position)
    {
        mediaPlayer.seekTo(position);
    }
}
