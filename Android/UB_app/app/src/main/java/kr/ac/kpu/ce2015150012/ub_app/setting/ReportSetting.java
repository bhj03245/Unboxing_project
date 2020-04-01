package kr.ac.kpu.ce2015150012.ub_app.setting;


import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.Observer;

import java.util.List;

import static android.content.ContentValues.TAG;
import kr.ac.kpu.ce2015150012.ub_app.R;

public class ReportSetting extends Activity {

    ReportDB reportDB;
    SQLiteDatabase db;

    EditText et_phoneNum;
    EditText et_contents;
    Button btn_insert;

    String str_phoneNum;
    String str_contents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_report_setting);

        et_phoneNum = (EditText)findViewById(R.id.et_phoneNum);
        et_contents = (EditText)findViewById(R.id.et_contents);
        btn_insert = (Button)findViewById(R.id.insertBtn);

        str_phoneNum = et_phoneNum.getText().toString();
        str_contents = et_contents.getText().toString();

        ReportDB db = ReportDB.getAppDatabase(this);



        et_phoneNum.setText(db.reportDAO().loadPhone().toString());


        btn_insert.setOnClickListener(new View.OnClickListener() { //데이터가 없다면 insert 있다면 update하는 걸로
            @Override
            public void onClick(View v) {

            }
        });
    }


}
