package com.example.voice;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PlayingAct extends AppCompatActivity {

    TextView pathTxt;
    Button backBtn, playBtn;
    String path;

    int num;

    PlayingAsync async;

    SoundPool soundPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        init();
        buttonsActionPerform();

        soundPool = new SoundPool(4, AudioManager.STREAM_VOICE_CALL, 0);
        num = soundPool.load(path, 1);
    }

    void init() {
        pathTxt = findViewById(R.id.playingAct_pathTxt);
        backBtn = findViewById(R.id.playingAct_backBtn);
        playBtn = findViewById(R.id.playingAct_playBtn);

        path = getIntent().getStringExtra("path");

        pathTxt.setText(getFilenameFromPath());
    }

    void buttonsActionPerform() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundPool.play(num, 0.99f, 0.99f, 0, 0, 1);

                async = new PlayingAsync(PlayingAct.this);
                async.execute();
            }
        });
    }

    String getFilenameFromPath() {
        String str = "";
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == '/') {
                str = "";
            } else str += path.charAt(i);
        }

        return str;
    }


}
