package com.example.voice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.voice.models.User;
import com.example.voice.sqlites.UserSqliteDao;

public class RegisAct extends AppCompatActivity {
    final UserSqliteDao userDao = new UserSqliteDao(this);
    Button submitBtn;
    EditText idEdt;
    CheckBox isMaleChk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);

        submitBtn = findViewById(R.id.regisAct_submitBtn);
        idEdt = findViewById(R.id.regisAct_idEdt);
        isMaleChk = findViewById(R.id.regisAct_isMaleChk);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idStr = idEdt.getText()+"";
                String gender = isMaleChk.isChecked() ? "MALE" : "FEMALE";

                if (idStr.equals("")) {
                    Toast.makeText(getApplicationContext(), "ID IS NOT VALID", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    userDao.insert(new User(idStr, gender));

                    Intent toMain = new Intent(getApplicationContext(), MainActivity.class);
                    toMain.putExtra("id", idStr);

                    startActivity(toMain);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "THIS ID WAS EXISTED", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
