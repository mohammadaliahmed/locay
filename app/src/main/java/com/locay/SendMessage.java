package com.locay;

import android.*;
import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static java.lang.Thread.MAX_PRIORITY;
import static java.lang.Thread.sleep;

public class SendMessage extends AppCompatActivity {


     FirebaseAnalytics mFirebaseAnalytics;
    String name;
    String number;
    String msgg="";
    EditText msg,time;
    DatabaseReference mDatabase;
    private InterstitialAd mInterstitialAd;
    //String timee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        //Back button
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (ContextCompat.checkSelfPermission(SendMessage.this,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SendMessage.this,
                    android.Manifest.permission.SEND_SMS)) {
                ActivityCompat.requestPermissions(SendMessage.this,
                        new String[]{android.Manifest.permission.SEND_SMS},1);



            } else {
                ActivityCompat.requestPermissions(SendMessage.this,
                        new String[]{android.Manifest.permission.SEND_SMS}, 1);
            }

        } else {
            //do nothing

        }

    mDatabase= FirebaseDatabase.getInstance().getReference("users").child(MainScreen.finalPhone);

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name","");
        number = bundle.getString("number","");
        TextView tv=(TextView)findViewById(R.id.textView3);
        tv.setText(name+"   "+number);

        if(number.substring(0,3).equals("+92")){
            number=number.substring(3,13);
            number="0"+number;
        }

        msg=(EditText)findViewById(R.id.msgfield);

        //ad
        mInterstitialAd = new InterstitialAd(SendMessage.this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8010167933323069/8411687508");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());



        FloatingActionButton floatingActionButton=(FloatingActionButton)findViewById(R.id.sendmsg);
        msgg=msg.getText().toString();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    msgg=msg.getText().toString();
                    notifyUser();


                    smsManager.sendTextMessage(number, null, "Shared his location for 5 minute .\n Tap to view\n" +
                            "http://www.locay.com/location?user="+MainScreen.finalPhone+"\n\n"+msgg, null, null);
                    Toast.makeText(SendMessage.this, "Message Sent", Toast.LENGTH_SHORT).show();
                    mDatabase.child("shareable").setValue("1");

                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }



                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });
        int secondsDelayed = 5;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                mDatabase.child("shareable").setValue("0");

            }
        }, secondsDelayed * 1000*60);


            }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void  notifyUser(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.iccon)
                        .setContentTitle("Location Shared Successfully!")
                        .setPriority(MAX_PRIORITY)
                        .setContentText("You shared your location to "+number);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainScreen.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainScreen.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


// mNotificationId is a unique integer your app uses to identify the
// notification. For example, to cancel the notification, you can pass its ID
// number to NotificationManager.cancel().
        mNotificationManager.notify(0, mBuilder.build());

    }
}
