package com.example.recyclerview_search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    private final List<TestItems> mValues;
    private Context mContext = null;

    public MyAdapter(Context context, List<TestItems> items){
        this.mContext = context;
        mValues = items;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent, false));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleText.setText(mValues.get(position).getTitle());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //검색리스트 리프레시
    public void setFilter(List<TestItems> items){
        mValues.clear();
        mValues.addAll(items);
        notifyDataSetChanged();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleText;

        public TestItems mItem;

        public MyHolder(View view) {
            super(view);
            mView = view;
            mTitleText = (TextView) view.findViewById(R.id.item_title);
        }
    }
}
