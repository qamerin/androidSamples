package com.example.mycampgear.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycampgear.R;

public class AddItemListViewAdapter extends BaseAdapter {

    static class ViewHolder {
        TextView category;
        TextView brand;
        TextView itemName;
        TextView description;
        ImageView imageView;
    }

    private LayoutInflater inflater;
    private int itemLayoutId;
    private String[] category;
    private String[] brand;
    private String[] itemName;
    private String[] description;
    private int[] ids;

    public AddItemListViewAdapter(Context context, int itemLayoutId,
                           String[] category,String[] brand,String[] itemName,
                           String[] description, int[] photos) {
        super();
        this.inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemLayoutId = itemLayoutId;
        this.category = category;
        this.brand = brand;
        this.itemName = itemName;
        this.description = description;

        this.ids = photos;
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
            holder.imageView = convertView.findViewById(R.id.imageView);
            holder.category = convertView.findViewById(R.id.category);
            holder.description = convertView.findViewById(R.id.descption);
            convertView.setTag(holder);
        }
        // holder を使って再利用
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        // holder の imageView にセット
        holder.imageView.setImageResource(ids[position]);
        // 現在の position にあるファイル名リストを holder の textView にセット
        holder.category.setText(category[position]);
        holder.brand.setText(brand[position]);
        holder.itemName.setText(itemName[position]);
        holder.description.setText(description[position]);
        return convertView;
    }

    @Override
    public int getCount() {
        // texts 配列の要素数
        return category.length;
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