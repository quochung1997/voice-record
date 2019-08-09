package com.example.voice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.voice.models.User;
import com.example.voice.models.VoiceRecord;
import com.example.voice.sqlites.UserSqliteDao;
import com.example.voice.sqlites.VoiceRecordSqliteDao;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class UploadRecordsAct extends AppCompatActivity {
    final UserSqliteDao userDao = new UserSqliteDao(this);
    final VoiceRecordSqliteDao recordDao = new VoiceRecordSqliteDao(this);

    Button uploadBtn;
    String userId;
    ArrayList<VoiceRecord> records;

    FirebaseStorage fbStorage;
    FirebaseDatabase fbDatabase;
    int k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_records);

        loadData();
        initFirebase();
        initComponents();
        buttonsAction();
    }

    void loadData() {
        userId = getIntent().getStringExtra("id");

        User user = userDao.get(userId);

        ArrayList<VoiceRecord> listRecords = recordDao.getAll();
        records = new ArrayList<>();

        for (VoiceRecord r: listRecords) {
            if (r.getUser().getId().equals(userId)) {
                r.setUser(user);
                records.add(r);
            }
        }
    }

    void initComponents() {
        uploadBtn = findViewById(R.id.uploadAct_uploadBtn);
    }

    void buttonsAction() {
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                k = 0;
                uploadFile();
            }
        });
    }



    void initFirebase() {
        fbStorage = FirebaseStorage.getInstance();
        fbDatabase = FirebaseDatabase.getInstance();
    }

    void uploadFile() {

        if (k >= records.size()) return;

        VoiceRecord record = records.get(k);

        StorageReference reference = fbStorage.getReference();

        reference.child("Uploads").child(record.getFilename()).putFile(Uri.fromFile(new File(record.getPath())))
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    k++;
                    uploadFile();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });
    }
}
