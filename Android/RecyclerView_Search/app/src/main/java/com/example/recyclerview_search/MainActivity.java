package com.example.recyclerview_search;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private SerializeContent mSerialContent = null;
    private Context mContext=null;
    private Button mTestBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mSerialContent = new SerializeContent();
        mSerialContent.getList().add(new TestItems("이왕규"));
        mSerialContent.getList().add(new TestItems("백현준"));
        mSerialContent.getList().add(new TestItems("이재근"));
        mSerialContent.getList().add(new TestItems("양현용"));
        mSerialContent.getList().add(new TestItems("박인효"));
        mSerialContent.getList().add(new TestItems("한승훈"));
        mSerialContent.getList().add(new TestItems("ㅁㄴㅇㄹ"));
        mSerialContent.getList().add(new TestItems("ㅎㅇ"));
        mSerialContent.getList().add(new TestItems("졸업작품.."));

        mTestBtn = (Button)findViewById(R.id.test_btn);
        mTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, SearchList.class);
                in.putExtra(SerializeContent.CONTENT, mSerialContent);
                startActivity(in);
            }
        });
    }
}
