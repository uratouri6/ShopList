package com.example.murata.shoplist;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private LocationManager mLocationManager;
    private double latitude1 = 35.748068;
    private double longitude1 = 139.806256;
    private Handler h = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            // 位置情報の取得が許可されているかチェック
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // 権限があればLocationManagerを取得
                Toast.makeText(MainActivity.this, "位置情報の取得は既に許可されています", Toast.LENGTH_SHORT).show();
                initLocationManger();
            } else {
                // なければ権限を求めるダイアログを表示
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            // Android 6.0以下の場合
        } else {
            // インストール時点で許可されているのでチェックの必要なし
            Toast.makeText(MainActivity.this, "位置情報の取得は既に許可されています(Android 5.0以下です)", Toast.LENGTH_SHORT).show();
            initLocationManger();
        }
    }

    public float getDistance(double x, double y, double x2, double y2) {
        // 結果を格納するための配列を生成
        float[] results = new float[3];
        // 距離計算
        Location.distanceBetween(x, y, x2, y2, results);
        return results[0];
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // GPSの権限を求めるコード
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            // 許可されたら
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // テキストを表示してLocationManagerを取得
                Toast.makeText(this, "位置情報取得が許可されました", Toast.LENGTH_SHORT).show();
                initLocationManger();
                // 許可されなかったら
            } else {
                // 何もしない
                Toast.makeText(this, "位置情報取得が拒否されました", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initLocationManger() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0, // 位置情報更新を行う最低更新時間間隔（ms）
                0, // 位置情報更新を行う最小距離間隔（メートル）
                new LocationListener() {
                    // ロケーションが変更された時の動き
                    @Override
                    public void onLocationChanged(Location location) {
                        // 権限のチェック
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                            return;

                        // テキストとログに位置情報を表示
                        Toast.makeText(MainActivity.this, "位置情報が更新されました", Toast.LENGTH_SHORT).show();
                        latitude1  = location.getLatitude();
                        longitude1 = location.getLongitude();

                        //mLocationManager.removeUpdates(this);

                        setList();
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        Toast.makeText(MainActivity.this, "GPS provide is disabled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        Toast.makeText(MainActivity.this, "GPS provide is enabled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        Toast.makeText(MainActivity.this, "GPS provide status changed", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void setList(){
        // Construct the data source

        final RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        final String url = "http://www.icrus.org/murata/shoplist.php";
        final ArrayList<Shop> arrayOfShops = new ArrayList<Shop>();
        final ShopAdapter adapter = new ShopAdapter(this, arrayOfShops);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ResponseEntity<Shop[]> responseEntity = template.exchange(url, HttpMethod.GET, null, Shop[].class);
                Shop[] res = responseEntity.getBody();

                ArrayList<Shop> shops = new ArrayList<Shop>(Arrays.asList(res));
                // Create the adapter to convert the array to views

                for(Shop thisshop: shops){
                    thisshop.setDistance(getDistance(latitude1, longitude1, thisshop.getLatitude(), thisshop.getLongTude()));
                }

                Collections.sort(shops, itemComparator);
                arrayOfShops.addAll(shops);
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        thread.start();

        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                Shop selectedShop = adapter.getItem(position);
                // position を「imagePath」のキーワードでインテントにセット
                intent.putExtra("shop", selectedShop);
                // Activity をスイッチする
                startActivity(intent);
            }
        });
    }

    Comparator<Shop> itemComparator = new Comparator<Shop>() {
        public int compare(Shop shop1, Shop shop2) {

            return new Integer(shop1.getDistance()).compareTo(new Integer(shop2.getDistance()));

        }

    };


}
