package com.example.recyclerviewtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "http://211.216.137.157/apkCtrl/normList_apk.php";
    private static String TAG = "phptest";

    private ArrayList<VideoVO> mArrayList;
    private VideoAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private String mJsonString;

    private DownloadManager downloadManager;
    private long downloadID = -1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView)findViewById(R.id.normList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mArrayList = new ArrayList<>();
        mAdapter = new VideoAdapter(this, mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        //VideoItemDeco videoItemDeco = new VideoItemDeco(1);
        //mRecyclerView.addItemDecoration(videoItemDeco);

        mArrayList.clear();
        mAdapter.notifyDataSetChanged();

        GetData task = new GetData();
        task.execute(IP_ADDRESS);


        downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        registerReceiver(onDownloadComplete, intentFilter);

    }

    protected void startDownload(String title, String download_Url){
        File file = new File(getExternalFilesDir(null), title);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(download_Url))
                .setTitle("영상")
                .setDescription("다운로드")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setDestinationUri(Uri.fromFile(file))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true);


        downloadID = downloadManager.enqueue(request);
    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if("android.intent.action.DOWNlOAD_COMPLETE".equals(intent.getAction())) {

                if (downloadID == id) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(new long[]{id});
                    Cursor cursor = downloadManager.query(query);
                    if(!cursor.moveToFirst()){
                        return;
                    }

                    int columnIndex = cursor.getColumnIndex("status");
                    int status = cursor.getInt(columnIndex);
                    if(status==8){
                        Toast.makeText(context, (CharSequence)"Download succeeded", Toast.LENGTH_SHORT).show();
                    }else if (status == 16){
                        Toast.makeText(context, (CharSequence)"Download failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }else if ("android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED".equals(intent.getAction())) {
                Toast.makeText(context, (CharSequence)"Notification clicked", Toast.LENGTH_SHORT).show();
            }
        }
    };


    private class GetData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        String errString = null;

        URL url = null;
        HttpURLConnection httpURLConnection;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            if (result == null){

                //mTextViewResult.setText(errorString);
            }
            else {

                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            //String postParameters = params[1];



            try {

                url = new URL(serverURL);
                httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();

            } catch (MalformedURLException e) {
                Log.d(TAG, "InsertData: Error ", e);
                errString = e.toString();
                return null;
            } catch (IOException e) {
                Log.d(TAG, "InsertData: Error ", e);
                errString = e.toString();
                return null;
            }finally{
                httpURLConnection.disconnect();
            }
        }
    }

    private void showResult(){


        try{
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("fromWeb");

            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);

                String num = item.getString("num");
                String title = item.getString("title");
                String size = item.getString("size");
                String length = item.getString("length");
                String url = item.getString("url");

                VideoVO data = new VideoVO();

                data.setNum(num);
                data.setTitle(title);
                data.setSize(size);
                data.setLength(length);
                data.setUrl(url);


                mArrayList.add(data);

                //recycleAdapter.addItem(recycleVO);

            }
            mAdapter.notifyDataSetChanged();


        }catch (JSONException e){
            Log.d(TAG, "showResult: ", e);
        }
    }

}
