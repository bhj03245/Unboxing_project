package com.example.ub_test.setting;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import com.example.ub_test.R;

public class ReportSetting extends Activity {

    EditText et_phone;
    EditText et_contents;
    Button btn_insert;

    String str_phone;
    String str_contents;

    private SharedPreferences data;
    private String load_phone;
    private String load_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_report_setting);

        et_phone = (EditText)findViewById(R.id.et_phoneNum);
        et_contents = (EditText)findViewById(R.id.et_contents);
        btn_insert = (Button)findViewById(R.id.insertBtn);

        data = getSharedPreferences("report_data", MODE_PRIVATE);


        if(data != null) {
            load();
            et_phone.setText(load_phone);
            et_contents.setText(load_content);
        }

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_phone = et_phone.getText().toString();
                str_contents = et_contents.getText().toString();
                save();
            }
        });
    }
    private void save(){
        SharedPreferences.Editor editor = data.edit();

        editor.putString("phone", str_phone.trim());
        editor.putString("content", str_contents.trim());

        editor.apply();
        finish();
    }

    private void load() {
        load_phone = data.getString("phone", "");
        load_content = data.getString("content", "");
    }
}
