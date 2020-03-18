package kr.ac.kpu.ce2015150012.ub_app.list;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import kr.ac.kpu.ce2015150012.ub_app.R;

public class NormList extends AppCompatActivity implements AbsListView.OnScrollListener{

    private ListView listView;
    private boolean lastItemVisibleFlag = false; //리스트 스크롤이 마지막 셀로 이동했는지 체크
    private List<String>list;
    private ListViewAdapter adapter;
    private int page=0;
    private final int OFFSET=20; //한 페이지마다 출력되는 데이터 개수
    private ProgressBar progressBar; //데이터 로딩을 표시할 용도
    private boolean mLockListView = false; // 데이터 불러올 때 중복되지 않게 하기 위한 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_norm_list);

        listView = (ListView) findViewById(R.id.list_item);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        list = new ArrayList<String>();
        adapter = new ListViewAdapter(this, list);
        listView.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);
        listView.setOnScrollListener(this);
        getItem();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 1. OnScrollListener.SCROLL_STATE_IDLE : 스크롤이 이동하지 않을때의 이벤트(즉 스크롤이 멈추었을때).
        // 2. lastItemVisibleFlag : 리스트뷰의 마지막 셀의 끝에 스크롤이 이동했을때.
        // 3. mLockListView == false : 데이터 리스트에 다음 데이터를 불러오는 작업이 끝났을때.
        // 1, 2, 3 모두가 true일때 다음 데이터를 불러온다.
        if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && mLockListView == false){
            progressBar.setVisibility(View.VISIBLE);
            getItem();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // firstVisibleItem : 화면에 보이는 첫번째 리스트의 아이템 번호.
        // visibleItemCount : 화면에 보이는 리스트 아이템의 갯수
        // totalItemCount : 리스트 전체의 총 갯수
        lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
    }

    private void getItem(){
        mLockListView = true;

        for(int i=0; i<20; i++){
            //수정 필요
            String label="Label" + ((page+OFFSET) + i);
            list.add(label);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                mLockListView=false;
            }
        }, 1000);
    }
}
