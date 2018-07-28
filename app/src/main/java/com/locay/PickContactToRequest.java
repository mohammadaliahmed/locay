package com.locay;

import android.*;
import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Arrays;

public class PickContactToRequest extends AppCompatActivity {
    FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_contact_to_request);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        ActivityCompat.requestPermissions(PickContactToRequest.this,new String[]{Manifest.permission.READ_CONTACTS },123);

mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
 Bundle bundle = new Bundle();

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);






        //Back button
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }









        ArrayList<PickContactToShare.Android_Contact> arrayList = doSomething(getApplicationContext());

        int size = arrayList.size();
        String[] tel = new String[size];
        String[] name1 = new String[size];
        int i = 0;
        for(i=0 ; i < size; i++)
        {
            tel[i] = arrayList.get(i).android_contact_Tel;
            name1[i] = arrayList.get(i).android_contact_name + ":"  + tel[i];
        }
        Arrays.sort(name1);
        ListAdapter list1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, name1);
        ListView buckyAdapter = (ListView) findViewById(R.id.contactlist2);
        buckyAdapter.setAdapter(list1);

        buckyAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent i = new Intent(getApplicationContext(), Request.class);
                Object item = adapterView.getItemAtPosition(position);
                String namepass[] = item.toString().split(":");
                String name = namepass[0];
                String nmbr = namepass[1];
                i.putExtra("name",name);
                i.putExtra("number",nmbr);
                startActivity(i);
            }
        });
    }
    public static class Android_Contact {
        public String android_contact_name = "";
        public String android_contact_Tel = "";
        public int android_contact_ID = 0;
    }

    public ArrayList<PickContactToShare.Android_Contact> doSomething(Context context) {
        ArrayList<PickContactToShare.Android_Contact> arrayList_Android_Contacts = new ArrayList<PickContactToShare.Android_Contact>();

        Cursor cursor_Android_Contacts = null;
        ContentResolver contentResolver = context.getContentResolver();
        try {
            cursor_Android_Contacts = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        } catch (Exception ex) {
            Log.e("Error on contact", ex.getMessage());
        }
        if (cursor_Android_Contacts.getCount() > 0) {

            while (cursor_Android_Contacts.moveToNext()) {
                PickContactToShare.Android_Contact android_contact = new PickContactToShare.Android_Contact();
                String contact_id = cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts._ID));
                String contact_display_name = cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                android_contact.android_contact_name = contact_display_name;
                int hasPhoneNumber = Integer.parseInt(cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                            , null
                            , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?"
                            , new String[]{contact_id}
                            , null);

                    while (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        android_contact.android_contact_Tel = phoneNumber;
                    }
                    phoneCursor.close();
                }
                arrayList_Android_Contacts.add(android_contact);
            }
        }
        return arrayList_Android_Contacts;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    }

