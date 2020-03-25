package kr.ac.kpu.ce2015150012.ub_app.list;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.ac.kpu.ce2015150012.ub_app.R;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {

    private List<VideoVO> mList = null;
    private Activity context = null;
    private LayoutInflater inflater;

    private DownloadManager downloadManager;
    private DownloadManager.Request request;
    private Uri urlToDownload;

    private long latestId = -1;


    public VideoAdapter(Activity context, ArrayList<VideoVO> list){
        this.context= context;
        inflater= LayoutInflater.from(context);
        this.mList = list;
    }

    class VideoHolder extends RecyclerView.ViewHolder{
        protected TextView tv_num;
        protected TextView tv_title;
        protected TextView tv_size;
        protected TextView tv_length;
        protected ImageView iv_download;


        public VideoHolder(View view){
            super(view);
            this.tv_num = (TextView) view.findViewById(R.id.list_num);
            this.tv_title = (TextView) view.findViewById(R.id.list_title);
            this.tv_size = (TextView)view.findViewById(R.id.list_size);
            this.tv_length = (TextView) view.findViewById(R.id.list_length);
            this.iv_download = (ImageView) view.findViewById(R.id.list_download);


        }
    }


    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {

        VideoVO item = mList.get(position);

        holder.tv_num.setText(item.getNum());
        holder.tv_title.setText("영상 제목: " + item.getTitle());
        holder.tv_size.setText("영상 사이즈: " + item.getSize());
        holder.tv_length.setText("영상 길이: " + item.getLength());
        final String videoUrl = item.getUrl();
        holder.iv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> pathSegments = urlToDownload.getPathSegments();
                urlToDownload = Uri.parse(videoUrl);
                request = new DownloadManager.Request(urlToDownload);
                request.setTitle("영상 다운로드");
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, pathSegments.get(pathSegments.size() - 1));
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
                latestId = downloadManager.enqueue(request);
            }

        });

    }

    @Override
    public int getItemCount() {
        return (null != mList? mList.size() : 0);
    }


    private BroadcastReceiver donwloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "다운로드가 완료되었습니다.", Toast.LENGTH_SHORT).show();
        }
    };
}
