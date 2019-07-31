package com.example.voice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.voice.models.User;
import com.example.voice.sqlites.UserSqliteDao;
import com.example.voice.sqlites.VoiceRecordSqliteDao;

public class MainActivity extends AppCompatActivity {
    final UserSqliteDao userDao = new UserSqliteDao(this);
    final VoiceRecordSqliteDao recordDao = new VoiceRecordSqliteDao(this);
    Button loginBtn, regisBtn;
    EditText idEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        userDao.drop();
//        recordDao.drop();

        final SharedPreferences preferences = getSharedPreferences("id", Context.MODE_PRIVATE);
        final Intent thisIntent = getIntent();
        String oldId = preferences.getString("id", null);
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

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("id", idStr);
                    editor.commit();

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
}
