package com.example.voice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.service.autofill.UserData;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.voice.models.User;
import com.example.voice.models.VoiceRecord;
import com.example.voice.recordhelper.WavRecorder;
import com.example.voice.sqlites.UserSqliteDao;
import com.example.voice.sqlites.VoiceRecordSqliteDao;
import com.example.voice.threads.CountingRunnable;
import com.example.voice.threads.RecordThr;
import com.example.voice.threads.WavRecorderRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RecordingAct extends AppCompatActivity {
    final VoiceRecordSqliteDao recordDao = new VoiceRecordSqliteDao(this);
    final UserSqliteDao userDao = new UserSqliteDao(this);

    ArrayList<VoiceRecord> records;
    User user;
    String userId;

    Button startBtn;
    TextView txtTxt;

    HashMap<String, Integer> map;

    String outputFile;

    WavRecorder recorder;

    boolean isRecording;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        isRecording = false;

        startBtn = findViewById(R.id.recordingAct_startBtn);
        txtTxt = findViewById(R.id.recordingAct_txtTxt);

        Intent thisIntent = getIntent();

        userId = thisIntent.getStringExtra("id");

        ArrayList<VoiceRecord> allRecords = recordDao.getAll();

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

        user = userDao.get(userId);

        //Toast.makeText(getApplicationContext(), records.size()+"", Toast.LENGTH_LONG).show();

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecording) {
                    recorder.stopRecording();
                    startBtn.setText("START");

                    isRecording = false;
                    return;
                }

                if (records.size() >= VoiceRecord.labels.length*20) {
                    Toast.makeText(getApplicationContext(), "ENOUGH RECORD", Toast.LENGTH_LONG).show();
                } else {
                    int labelIndex = -1;

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
                    record.setUser(new User(userId, null));
                    record.setLabel(labelIndex);

                    int recordId = recordDao.insert(record);

                    records.add(record);
                    String gender = user.getGender();

                    outputFile = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+
                            record.getLabel()+"_"+userId+"_"+gender+"_"+
                            String.format("%05d", recordId)+".wav";


                    recorder = new WavRecorder(outputFile);
                    recorder.startRecording();

                    //Toast.makeText(getApplicationContext(), "Started record...", Toast.LENGTH_LONG).show();

                    startBtn.setText("DONE");

                    isRecording = true;

                }
            }
        });


    }
}
