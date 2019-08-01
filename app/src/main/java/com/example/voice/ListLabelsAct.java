package com.example.voice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.voice.adapters.LabelAdapter;
import com.example.voice.models.User;
import com.example.voice.models.VoiceRecord;
import com.example.voice.sqlites.UserSqliteDao;
import com.example.voice.sqlites.VoiceRecordSqliteDao;

import java.lang.invoke.VolatileCallSite;
import java.util.ArrayList;

public class ListLabelsAct extends AppCompatActivity {
    ListView listView;
    ArrayList<VoiceRecord> records;
    User user;

    final VoiceRecordSqliteDao recordDao = new VoiceRecordSqliteDao(this);
    final UserSqliteDao userDao = new UserSqliteDao(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_labels);

        Intent thisIntent = getIntent();

        final String userId = thisIntent.getStringExtra("id");

        user = userDao.get(userId);

        ArrayList<VoiceRecord> list = recordDao.getAll();

        records = new ArrayList<>();

        for (VoiceRecord record: list) {
            if (record.getUser().getId().equals(userId)) records.add(record);
        }

        final String[] labels = new String[VoiceRecord.labels.length];
        String[] numbers = new String[VoiceRecord.labels.length];

        for (int i = 0; i < VoiceRecord.labels.length; i++) {
            labels[i] = VoiceRecord.labels[i];
            int count = 0;
            for (VoiceRecord record: records) {
                if (record.getLabel().equals(labels[i])) count++;
            }
            numbers[i] = count+"";
        }


        listView = findViewById(R.id.listLabels);

        LabelAdapter labelAdapter = new LabelAdapter(getApplicationContext(), labels, numbers);

        listView.setAdapter(labelAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent toRecords = new Intent(getApplicationContext(), ListRecordsAct.class);

                toRecords.putExtra("id", user.getId());
                toRecords.putExtra("label", labels[i]);

                startActivity(toRecords);
            }
        });

    }
}
