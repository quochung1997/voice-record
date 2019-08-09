package com.example.voice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
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

    Button playBtn, deleteBtn, replaceBtn;
    TextView labelTxt, idTxt, pathTxt;

    boolean isPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_info);

        initData();
        initComponents();
        setButtonEvents();

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
        replaceBtn = findViewById(R.id.recordInfo_replaceBtn);

        labelTxt = findViewById(R.id.recordInfo_labelTxt);
        idTxt = findViewById(R.id.recordInfo_recordIdTxt);
        pathTxt = findViewById(R.id.recordInfo_pathTxt);

        labelTxt.setText("Label: \""+voiceRecord.getLabel()+"\"");
        idTxt.setText("ID: "+voiceRecord.getNumber());
        pathTxt.setText("\""+voiceRecord.getFilename()+"\"");
    }

    void setButtonEvents() {
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toPlaying = new Intent(getApplicationContext(), PlayingAct.class);
                toPlaying.putExtra("path", voiceRecord.getPath());
                startActivity(toPlaying);
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

        replaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toReplace = new Intent(getApplicationContext(), ReplaceAct.class);
                toReplace.putExtra("path", voiceRecord.getPath());
                toReplace.putExtra("text", voiceRecord.getLabel());
                startActivity(toReplace);
            }
        });
    }
}
