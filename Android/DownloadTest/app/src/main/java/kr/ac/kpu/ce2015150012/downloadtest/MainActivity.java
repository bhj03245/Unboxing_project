package kr.ac.kpu.ce2015150012.downloadtest;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText downloadUrl;
    private Button addToQueueButton;
    private Button cancelLatestButton;
    private Button viewDownloadsButton;

    private long latestId = -1;

    private DownloadManager downloadManager;
    private DownloadManager.Request request;
    private Uri urlToDownload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadUrl = (EditText)findViewById(R.id.downloadUrl);
        addToQueueButton = (Button)findViewById(R.id.addQueueButton);
        cancelLatestButton = (Button)findViewById(R.id.cancelDownloadButton);
        viewDownloadsButton = (Button)findViewById(R.id.viewDownloadsButton);

        downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);

        addToQueueButton.setOnClickListener(this);
        cancelLatestButton.setOnClickListener(this);
        viewDownloadsButton.setOnClickListener(this);

    }

    private BroadcastReceiver completeReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "다운로드가 완료되었습니다.",Toast.LENGTH_SHORT).show();
        }

    };

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.addQueueButton:

                urlToDownload = Uri.parse(downloadUrl.getText().toString());
                request = new DownloadManager.Request(urlToDownload);
                request.setTitle("다운로드 예제");
                request.setDescription("항목 설명");

                latestId = downloadManager.enqueue(request);
                downloadUrl.setText("");
                break;

            case R.id.cancelDownloadButton:
                downloadManager.remove(latestId);
                break;

            case R.id.viewDownloadsButton:
                startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
                break;
        }
    }


    @Override
    public void onResume(){
        super.onResume();
        IntentFilter completeFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(completeReceiver, completeFilter);
    }

    @Override
    public void onPause(){
        super.onPause();
        unregisterReceiver(completeReceiver);
    }
}
