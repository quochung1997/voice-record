package com.example.voice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.voice.models.User;
import com.example.voice.models.VoiceRecord;
import com.example.voice.sqlites.UserSqliteDao;
import com.example.voice.sqlites.VoiceRecordSqliteDao;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class UploadRecordsAct extends AppCompatActivity {
    final UserSqliteDao userDao = new UserSqliteDao(this);
    final VoiceRecordSqliteDao recordDao = new VoiceRecordSqliteDao(this);

    Button uploadBtn;
    TextView uploadStatus;
    ProgressBar progressBar;
    String userId;
    ArrayList<VoiceRecord> records;

    FirebaseStorage fbStorage;
    FirebaseDatabase fbDatabase;

    UploadTask uploadTask;

    int k;
    String parentDir;
    int recordsCount;

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
        progressBar = findViewById(R.id.uploadAct_progressBar);
        uploadStatus = findViewById(R.id.uploadAct_uploadStatusTxt);

        uploadStatus.setText("Uploaded: 0/"+records.size()+" record(s)");
    }

    void buttonsAction() {
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadAllFiles();
            }
        });
    }



    void initFirebase() {
        fbStorage = FirebaseStorage.getInstance();
        fbDatabase = FirebaseDatabase.getInstance();
    }

    void uploadAllFiles() {
        String currentTimeStr = System.currentTimeMillis()+"";
        parentDir = userId+"_"+currentTimeStr;
        k = 0;
        recordsCount = records.size();
        progressBar.setProgress(0);

        uploadBtn.setEnabled(false);

        uploadFile();
    }

    void uploadFile() {

        final StorageReference reference = fbStorage.getReference();

        VoiceRecord record = records.get(k);

        Uri file = Uri.fromFile(new File(record.getPath()));
        Log.d("last path", file.getLastPathSegment());
        StorageReference riversRef = reference.child(parentDir).child(file.getLastPathSegment());
        uploadTask = riversRef.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                k++;
                uploadStatus.setText("Uploaded: "+k+"/"+recordsCount+" record(s)");
                progressBar.setProgress(k*100/recordsCount);
                if (k == recordsCount) {
                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });

    }
}
