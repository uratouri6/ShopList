package com.example.murata.shoplist;

/**
 * Created by murata on 2016/02/05.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by murata on 2016/02/05.
 */
public class ShopAdapter extends ArrayAdapter<Shop> {
    public ShopAdapter(Context context, ArrayList<Shop> shops) {
        super(context, 0, shops);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Shop shop = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_shop, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvHome = (TextView) convertView.findViewById(R.id.tvData);
        // Populate the data into the template view using the data object
        tvName.setText(shop.getShopName());
        if(shop.getDistance()<10000){
            tvHome.setText(String.valueOf(shop.getDistance()) + "m");
        }else {
            tvHome.setText("10km以上");
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
