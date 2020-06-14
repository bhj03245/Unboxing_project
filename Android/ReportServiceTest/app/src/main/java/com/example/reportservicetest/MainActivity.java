package com.example.reportservicetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Context context = this;
    private Intent serviceIntent;
    Switch mode_switch;
    SharedPreferences data;

    volatile boolean modeChecked = false;

    boolean load_mode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mode_switch = (Switch) findViewById(R.id.mode_switch);

        mode_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (ReportService.serviceIntent == null) {
                        serviceIntent = new Intent(context, ReportService.class);
                        startService(serviceIntent);
                        Toast.makeText(context, "주행모드", Toast.LENGTH_SHORT).show();

                    } else {
                        serviceIntent = ReportService.serviceIntent;//getInstance().getApplication();
                        Toast.makeText(MainActivity.this, "already", Toast.LENGTH_LONG).show();
                    }
                } else {
                    stopService(serviceIntent);
                    Toast.makeText(context, "주차모드", Toast.LENGTH_SHORT).show();
                }
                //String ex = Boolean.toString(isChecked);
                //System.out.println("스트링 확인" + ex);
                //System.out.println("isCecked 확인" + isChecked);
                modeChecked = isChecked;

                Intent toReport = new Intent(MainActivity.this, ReportService.class);
                toReport.putExtra("modeChecked", isChecked);
                startService(toReport);

                Intent toAlarm = new Intent(MainActivity.this, AlarmReceiver.class);
                toAlarm.putExtra("toAlarmChecked", isChecked);

                SharedPreferences data = getSharedPreferences("switch_data", MODE_PRIVATE);
                SharedPreferences.Editor editor = data.edit();
                editor.putBoolean("switchkey", isChecked);
                editor.commit();
            }
        });

        data = getSharedPreferences("switch_data", MODE_PRIVATE);
        load_mode = data.getBoolean("switchkey", false);
        mode_switch.setChecked(load_mode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceIntent != null) {
            stopService(serviceIntent);
            serviceIntent = null;
        }

        if (modeChecked == true) {
            serviceIntent = new Intent(context, ReportService.class);
            startService(serviceIntent);
        }
    }
}
