package com.example.ub_test.list;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ub_test.R;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {

    private List<VideoVO> mList = null;
    private Activity context = null;
    private LayoutInflater inflater;


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

        final String videoTitle = item.getTitle();
        final String videoUrl = item.getUrl();
        holder.iv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent toMain = new Intent(v.getContext(), NormList.class);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(videoTitle);
                builder.setMessage("다운로드하시겠습니까?");
                builder.setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                toMain.putExtra("title", videoTitle);
                                toMain.putExtra("url", videoUrl);
                                context.startActivity(toMain);
                            }
                        });
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
            }

        });
    }
    @Override
    public int getItemCount() {
        return (null != mList? mList.size() : 0);
    }
}
