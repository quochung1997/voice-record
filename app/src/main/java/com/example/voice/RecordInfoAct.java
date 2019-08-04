package com.example.voice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.voice.models.User;
import com.example.voice.models.VoiceRecord;
import com.example.voice.sqlites.UserSqliteDao;
import com.example.voice.sqlites.VoiceRecordSqliteDao;

import java.io.File;
import java.util.ArrayList;

public class RecordInfoAct extends AppCompatActivity {

    ArrayList<VoiceRecord> records;
    VoiceRecord voiceRecord;

    final VoiceRecordSqliteDao recordDao = new VoiceRecordSqliteDao(this);
    final UserSqliteDao userDao = new UserSqliteDao(this);

    Button playBtn, deleteBtn;
    TextView labelTxt, idTxt, pathTxt;

    boolean isPlaying;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_info);

        initData();
        initComponents();


        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(voiceRecord.getPath());
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();
                        }
                    });
                    Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG)
                            .show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Playing Errors", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(voiceRecord.getPath());

                file.delete();

                recordDao.delete(voiceRecord.getNumber());

                finish();
            }
        });

    }

    void initData() {
        Intent thisIntent = getIntent();
        int recordId = thisIntent.getIntExtra("recordId", 0);

        records = recordDao.getAll();

        for (VoiceRecord vr: records) {
            if (vr.getNumber() == recordId) {
                voiceRecord = vr;
                break;
            }
        }

        User user = userDao.get(voiceRecord.getUser().getId());
        voiceRecord.setUser(user);

        isPlaying = false;
    }

    void initComponents() {
        playBtn = findViewById(R.id.recordInfo_playBtn);
        deleteBtn = findViewById(R.id.recordInfo_deleteBtn);

        labelTxt = findViewById(R.id.recordInfo_labelTxt);
        idTxt = findViewById(R.id.recordInfo_recordIdTxt);
        pathTxt = findViewById(R.id.recordInfo_pathTxt);

        labelTxt.setText(voiceRecord.getLabel());
        idTxt.setText(voiceRecord.getNumber()+"");
        pathTxt.setText(voiceRecord.getPath());
    }
}
