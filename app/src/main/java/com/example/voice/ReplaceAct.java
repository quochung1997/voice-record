package com.example.voice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReplaceAct extends AppCompatActivity {
    String path, text;
    Button doneBtn, startBtn;
    TextView txtTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace);

        initData();
        initComponents();
        actionPerformed();
    }

    void initData() {
        Intent thisIntent = getIntent();
        path = thisIntent.getStringExtra("path");
        text = thisIntent.getStringExtra("text");
    }

    void initComponents() {
        doneBtn = findViewById(R.id.replaceAct_doneBtn);
        startBtn = findViewById(R.id.replaceAct_startBtn);
        txtTxt = findViewById(R.id.replaceAct_txtTxt);
    }

    void actionPerformed() {
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReplaceRecordAsyn asyn = new ReplaceRecordAsyn(ReplaceAct.this, path);
                asyn.execute();
            }
        });
    }
}
