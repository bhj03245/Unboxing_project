package com.example.listviewtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private static String TAG = "phptest_MainActivity";

    private static final String TAG_JSON="fromWeb";
    private static final String TAG_NUM = "num";
    private static final String TAG_TITLE = "title";
    private static final String TAG_SIZE ="size";
    private static final String TAG_LENGTH ="length";
    //private static final String TAG_URL ="url";

    public static final String listURL = "http://211.216.137.157/apkCtrl/normList_apk.php";

    private TextView textViewResult;
    private ArrayList<RecycleVO> mArrayList = new ArrayList<>();
    private RecycleAdapter recycleAdapter;
    private RecycleVO recycleVO;
    private RecyclerView recyclerView;
    private VideoItemDeco videoItemDeco;
    String jsonString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //textViewResult = (TextView)findViewById(R.id.textView);


        //recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recycleAdapter = new RecycleAdapter(getApplicationContext(), mArrayList);
        recyclerView.setAdapter(recycleAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation()));

        videoItemDeco = new VideoItemDeco(5);
        recyclerView.addItemDecoration(videoItemDeco);


        GetData task = new GetData();
        task.execute(listURL);
    }

    private class GetData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        String errString=null;
        HttpURLConnection httpURLConnection;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = progressDialog.show(MainActivity.this, "잠시만 기다려주세요.", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //textViewResult.setText(result);
            Log.d(TAG, "response" + result);

            if(result==null) {
                //textViewResult.setText(errString);
            }else{
                jsonString=result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];

            try {
                url = new URL(serverURL);
                httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream is;
                if (responseStatusCode == httpURLConnection.HTTP_OK) {
                    is = httpURLConnection.getInputStream();
                } else {
                    is = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(is, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
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
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);

                String num = item.getString(TAG_NUM);
                String title = item.getString(TAG_TITLE);
                String size = item.getString(TAG_SIZE);
                String length = item.getString(TAG_LENGTH);
                //String url = item.getString(TAG_URL);


                recycleVO = new RecycleVO();
                recycleVO.setNum(num);
                recycleVO.setTitle(title);
                recycleVO.setSize(size);
                recycleVO.setLength(length);


                mArrayList.add(recycleVO);
                //recycleAdapter.addItem(recycleVO);

            }
            recycleAdapter.notifyDataSetChanged();


        }catch (JSONException e){
            Log.d(TAG, "showResult: ", e);
        }
    }

}
