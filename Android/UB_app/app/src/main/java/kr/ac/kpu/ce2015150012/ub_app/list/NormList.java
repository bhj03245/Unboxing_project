package kr.ac.kpu.ce2015150012.ub_app.list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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

import kr.ac.kpu.ce2015150012.ub_app.R;

public class NormList extends AppCompatActivity{

    private static String IP_ADDRESS = "http://211.216.137.157/apkCtrl/normList_apk.php";
    private static String TAG = "phptest";

    private ArrayList<VideoVO> mArrayList;
    private VideoAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private String mJsonString;

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
    }

    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errString = null;

        URL url = null;
        HttpURLConnection httpURLConnection;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(NormList.this,
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
