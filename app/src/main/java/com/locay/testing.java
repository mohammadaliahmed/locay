package com.locay;

import android.*;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.locay.MainScreen.lat;
import static com.locay.MainScreen.lng;

public class testing extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    String user;
    String lati;
    String longi;
    ArrayList aList;
    Double latitude,longitude;


    private FirebaseAnalytics mFirebaseAnalytics;




    public DatabaseReference mDatabase;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
         Bundle bundle = new Bundle();

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);





        //Back button
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }





        progress= new ProgressDialog(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        TextView tv=(TextView)findViewById(R.id.textView4);


        Intent intent = getIntent();
        String action = intent.getAction();
        final Uri data = intent.getData();
        user=data.getQueryParameter("user");

        mDatabase=FirebaseDatabase.getInstance().getReference("users").child(user);

        mDatabase.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {
            // progress.setMessage("loading");
             //progress.show();

             UserData value=dataSnapshot.getValue(UserData.class);
             latitude=Double.parseDouble(value.getLatitude());
             longitude=Double.parseDouble(value.getLongitude());

                 LatLng loc = new LatLng(latitude,longitude);
                 mMap.addMarker(new MarkerOptions().position(loc).title(user));
                 mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));

             if (ActivityCompat.checkSelfPermission(testing.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(testing.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                 // TODO: Consider calling
                 //    ActivityCompat#requestPermissions
                 // here to request the missing permissions, and then overriding
                 //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                 //                                          int[] grantResults)
                 // to handle the case where the user grants the permission. See the documentation
                 // for ActivityCompat#requestPermissions for more details.
                 return;
             }

             mMap.setMyLocationEnabled(true);





         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

         }
     });

        tv.setText(user);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
