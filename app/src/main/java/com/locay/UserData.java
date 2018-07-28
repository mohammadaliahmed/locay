package com.locay;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by M Ali Ahmed on 7/25/2017.
 */

public class UserData {
    String phone,longitude,latitude,time;

    FirebaseAnalytics mFirebaseAnalytics;

    public UserData(String phone, String latitude,String longitude , String time) {
        this.phone = phone;
        this.longitude = longitude;
        this.latitude = latitude;

        this.time = time;
    }

    public UserData() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }





    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
