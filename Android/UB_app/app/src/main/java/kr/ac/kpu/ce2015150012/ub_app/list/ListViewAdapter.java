package kr.ac.kpu.ce2015150012.ub_app.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.List;

import kr.ac.kpu.ce2015150012.ub_app.R;

public class ListViewAdapter extends BaseAdapter {
    private LayoutInflater inflate;
    private ViewHolder viewHolder;
    private List<String> list;

    public ListViewAdapter(Context context, List<String> list){
        this.list=list;
        this.inflate = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflate.inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.num = (TextView) convertView.findViewById(R.id.num);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.size = (TextView) convertView.findViewById(R.id.size);
            viewHolder.length = (TextView) convertView.findViewById(R.id.length);
            viewHolder.img_download = (ImageView) convertView.findViewById(R.id.downloadImage);
            convertView.setTag(viewHolder);
        }else
            viewHolder = (ViewHolder)convertView.getTag();

        return convertView;
    }

    class ViewHolder{

        public TextView num;
        public TextView title;
        public TextView size;
        public TextView length;
        public ImageView img_download;
    }
}
