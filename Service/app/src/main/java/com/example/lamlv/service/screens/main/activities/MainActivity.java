package com.example.lamlv.service.screens.main.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.lamlv.service.R;
import com.example.lamlv.service.Utils.Utils;
import com.example.lamlv.service.services.PlaySongService;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    public boolean isBound = false;

    private ImageButton img_btn_music, img_btn_fast_rewind, img_btn_fast_forward;
    private SeekBar seekbar_music;
    private TextView tv_duration, tv_current_position;
    private TextView tv_seekbar_progress, tv_current_seekbar_position, tv_total_seekbar_position;

    private PlaySongService playSongService;
    private ServiceConnection connection;

    private Handler mHandler;
    private Runnable mUpdateTimeTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        btnSetOnClick();
        setSeekBarEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    //region init View
    private void initView() {

        this.img_btn_music = findViewById(R.id.img_btn_music);
        this.img_btn_fast_forward = findViewById(R.id.img_btn_fast_forward);
        this.img_btn_fast_rewind = findViewById(R.id.img_btn_fast_rewind);

        this.seekbar_music = findViewById(R.id.seekbar_music);

        this.tv_duration = findViewById(R.id.tv_duration);
        this.tv_current_position = findViewById(R.id.tv_current_position);

        this.tv_seekbar_progress = findViewById(R.id.tv_seekbar_progress);
        this.tv_current_seekbar_position = findViewById(R.id.tv_current_seekbar_position);
        this.tv_total_seekbar_position = findViewById(R.id.tv_total_seekbar_position);
    }
    //endregion

    //region initData
    private void initData() {

        this.mHandler = new Handler();

        Intent intent = new Intent(MainActivity.this, PlaySongService.class);

        this.connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                PlaySongService.MyBinder binder = (PlaySongService.MyBinder)service;
                playSongService = binder.getService();
                isBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isBound = false;
            }
        };
        bindService(intent,connection, Context.BIND_AUTO_CREATE);
    }
    //endregion

    //region Button Click Event
    public void btnSetOnClick() {

        //Play and Pause Music
        this.img_btn_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable.ConstantState currentBackground = img_btn_music.getBackground().getConstantState();
                final Drawable.ConstantState playDrawable = getResources().getDrawable(R.drawable.ic_play_circle_outline_black_24dp).getConstantState();

                assert currentBackground != null;
                if(currentBackground.equals(playDrawable)) {
                    runnable();
                    img_btn_music.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp);

                    playSongService.play();

                    mHandler.postDelayed(mUpdateTimeTask, 100);

                }
                else {
                    playSongService.pause();

                    Log.d(TAG, String.valueOf(playSongService.getCurrentPosition()));
                    img_btn_music.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
                    Log.d(TAG, "pause action");
                }
            }
        });

        //fast forward button
        this.img_btn_fast_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = playSongService.getCurrentPosition();
                int fastforward = currentPosition + 5000;
                playSongService.seekto(fastforward);
            }
        });

        //fast rewind button
        this.img_btn_fast_rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = playSongService.getCurrentPosition();
                if(currentPosition >5000) {
                    int fastrewind = currentPosition - 5000;
                    playSongService.seekto(fastrewind);
                }
                else
                {
                    playSongService.seekto(0);
                }
            }
        });

    }

    //endregion

    //region runable update seekbar progress
    public void runnable()
    {
        mUpdateTimeTask = new Runnable() {
            @Override
            public void run() {

                int totalPosition = playSongService.getDuration();
                int currentPosition = playSongService.getCurrentPosition();
                int delta = totalPosition - currentPosition;
                Log.d(TAG, "run: " + delta);

                tv_duration.setText(Utils.milliSecondsToTimer(totalPosition));
                tv_current_position.setText(Utils.milliSecondsToTimer(currentPosition));

                int progress = Utils.getProgressPercentage(currentPosition, totalPosition);
                seekbar_music.setProgress(progress);

                tv_seekbar_progress.setText(progress + "%");
                tv_current_seekbar_position.setText(String.valueOf(currentPosition));
                tv_total_seekbar_position.setText(String.valueOf(totalPosition));

                // fix bug current position not equal total position
                if (delta < 500) {

                    img_btn_music.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
                    seekbar_music.setProgress(0);
                    tv_current_position.setText("0:00");
                    mHandler.removeCallbacks(this);
                    Log.d(TAG, "run: remove");
                }
                else {
                    mHandler.postDelayed(mUpdateTimeTask, 100);
                }
            }
        };
    }
    //endregion

    //region Seekbar event
    public void setSeekBarEvent()
    {
        seekbar_music.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                int totalPosition = playSongService.getDuration();
                int currentPositionAfterSeek = Utils.progressToTimer(seekBar.getProgress(), totalPosition);

                Log.d("Position after drag", String.valueOf(currentPositionAfterSeek));
                Log.d("Total position", String.valueOf(totalPosition));

                playSongService.seekto(currentPositionAfterSeek);

                mHandler.postDelayed(mUpdateTimeTask, 100);
            }
        });
    }
    //endregion

}
