package com.example.recyclerviewtest;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        return new VideoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {

        VideoVO item = mList.get(position);

        holder.tv_num.setText(item.getNum());
        holder.tv_title.setText("영상 제목: " + item.getTitle());
        holder.tv_size.setText("영상 사이즈: " + item.getSize());
        holder.tv_length.setText("영상 길이: " + item.getLength());
        holder.iv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != mList? mList.size() : 0);
    }
}
