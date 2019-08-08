package com.example.voice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PlayingAct extends AppCompatActivity {

    TextView pathTxt;
    Button backBtn;
    String path;

    PlayingAsync async;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        init();
        buttonsActionPerform();

        async = new PlayingAsync(PlayingAct.this, path);
        async.execute();
    }

    void init() {
        pathTxt = findViewById(R.id.playingAct_pathTxt);
        backBtn = findViewById(R.id.playingAct_backBtn);

        path = getIntent().getStringExtra("path");

        pathTxt.setText(path);
    }

    void buttonsActionPerform() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
