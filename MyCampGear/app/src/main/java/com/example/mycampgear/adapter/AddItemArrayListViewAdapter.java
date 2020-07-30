package com.example.mycampgear.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycampgear.entity.ItemEntity;
import com.example.mycampgear.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddItemArrayListViewAdapter extends ArrayAdapter<ItemEntity> {

    private int mResource;
    private List<ItemEntity> mItems;
    private LayoutInflater mInflater;


    // 外部から呼び出し可能なマップ
    public Map<Integer,Boolean> checkList = new HashMap<>();


    private LayoutInflater inflater;
    private int itemLayoutId;
    private String[] category;
    private String[] brand;
    private String[] itemName;
    private String[] description;
    private int[] ids;

    public AddItemArrayListViewAdapter(Context context, int resource, List<ItemEntity> items) {
        super(context, resource, items);
        mResource = resource;
        mItems = items;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 初期値を設定する
        for(int i=0; i<items.size();i++){
            ItemEntity map = (ItemEntity)items.get(i);
            checkList.put(i,(Boolean)map.isCheckd());
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView != null) {
            view = convertView;
        }
        else {
            view = mInflater.inflate(mResource, null);
        }

        // リストビューに表示する要素を取得
        ItemEntity item = mItems.get(position);

        // サムネイル画像を設定
        ImageView thumbnail = (ImageView)view.findViewById(R.id.imageView);
        if(item.getImage()!=null){
            thumbnail.setImageBitmap(item.getImage());

        }else{
            thumbnail.setImageResource(R.drawable.no_image);
        }

        // Category
        TextView category = (TextView)view.findViewById(R.id.label_category);
        category.setText(item.getCategory());

        TextView itemId = (TextView)view.findViewById(R.id.itemId);
        itemId.setText(String.valueOf(item.getItemId()));

        TextView brand = (TextView)view.findViewById(R.id.brand);
        brand.setText(item.getBrand());

        TextView itemName = (TextView)view.findViewById(R.id.label_itemName);
        itemName.setText(item.getItemName());

        TextView description = (TextView)view.findViewById(R.id.descption);
        description.setText(item.getDescription());


        CheckBox ch = view.findViewById(R.id.checkbox_1);

        // チェックの状態が変化した場合はマップに記憶する
        ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkList.put(position,isChecked);
            }
        });



        return view;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}