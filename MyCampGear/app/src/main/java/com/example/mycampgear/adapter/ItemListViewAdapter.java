package com.example.mycampgear.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycampgear.R;
import com.example.mycampgear.entity.ItemEntity;

import java.util.List;

public class ItemListViewAdapter extends BaseAdapter {

    static class ViewHolder {
        TextView brand;
        TextView itemName;
        TextView category;
        TextView description;
        ImageView imageView;
    }

    private LayoutInflater inflater;
    private int itemLayoutId;
    private List<ItemEntity> itemEntities;


    public ItemListViewAdapter(Context context, int itemLayoutId,
                               List<ItemEntity> itemEntities) {
        super();
        this.inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemLayoutId = itemLayoutId;
        this.itemEntities = itemEntities;
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
            holder.brand = convertView.findViewById(R.id.brand);
            holder.itemName = convertView.findViewById(R.id.itemName);
            holder.imageView =convertView.findViewById(R.id.imageView);
            holder.category =convertView.findViewById(R.id.category);
            holder.description = convertView.findViewById(R.id.descption);
            convertView.setTag(holder);
        }
        // holder を使って再利用
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        // holder の imageView にセット
        holder.imageView.setImageBitmap(itemEntities.get(position).getImage());

        // 現在の position にあるファイル名リストを holder の textView にセット
        holder.brand.setText(itemEntities.get(position).getBrand());
        holder.itemName.setText(itemEntities.get(position).getItemName());
        holder.category.setText(itemEntities.get(position).getCategory());
        holder.description.setText(itemEntities.get(position).getDescription());

        return convertView;
    }

    @Override
    public int getCount() {
        // texts 配列の要素数
        return itemEntities.size();
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