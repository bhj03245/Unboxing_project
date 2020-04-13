package com.example.unboxing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.unboxing.list.ImptList;
import com.example.unboxing.list.ManlList;
import com.example.unboxing.list.NormList;
import com.example.unboxing.list.ParkList;
import com.example.unboxing.parking.ParkLocation;
import com.example.unboxing.setting.UserSetting;

public class MainMenu extends AppCompatActivity {

    Button btn_streaming;
    Button btn_parkLocation;
    Button btn_normList;
    Button btn_manlList;
    Button btn_parkList;
    Button btn_imptList;

    ImageView iv_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        btn_streaming = (Button)findViewById(R.id.real_streaming);
        btn_parkLocation = (Button)findViewById(R.id.park_location);
        btn_normList = (Button)findViewById(R.id.norm_list);
        btn_manlList = (Button)findViewById(R.id.manl_list);
        btn_parkList = (Button)findViewById(R.id.park_list);
        btn_imptList = (Button)findViewById(R.id.impt_list);
        iv_setting = (ImageView)findViewById(R.id.settings);

        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, UserSetting.class);
                startActivity(intent);
            }
        });

        btn_streaming.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Streaming.class);
                startActivity(intent);
            }
        });

        btn_parkLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, ParkLocation.class);
                startActivity(intent);
            }
        });

        btn_normList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, NormList.class);
                startActivity(intent);
            }
        });

        btn_manlList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, ManlList.class);
                startActivity(intent);
            }
        });

        btn_parkList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, ParkList.class);
                startActivity(intent);
            }
        });

        btn_imptList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, ImptList.class);
                startActivity(intent);
            }
        });
    }
}
