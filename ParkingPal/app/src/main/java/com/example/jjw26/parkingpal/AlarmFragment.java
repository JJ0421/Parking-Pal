package com.example.jjw26.parkingpal;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;


public class AlarmFragment extends Fragment {
    public void startTimerClick(View view){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        MainActivity.mHour = 0;
        MainActivity.mMinute = 0;
        MainActivity.mSecond = 0;

        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        };

        NumberPicker noPicker = null;
        noPicker = (NumberPicker)view.findViewById(R.id.numberPicker);
        noPicker.setMaxValue(59);
        noPicker.setMinValue(0);
        noPicker.setWrapSelectorWheel(true);
        noPicker.setFormatter(formatter);
        noPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                MainActivity.mHour = newVal;
            }
        });


        NumberPicker noPicker2 = null;
        noPicker2 = (NumberPicker)view.findViewById(R.id.numberPicker2);
        noPicker2.setMaxValue(59);
        noPicker2.setMinValue(0);
        noPicker2.setWrapSelectorWheel(true);
        noPicker2.setFormatter(formatter);
        noPicker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                MainActivity.mMinute = newVal;
            }
        });

        NumberPicker noPicker3 = null;
        noPicker3 = (NumberPicker)view.findViewById(R.id.numberPicker3);
        noPicker3.setMaxValue(59);
        noPicker3.setMinValue(0);
        noPicker3.setWrapSelectorWheel(true);
        noPicker3.setFormatter(formatter);
        noPicker3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                MainActivity.mSecond = newVal;
            }
        });

        return view;
    }


}
