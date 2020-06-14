package com.example.ub_test.list;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ub_test.R;

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
import java.util.List;

import androidx.appcompat.widget.SearchView;


public class NormList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static String TAG = "test";

    private ArrayList<VideoVO> mArrayList;
    private VideoAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private String mJsonString;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private DownloadManager downloadManager;
    private long downloadID = -1L;

    private static String list_name = "norm";

    private SearchView searchView;
    private SearchView.OnQueryTextListener queryTextListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.showList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //리스트 초기화 및 Item을 어뎁터 이용하여 리사이클러 뷰에 적용
        mArrayList = new ArrayList<>();
        mAdapter = new VideoAdapter(this, mArrayList, list_name);
        mRecyclerView.setAdapter(mAdapter);

        mArrayList.clear();
        mAdapter.notifyDataSetChanged();


        //SwipeRefreshLayout 등록
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        GetData task = new GetData();
        task.execute();

        //다운로드 매니저 등록
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        registerReceiver(onDownloadComplete, intentFilter);

        //어댑터로부터 다운로드할 url 받기
        Intent intent = getIntent();
        String inTitle = intent.getStringExtra("title");
        String inUrl = intent.getStringExtra("url");

        if (inTitle != null & inUrl != null) {
            Toast.makeText(getApplicationContext(), inUrl, Toast.LENGTH_SHORT).show();

            startDownload(inTitle, inUrl);
            finish();
        }
    }

    //리스트 새로고침
    @Override
    public void onRefresh() {
        mArrayList.clear();
        GetData task = new GetData();
        task.execute();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }

    //해당 Item의 URL로 안드로이드 다운로드 매니저 실행
    private void startDownload(String title, String downloadUrl) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM), title);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl))
                .setTitle("영상")
                .setDescription("다운로드")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setDestinationUri(Uri.fromFile(file))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true);

        downloadID = downloadManager.enqueue(request);
    }

    //다운로드 결과 알려 주는 함수
    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if ("android.intent.action.DOWNlOAD_COMPLETE".equals(intent.getAction())) {

                if (downloadID == id) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(new long[]{id});
                    Cursor cursor = downloadManager.query(query);
                    if (!cursor.moveToFirst()) {
                        return;
                    }

                    int columnIndex = cursor.getColumnIndex("status");
                    int status = cursor.getInt(columnIndex);
                    if (status == 8) {
                        Toast.makeText(context, (CharSequence) "다운로드 성공", Toast.LENGTH_SHORT).show();
                    } else if (status == 16) {
                        Toast.makeText(context, (CharSequence) "다운로드 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if ("android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED".equals(intent.getAction())) {
                Toast.makeText(context, (CharSequence) "Notification clicked", Toast.LENGTH_SHORT).show();
            }
        }
    };

    //리스트에서 검색 함수
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.onActionViewExpanded(); //바로 검색할 수 있도록

        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
            searchView.setQueryHint(getString(R.string.search_hint));
            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    mAdapter.setFilter(filter(mArrayList, newText));
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        return true;
    }

    //필터 설정 함수
    private List<VideoVO> filter(List<VideoVO> testItem, String query) {
        query = query.toLowerCase();

        final List<VideoVO> filteredNoticeList = new ArrayList<>();
        if (query != null && !query.equals("")) {
            for (VideoVO model : testItem) {
                final String title = model.getTitle().toLowerCase();
                if (title.contains(query)) {
                    filteredNoticeList.add(model);
                }
            }
        } else {

        }
        return filteredNoticeList;
    }


    //웹서버에서 리스트 가져오기
    private class GetData extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;
        String errString = null;

        URL url = null;
        HttpURLConnection httpURLConnection;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(NormList.this,
                    "잠시만 기다려주세요", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            if (result == null) {
                Toast.makeText(NormList.this, "Error: Network is not ready", Toast.LENGTH_SHORT).show();
            } else {
                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(Void... unused) {

            try {

                url = new URL(getString(R.string.ip) + "/apkCtrl/normList_apk.php");
                httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
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
            } finally {
                httpURLConnection.disconnect();
            }
        }
    }

    // Asnyctask로 가져온 결과 보여주는 함수
    private void showResult() {
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("fromWeb");

            for (int i = 0; i < jsonArray.length(); i++) {
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

            }
            mAdapter.notifyDataSetChanged();


        } catch (JSONException e) {
            Log.d(TAG, "showResult: ", e);
        }
    }
}
