package com.example.ub_test.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ub_test.R;

public class UserSetting extends AppCompatActivity {

    Button btn_pwChange;
    Button btn_reportSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        btn_pwChange = (Button)findViewById(R.id.pwChangeBtn);
        btn_reportSet = (Button)findViewById(R.id.reportSetBtn);

        btn_pwChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_reportSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSetting.this, ReportSetting.class);
                startActivity(intent);
            }
        });
    }
}
