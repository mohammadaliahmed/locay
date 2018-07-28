package com.locay;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by M Ali Ahmed on 7/29/2017.
 */


public class MyLocationListner implements LocationListener {
   // private FirebaseAnalytics mFirebaseAnalytics;
    Context context;
    public MyLocationListner(Context c){
        this.context=c;
    }



    public Location getLocation() {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(context, "permission not grandted", Toast.LENGTH_SHORT).show();
            return null;
        }
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isGPSEnabled) {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, this);
                Location l = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                return l;

            } else {

                Toast.makeText(context, "Please Enable Gps", Toast.LENGTH_SHORT).show();
            }
            return null;
        }



    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
