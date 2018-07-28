package com.locay;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;


public class Request extends AppCompatActivity {

    FirebaseAnalytics mFirebaseAnalytics;
    String user;
    String name;
    String number;
    String msgg1;
    EditText msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
         Bundle bbundle = new Bundle();

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bbundle);



        //Back button
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name","");
        number = bundle.getString("number","");
        TextView reqn=(TextView)findViewById(R.id.textView3);

        reqn.setText(name+"   "+number);

        msg=(EditText)findViewById(R.id.reqmsg);




        FloatingActionButton floatingActionButton=(FloatingActionButton)findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    msgg1=msg.getText().toString();

                    smsManager.sendTextMessage(number, null,"Is requesting your location.\n" +
                            "Tap to share\n" +
                            "http://www.locay.com/share"+"\n\n"+msgg1, null, null);
                    Toast.makeText(Request.this, "Request Sent", Toast.LENGTH_SHORT).show();
                    Intent main=new Intent(Request.this,MainScreen.class);
                    startActivity(main);
                    finish();



                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });





    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
