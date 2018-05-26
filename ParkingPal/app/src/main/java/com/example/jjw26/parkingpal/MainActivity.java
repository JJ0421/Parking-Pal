package com.example.jjw26.parkingpal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URI;
import java.util.Date;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    public static String myLatitude;
    public static String myLongitude;
    public static String mId;
    public static String savedLat;
    public static String savedLon;

    public static int mHour;
    public static int mMinute;
    public static int mSecond;
    public static boolean timeRunning = false;
    public static long timeLeftInMillis;


    public double lon = 0;
    public double lat = 0;
    public DataBaseHelper helper;
    public GPStracker g;

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            FragmentManager fragManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    transaction.replace(R.id.fragment_place, fragment).addToBackStack("homeTag");
                    transaction.commit();
                    return true;
                case R.id.navigation_history:
                    fragment = new HistoryFragment();
                    transaction.replace(R.id.fragment_place, fragment).addToBackStack("historyTag");
                    transaction.commit();
                    return true;
                case R.id.navigation_garage:
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=centurylink+field+parking"));
                    startActivity(intent);
                    return true;
                case R.id.navigation_alarm:
                    if(timeRunning == false) {
                        fragment = new AlarmFragment();
                        transaction.replace(R.id.fragment_place, fragment).addToBackStack("alarmTag");
                        transaction.commit();
                        return true;
                    }else{
                        fragment = new CountDownFragment();
                        transaction.replace(R.id.fragment_place, fragment).addToBackStack("alarmTag");
                        transaction.commit();
                        return true;
                    }

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        helper = new DataBaseHelper(this);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Fragment fragment;
        FragmentManager fragManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragManager.beginTransaction();
        fragment = new HomeFragment();
        transaction.add(R.id.fragment_place, fragment);
        transaction.commit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);


        //Ad Stuff
        //MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);

    }

    //Home frag logic---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void getSpotClick(android.view.View v) {
        double oLat = 0;
        double oLon = 0;
        boolean check = false;
        int count = 0;
        while (count < 3 && check == false) {
            this.g = new GPStracker(getApplicationContext());
            Location l = g.getLocation();
            if (l != null) {
                oLat = l.getLatitude();
                oLon = l.getLongitude();
                check = true;
            }
            count++;
        }

        if (check == false) {
            Toast.makeText(getApplicationContext(), "Current location could not be determined", Toast.LENGTH_LONG).show();
        }

        Cursor data = helper.getData();
        data.moveToFirst();
        //String myLat = data.getString(1);
        //String myLon = data.getString(2);
        String myLat = savedLat;
        String myLon = savedLon;
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" + oLat + "," + oLon + "&destination=" + myLat + "," + myLon + "&travelmode=walking"));a
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" + oLat + "," + oLon + "&destination=" + myLon + "," + myLat + "&travelmode=walking"));
        startActivity(intent);

    }

    public void setSpotClick(View v) {
        this.g = new GPStracker(getApplicationContext());
        Location l = g.getLocation();
        if (l != null) {
            this.lat = l.getLatitude();
            this.lon = l.getLongitude();
            String latitude = Double.toString(this.lat);
            String longitude = Double.toString(this.lon);
            Date cDate = new Date();
            String currentDate = cDate.toString();
            addData(latitude, longitude, currentDate);
            savedLat = latitude;
            savedLon = longitude;
        }

    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //History Frag Logic-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void clearHistory(View v){


        helper.clearHistory();

        Fragment fragment;
        FragmentManager fragManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragManager.beginTransaction();
        fragment = new HistoryFragment();
        transaction.replace(R.id.fragment_place, fragment);
        transaction.commit();
    }


    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //Map Frag Logic---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void deleteSpotClick(View view){
        Boolean checker = helper.deleteRow(mId);
        FragmentManager fragManager = getSupportFragmentManager();
        fragManager.popBackStackImmediate();
        if(checker){
            Toast.makeText(getApplicationContext(), "Spot was successfully deleted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Unable to delete spot", Toast.LENGTH_SHORT).show();
        }
    }
    public void setSavedSpotClick(View view){
        boolean checker = helper.updateRow(mId);
        FragmentManager fragManager = getSupportFragmentManager();
        fragManager.popBackStackImmediate();
        if(checker){
            Toast.makeText(getApplicationContext(), "Current spot successfully updated", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Unable to update to current spot", Toast.LENGTH_SHORT).show();
        }
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //AlarmFrag Logic-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void startTimerClick(View view){
        timeLeftInMillis = (mHour * 3600000) + (mMinute * 60000) + (mSecond * 1000);
        Fragment fragment;
        FragmentManager fragManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragManager.beginTransaction();
        fragment = new CountDownFragment();
        transaction.replace(R.id.fragment_place, fragment).addToBackStack("alarmTag");
        transaction.commit();
        timeRunning = true;
    }





    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void addData(String lat, String lon, String date) {
        boolean insertData = helper.addData(lat, lon, date);
        if (insertData) {
            Toast.makeText(getApplicationContext(), "Spot saved!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Unable to save spot", Toast.LENGTH_SHORT).show();
        }

    }

}
