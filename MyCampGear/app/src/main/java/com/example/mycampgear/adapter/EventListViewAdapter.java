package com.example.mycampgear.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycampgear.R;
import com.example.mycampgear.entity.EventEntity;

import java.util.List;

public class EventListViewAdapter extends BaseAdapter {

    static class ViewHolder {
        TextView textView;
        TextView dateView;
        TextView descriptionView;
        ImageView imageView;
    }

    private LayoutInflater inflater;
    private int itemLayoutId;
    private List<EventEntity> eventEntities;
    public EventListViewAdapter(Context context, int itemLayoutId,
                                List<EventEntity> eventEntities) {
        super();
        this.inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemLayoutId = itemLayoutId;
        this.eventEntities = eventEntities;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        // 最初だけ View を inflate して、それを再利用する
        if (convertView == null) {
            // activity_main.xml に list.xml を inflate して convertView とする
            convertView = inflater.inflate(itemLayoutId, parent, false);
            // ViewHolder を生成
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.textView);
            holder.dateView = convertView.findViewById(R.id.dateView);
            holder.descriptionView = convertView.findViewById(R.id.descptionView);
            holder.imageView = convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        }
        // holder を使って再利用
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 現在の position にあるファイル名リストを holder の textView にセット
        holder.textView.setText(eventEntities.get(position).getTitle());
        holder.dateView.setText(eventEntities.get(position).getDate());
        holder.descriptionView.setText(eventEntities.get(position).getDescription());

        // holder の imageView にセット
        if(eventEntities.get(position).getImage()!=null){
            holder.imageView.setImageBitmap(eventEntities.get(position).getImage());
        }else{
            holder.imageView.setImageResource(R.drawable.no_image);
        }
        return convertView;
    }

    @Override
    public int getCount() {
        // texts 配列の要素数
        return eventEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}