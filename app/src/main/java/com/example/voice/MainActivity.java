package com.example.voice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.voice.models.User;
import com.example.voice.sqlites.UserSqliteDao;
import com.example.voice.sqlites.VoiceRecordSqliteDao;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST = 1;

    final UserSqliteDao userDao = new UserSqliteDao(this);
    final VoiceRecordSqliteDao recordDao = new VoiceRecordSqliteDao(this);
    Button loginBtn, regisBtn;
    EditText idEdt;
    String oldId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createFolder();
        requestPermission();


        final SharedPreferences preferences = getSharedPreferences("id", Context.MODE_PRIVATE);
        final Intent thisIntent = getIntent();
        oldId = preferences.getString("id", null);
        if (thisIntent.hasExtra("id")) oldId = thisIntent.getStringExtra("id");

        loginBtn = findViewById(R.id.mainAct_loginBtn);
        regisBtn = findViewById(R.id.mainAct_regisBtn);
        idEdt = findViewById(R.id.mainAct_idEdt);

        idEdt.setText(oldId);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idStr = idEdt.getText().toString();

                User user = userDao.get(idStr);

                if (user != null) {
                    Intent toRecordManager = new Intent(getApplicationContext(), RecordManagerAct.class);
                    toRecordManager.putExtra("id", idStr);

                    if (oldId == null || !oldId.equals(idStr)) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("id", idStr);
                        editor.commit();
                    }

                    startActivity(toRecordManager);
                } else {
                    Toast.makeText(getApplicationContext(),"ID DOES NOT EXIST",  Toast.LENGTH_LONG).show();
                }
            }
        });

        regisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toRegis = new Intent(getApplicationContext(), RegisAct.class);

                startActivity(toRegis);
            }
        });
    }

    void createFolder() {
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "voice_records");
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "ACCESSED", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "DENIED", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
