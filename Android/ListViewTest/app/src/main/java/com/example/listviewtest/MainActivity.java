package com.example.listviewtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;

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
    private ArrayList<RecycleVO> mArrayList;
    private RecycleAdapter recycleAdapter;
    private RecycleVO recycleVO;
    private int count = -1;

    String jsonString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = (TextView)findViewById(R.id.textView);
        //mlistView = (ListView)findViewById(R.id.listView);
       // arrayList = new ArrayList<>();


        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        mArrayList = new ArrayList<>();
        recycleAdapter = new RecycleAdapter(mArrayList);
        recyclerView.setAdapter(recycleAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


        GetData task = new GetData();
        task.execute(listURL);
    }

    private class GetData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        String errString=null;

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
                textViewResult.setText(errString);
            }else{
                jsonString=result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setConnectTimeout(10000);
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


                recycleVO = new RecycleVO(num, title, size, length);
                mArrayList.add(recycleVO);
                //recycleAdapter.addItem(recycleVO);

            }
            recycleAdapter.notifyDataSetChanged();


        }catch (JSONException e){
            Log.d(TAG, "showResult: ", e);
        }
    }


}
