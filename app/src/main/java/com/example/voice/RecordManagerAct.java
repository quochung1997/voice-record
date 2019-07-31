package com.example.voice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.voice.models.User;
import com.example.voice.models.VoiceRecord;
import com.example.voice.sqlites.UserSqliteDao;
import com.example.voice.sqlites.VoiceRecordSqliteDao;

import java.util.ArrayList;

public class RecordManagerAct extends AppCompatActivity {
    final UserSqliteDao userDao = new UserSqliteDao(this);
    final VoiceRecordSqliteDao recordDao = new VoiceRecordSqliteDao(this);

    Button startRecordBtn, showRecordsBtn;
    TextView idTxt, genderTxt, recordNumberTxt;

    User user;
    String userId;
    ArrayList<VoiceRecord> records;
    int recordsCount;
    int totalRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_manager);

        startRecordBtn = findViewById(R.id.recordManager_startRecordBtn);
        showRecordsBtn = findViewById(R.id.recordManager_showRecordsBtn);
        idTxt = findViewById(R.id.recordManager_idTxt);
        genderTxt = findViewById(R.id.recordManager_genderTxt);
        recordNumberTxt = findViewById(R.id.recordManager_recordNumberTxt);

        Intent thisIntent = getIntent();

        userId = thisIntent.getStringExtra("id");

        user = userDao.get(userId);

        records = recordDao.getAll();

        recordsCount = 0;
        totalRecords = VoiceRecord.labels.length*20;

        for (VoiceRecord record : records) {
            if (record.getUser().getId().equals(userId)) recordsCount++;
        }

        idTxt.setText("ID: "+user.getId());
        genderTxt.setText("Gender: "+user.getGender());
        recordNumberTxt.setText(recordsCount+"/"+totalRecords+" recorded");

        startRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toRecording = new Intent(getApplicationContext(), RecordingAct.class);
                toRecording.putExtra("id", userId);
                startActivity(toRecording);
            }
        });
    }
}
