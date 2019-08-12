package com.example.voice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RecordingAct extends AppCompatActivity {

    final VoiceRecordSqliteDao recordDao = new VoiceRecordSqliteDao(this);
    final UserSqliteDao userDao = new UserSqliteDao(this);

    ArrayList<VoiceRecord> records;
    User user;
    String userId;

    Button nextBtn, replaceBtn, replayBtn, startRecordBtn;
    TextView txtTxt;

    HashMap<String, Integer> map;

    String outputFile;

    String currentLabel;
    int labelIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);


        nextBtn = findViewById(R.id.recordingAct_nextBtn);
        replayBtn = findViewById(R.id.recordingAct_replayBtn);
        replaceBtn = findViewById(R.id.recordingAct_replaceBtn);
        startRecordBtn = findViewById(R.id.recordingAct_startRecordBtn);
        txtTxt = findViewById(R.id.recordingAct_txtTxt);

        replayBtn.setEnabled(false);
        replaceBtn.setEnabled(false);
        initRecordInfo();


        nextWord();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextWord();
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

                Intent toPlaying = new Intent(getApplicationContext(), PlayingAct.class);
                toPlaying.putExtra("path", outputFile);
                startActivity(toPlaying);

            }
        });

        startRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = map.get(currentLabel)+1;
                map.put(currentLabel, num);

                VoiceRecord record = new VoiceRecord();
                record.setUser(new User(userId, user.getGender()));
                record.setLabel(labelIndex);

                int recordId = recordDao.insert(record);

                record.setNumber(recordId);

                records.add(record);

                outputFile = record.getPath();

                startRecordBtn.setEnabled(false);

                RecordAsync recordAsync = new RecordAsync(RecordingAct.this, outputFile);
                recordAsync.execute();
            }
        });


    }


    boolean nextWord() {
        if (records.size() >= VoiceRecord.labels.length*20) {
            Toast.makeText(getApplicationContext(), "ENOUGH RECORD", Toast.LENGTH_LONG).show();
            return false;
        } else {
            while (true) {
                Random rand = new Random();
                labelIndex = rand.nextInt(VoiceRecord.labels.length);

                if (map.get(VoiceRecord.labels[labelIndex]) < 20) break;
            }

            currentLabel = VoiceRecord.labels[labelIndex];
            txtTxt.setText(currentLabel);

            startRecordBtn.setEnabled(true);
            replaceBtn.setEnabled(false);
            replayBtn.setEnabled(false);

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
