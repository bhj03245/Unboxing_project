package com.example.listviewtest;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.RecycleViewHolder> {

    private ArrayList<RecycleVO> mList = new ArrayList<>();

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

    public RecycleAdapter(ArrayList<RecycleVO> list){
        this.mList = list;
    }


    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list, viewGroup, false);

        RecycleViewHolder viewHolder = new RecycleViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {

        holder.tv_num.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        holder.tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        holder.tv_size.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        holder.tv_length.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        holder.tv_num.setGravity(Gravity.CENTER);
        holder.tv_title.setGravity(Gravity.CENTER);
        holder.tv_size.setGravity(Gravity.CENTER);
        holder.tv_length.setGravity(Gravity.CENTER);

        holder.tv_num.setText(mList.get(position).getNum());
        holder.tv_title.setText("영상 제목: " + mList.get(position).getTitle());
        holder.tv_size.setText("영상 사이즈: " +mList.get(position).getSize());
        holder.tv_length.setText("영상 길이: " + mList.get(position).getLength());
    }

    public void addItem(RecycleVO recycleVO){
        mList.add(recycleVO);
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
