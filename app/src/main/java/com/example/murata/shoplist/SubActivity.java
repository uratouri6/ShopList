package com.example.murata.shoplist;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Intent intent = getIntent();
        final Shop shop = (Shop)getIntent().getSerializableExtra("shop");
        TextView textview1 = (TextView) findViewById(R.id.shopname);
        textview1.setText(shop.getShopName());
        TextView textview2 = (TextView) findViewById(R.id.shopdata);
        textview2.setText(shop.getShopData());
        TextView textview3 = (TextView) findViewById(R.id.saledata);
        textview3.setText(shop.getSaleData());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("ShopData");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button button = (Button) findViewById(R.id.map);
        // ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ボタンがクリックされた時に呼び出されます
                Uri gmUri = Uri.parse("google.navigation:q=" + String.valueOf(shop.getLatitude())  + "," + String.valueOf(shop.getLongTude()) + "&mode=w");
                Intent gmIntent = new Intent(Intent.ACTION_VIEW, gmUri);
                gmIntent.setPackage("com.google.android.apps.maps");
                startActivity(gmIntent);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        boolean result = true;

        switch (id) {
            case android.R.id.home:
                finish();
                break;
            default:
                result = super.onOptionsItemSelected(item);
        }

        return result;
    }
}
