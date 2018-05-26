package com.example.jjw26.parkingpal;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CountDownFragment extends Fragment {


    private CountDownTimer cdt;
    private boolean timeRemaining;
    private TextView countDownTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_count_down, container, false);



        cdt = new CountDownTimer(MainActivity.timeLeftInMillis, 1000){
            @Override
            public void onTick(long l){
                MainActivity.timeLeftInMillis = l;
                updateTimer();
            }
            @Override
            public void onFinish(){

            }
        }.start();



        countDownTextView = (TextView)view.findViewById(R.id.countDownTimerId);



        return view;
    }


    public void updateTimer(){
        int hours = (int)MainActivity.timeLeftInMillis/3600000;
        int minutes = (int) MainActivity.timeLeftInMillis % 3600000 / 60000;
        int seconds = (int) MainActivity.timeLeftInMillis % 60000 / 1000;
        String hour = String.format("%02d", hours);
        String minute = String.format("%02d", minutes);
        String second = String.format("%02d", seconds);
        String timeLeftText = hour+":"+minute+":"+second;
        countDownTextView.setText(timeLeftText);
    }


}
