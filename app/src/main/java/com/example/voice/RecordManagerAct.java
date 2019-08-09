package com.example.voice;

import androidx.annotation.Nullable;
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

    Button startRecordBtn, showRecordsBtn, uploadBtn;
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

        loadData();
        initComponents();
        buttonsAction();

    }

    void initComponents() {
        uploadBtn = findViewById(R.id.recordManager_uploadBtn);
        startRecordBtn = findViewById(R.id.recordManager_startRecordBtn);
        showRecordsBtn = findViewById(R.id.recordManager_showRecordsBtn);
        idTxt = findViewById(R.id.recordManager_idTxt);
        genderTxt = findViewById(R.id.recordManager_genderTxt);
        recordNumberTxt = findViewById(R.id.recordManager_recordNumberTxt);
        idTxt.setText("ID: "+user.getId());
        genderTxt.setText("Gender: "+user.getGender());
        recordNumberTxt.setText(recordsCount+"/"+totalRecords+" recorded");
    }

    void loadData() {
        Intent thisIntent = getIntent();

        userId = thisIntent.getStringExtra("id");

        user = userDao.get(userId);

        records = recordDao.getAll();

        recordsCount = 0;
        totalRecords = VoiceRecord.labels.length*20;

        for (VoiceRecord record : records) {
            if (record.getUser().getId().equals(userId)) recordsCount++;
        }
    }

    void reloadData() {
        records = recordDao.getAll();

        recordsCount = 0;
        totalRecords = VoiceRecord.labels.length*20;

        for (VoiceRecord record : records) {
            if (record.getUser().getId().equals(userId)) recordsCount++;
        }

        recordNumberTxt.setText(recordsCount+"/"+totalRecords+" recorded");
    }

    void buttonsAction() {
        startRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toRecording = new Intent(getApplicationContext(), RecordingAct.class);
                toRecording.putExtra("id", userId);
                startActivityForResult(toRecording, 0);
            }
        });

        showRecordsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toListLabels = new Intent(getApplicationContext(), ListLabelsAct.class);
                toListLabels.putExtra("id", userId);
                startActivityForResult(toListLabels, 0);
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toUpload = new Intent(getApplicationContext(), UploadRecordsAct.class);
                toUpload.putExtra("id", userId);
                startActivity(toUpload);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        reloadData();
    }
}
