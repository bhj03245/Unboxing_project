package com.example.listviewtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.RecycleViewHolder> {

    private List<RecycleVO> mList;
    private Context mContext;
    private LayoutInflater inflater;


    //뷰 재활용을 위한 viewHolder
    public class RecycleViewHolder extends RecyclerView.ViewHolder{
        protected TextView tv_num;
        protected TextView tv_title;
        protected TextView tv_size;
        protected TextView tv_length;

        public RecycleViewHolder(View view){
            super(view);
            this.tv_num = (TextView) view.findViewById(R.id.list_num);
            this.tv_title = (TextView) view.findViewById(R.id.list_title);
            this.tv_size = (TextView)view.findViewById(R.id.list_size);
            this.tv_length = (TextView) view.findViewById(R.id.list_length);
        }
    }

    public RecycleAdapter(Context mContext, ArrayList<RecycleVO> list){
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.mList = list;
    }



    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list, viewGroup, false);

        return new RecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {

        RecycleVO item = mList.get(position);


        holder.tv_num.setText(item.getNum());
        holder.tv_title.setText("영상 제목: " + item.getTitle());
        holder.tv_size.setText("영상 사이즈: " + item.getSize());
        holder.tv_length.setText("영상 길이: " + item.getLength());


    }

    public void addItem(RecycleVO recycleVO){
        mList.add(recycleVO);
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
