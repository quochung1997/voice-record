package com.example.voice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.example.voice.threads.AudioRecordThread;
import com.example.voice.threads.CountingRunnable;
import com.example.voice.threads.RecordThr;
import com.example.voice.threads.WavRecorderRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RecordingAct extends AppCompatActivity {

    static final int REQUEST_AUDIORECORD = 1;
    static final int REQUEST_STORAGE = 2;

    final VoiceRecordSqliteDao recordDao = new VoiceRecordSqliteDao(this);
    final UserSqliteDao userDao = new UserSqliteDao(this);

    ArrayList<VoiceRecord> records;
    User user;
    String userId;

    Button startBtn;
    TextView txtTxt;

    HashMap<String, Integer> map;

    String outputFile;

    boolean isRecording;

    WavRecorder wavRecorder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.RECORD_AUDIO
            }, REQUEST_AUDIORECORD);
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, REQUEST_STORAGE);
        }


        isRecording = false;

        startBtn = findViewById(R.id.recordingAct_startBtn);
        txtTxt = findViewById(R.id.recordingAct_txtTxt);



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



        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecording) {
                    startBtn.setText("START");

                    wavRecorder.stopRecording();

                    isRecording = false;
                    return;
                }

                if (nextWord()) {

                    wavRecorder = new WavRecorder(outputFile);

                    try {
                        Thread.sleep(100);
                        wavRecorder.startRecording();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    startBtn.setText("DONE");
                    isRecording = true;

                }
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_AUDIORECORD: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "AUDIO REQUEST ACCESSED", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "AUDIO REQUEST DENIED", Toast.LENGTH_LONG).show();
                }
                return;
            }

            case REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "STORAGE REQUEST ACCESSED", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "STORAGE REQUEST DENIED", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


    boolean nextWord() {
        if (records.size() >= VoiceRecord.labels.length*20) {
            Toast.makeText(getApplicationContext(), "ENOUGH RECORD", Toast.LENGTH_LONG).show();
            return false;
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
            record.setUser(new User(userId, user.getGender()));
            record.setLabel(labelIndex);

            int recordId = recordDao.insert(record);

            record.setNumber(recordId);

            records.add(record);
            String gender = user.getGender();

            outputFile = record.getPath();

            return true;

        }
    }

}
