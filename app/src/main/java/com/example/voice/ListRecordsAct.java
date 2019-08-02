package com.example.voice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.voice.adapters.LabelAdapter;
import com.example.voice.adapters.RecordAdapter;
import com.example.voice.models.User;
import com.example.voice.models.VoiceRecord;
import com.example.voice.sqlites.UserSqliteDao;
import com.example.voice.sqlites.VoiceRecordSqliteDao;

import java.util.ArrayList;

public class ListRecordsAct extends AppCompatActivity {
    ListView listView;
    ArrayList<VoiceRecord> records;
    User user;

    final VoiceRecordSqliteDao recordDao = new VoiceRecordSqliteDao(this);
    final UserSqliteDao userDao = new UserSqliteDao(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_records);

        Intent thisIntent = getIntent();

        String userId = thisIntent.getStringExtra("id");
        String label = thisIntent.getStringExtra("label");

        user = userDao.get(userId);
        ArrayList<VoiceRecord> allRecords = recordDao.getAll();
        records = new ArrayList<>();

        for (VoiceRecord record: allRecords) {
            if (record.getUser().getId().equals(userId) && record.getLabel().equals(label)) {
                record.setUser(user);
                records.add(record);
            }
        }

        RecordAdapter recordAdapter = new RecordAdapter(getApplicationContext(), records);

        listView = findViewById(R.id.listRecords);

        listView.setAdapter(recordAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent toRecordInfo = new Intent(getApplicationContext(), RecordInfoAct.class);
                toRecordInfo.putExtra("recordId", records.get(i).getNumber());
                startActivity(toRecordInfo);
            }
        });

    }
}
