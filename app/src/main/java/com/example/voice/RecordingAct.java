package com.example.voice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.voice.models.User;
import com.example.voice.models.VoiceRecord;
import com.example.voice.recordhelper.WavRecorder;
import com.example.voice.sqlites.UserSqliteDao;
import com.example.voice.sqlites.VoiceRecordSqliteDao;
import com.example.voice.threads.WaitingRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RecordingAct extends AppCompatActivity {

    final VoiceRecordSqliteDao recordDao = new VoiceRecordSqliteDao(this);
    final UserSqliteDao userDao = new UserSqliteDao(this);

    ArrayList<VoiceRecord> records;
    User user;
    String userId;

    Button startBtn, replaceBtn, replayBtn;
    TextView txtTxt;

    HashMap<String, Integer> map;

    String outputFile;

    SoundPool soundPool;
    int soundNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);


        startBtn = findViewById(R.id.recordingAct_startBtn);
        replayBtn = findViewById(R.id.recordingAct_replayBtn);
        replaceBtn = findViewById(R.id.recordingAct_replaceBtn);
        txtTxt = findViewById(R.id.recordingAct_txtTxt);

        replayBtn.setEnabled(false);
        replaceBtn.setEnabled(false);
        initRecordInfo();


        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nextWord()) {
                    RecordAsync recordAsync = new RecordAsync(RecordingAct.this, outputFile);
                    recordAsync.execute();
                }
            }
        });

        replaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecordAsync async = new RecordAsync(RecordingAct.this, outputFile);
                async.execute();
            }
        });

        replayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    soundPool = new SoundPool(4, AudioManager.STREAM_VOICE_CALL, 0);
                    soundNumber = soundPool.load(outputFile, 1);
                    Log.d("path", outputFile);
                    soundPool.play(soundNumber, 0.99f, 0.99f, 0, 0, 1);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Playing errors", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    boolean nextWord() {
        if (records.size() >= VoiceRecord.labels.length*20) {
            Toast.makeText(getApplicationContext(), "ENOUGH RECORD", Toast.LENGTH_LONG).show();
            return false;
        } else {
            int labelIndex;

            while (true) {
                Random rand = new Random();
                labelIndex = rand.nextInt(VoiceRecord.labels.length);

                if (map.get(VoiceRecord.labels[labelIndex]) < 20) break;
            }

            String label = VoiceRecord.labels[labelIndex];

            int num = map.get(label)+1;
            txtTxt.setText(label);
            map.put(label, num);

            VoiceRecord record = new VoiceRecord();
            record.setUser(new User(userId, user.getGender()));
            record.setLabel(labelIndex);

            int recordId = recordDao.insert(record);

            record.setNumber(recordId);

            records.add(record);

            outputFile = record.getPath();

            return true;

        }
    }


    void initRecordInfo() {
        Intent thisIntent = getIntent();
        userId = thisIntent.getStringExtra("id");
        ArrayList<VoiceRecord> allRecords = recordDao.getAll();
        user = userDao.get(userId);
        records = new ArrayList<>();
        map = new HashMap<>();

        for (VoiceRecord record: allRecords) {
            if (record.getUser().getId().equals(userId))
                records.add(record);
        }

        for (String label : VoiceRecord.labels) {
            map.put(label, 0);
        }

        for (VoiceRecord record: records) {
            String tmLabel = record.getLabel();
            int num = map.containsKey(tmLabel) ? map.get(tmLabel) : 0;
            num++;
            map.put(tmLabel, num);
        }
    }

}
