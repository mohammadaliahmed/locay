package com.locay;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.DateFormat;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class MainScreen extends AppCompatActivity implements OnMapReadyCallback {
    private DatabaseReference mDatabase;

    LocationManager locationManager;
    LocationListener locationListener;
    static double lat, lng;

    FirebaseAnalytics mFirebaseAnalytics;


    Button share, request;
    static String phoneee, finalPhone;

    private GoogleMap mMap;
    String currentDateTimeString;
    private InterstitialAd mInterstitialAd;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        request = (Button) findViewById(R.id.requestlocation);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();


        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // String currentDateTimeString = DateFormat.getDateTimeInstance().format("yyyy-MM-dd hh:mm:ss a",new Date());
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        currentDateTimeString = (String) df.format("dd-MM-yyyy hh a", new Date());


        //analytics


        share = (Button) findViewById(R.id.sharelocation);

        phoneee = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        TextView nm = (TextView) findViewById(R.id.textView2);

        if (phoneee.substring(0, 3).equals("+92")) {

            finalPhone = phoneee.substring(3, 13);
            finalPhone = "0" + finalPhone;
        }

        nm.setText(phoneee);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

      // ActivityCompat.requestPermissions(MainScreen.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION },123);


        //permission checks

        if (ContextCompat.checkSelfPermission(MainScreen.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainScreen.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MainScreen.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);



            } else {
                ActivityCompat.requestPermissions(MainScreen.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }

        } else {
            //do nothing

        }
        mInterstitialAd = new InterstitialAd(MainScreen.this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8010167933323069/8411687508");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        locationManager();
        int secondsDelayed = 5;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }


            }
        }, secondsDelayed * 1000);



        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainScreen.this, PickContactToShare.class);
                startActivity(i);
            }
        });


        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreen.this, PickContactToRequest.class);
                startActivity(intent)
                ;


            }
        });

    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(sydney).title(phoneee));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (item.getItemId() == R.id.signout) {
            Intent signIn = new Intent(MainScreen.this, PhoneVerification.class);
            startActivity(signIn);
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Toast.makeText(MainScreen.this, "You have been signed out", Toast.LENGTH_SHORT).show();
                    finish();

                }
            });
        }

        return super.onOptionsItemSelected(item);
    }
    public  void locationManager(){

        MyLocationListner ml = new MyLocationListner(getApplicationContext());
        Location l = ml.getLocation();
        if (l != null) {
            lat = l.getLatitude();
            lng = l.getLongitude();
            Toast.makeText(MainScreen.this, String.valueOf(lat) + String.valueOf(lng), Toast.LENGTH_SHORT).show();
            mDatabase.child(finalPhone).setValue(new UserData(finalPhone, String.valueOf(lat), String.valueOf(lng), currentDateTimeString));

        }
    }


}
