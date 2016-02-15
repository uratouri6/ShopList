package com.example.murata.shoplist;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by murata on 2016/02/05.
 */
public class Shop implements Serializable {
    private String shopname;
    private String shopdata;
    private String saledata;
    private double latitude;
    private double longtude;
    private float distance = 0;

    public Shop(){}

    public Shop(String shopname, String shopdata, String saledata, double latitude, double longtude, float distance) {
        this.shopname = shopname;
        this.shopdata = shopdata;
        this.saledata = saledata;
        this.latitude = latitude;
        this.longtude = longtude;
        this.distance = distance;
    }

    public Shop(String shopname, String shopdata, String saledata, double latitude, double longtude) {
        this.shopname = shopname;
        this.shopdata = shopdata;
        this.saledata = saledata;
        this.latitude = latitude;
        this.longtude = longtude;
    }

    public String getShopName() {
        return shopname;
    }

    public String getShopData() {
        return shopdata;
    }

    public String getSaleData() {
        return saledata;
    }

    public Double getLatitude()
    {
        return latitude;
    }

    public Double getLongTude() {
        return longtude;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public void setShopdata(String shopdata) {
        this.shopdata = shopdata;
    }

    public void setSaledata(String saledata) {
        this.saledata = saledata;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongtude(double longtude) {
        this.longtude = longtude;
    }

    public int getDistance() { return (int) distance; }

    public  void setDistance(float distance) {
        this.distance = distance;
    }
}