package com.example.jjw26.parkingpal;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {
    public DataBaseHelper helper;
    public ArrayList<String> myList;
    public ArrayList<Spot> myDataList;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void getHistory(){
        helper = new DataBaseHelper(getActivity());


        myList = new ArrayList<>();
        myDataList = new ArrayList<>();
        Cursor cursor = helper.getData();
        cursor.moveToFirst();
        int x = cursor.getCount();
        int y = cursor.getColumnCount();
        for (int i = 0; i < cursor.getCount(); i++) {
            myList.add(cursor.getString(0)+"  "+cursor.getString(3) + "    " + cursor.getString(4));

            Spot geoSpot = new Spot();
            geoSpot.setId(cursor.getString(0));
            geoSpot.setLatitude(cursor.getString(1));
            geoSpot.setLongitude(cursor.getString(2));
            geoSpot.setDate(cursor.getString(3));
            geoSpot.setActive(cursor.getString(4));
            myDataList.add(geoSpot);

            cursor.moveToNext();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_history, container, false);

        this.getHistory();

        ListAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, myList);
        ListView listView = (ListView) view.findViewById(R.id.listViewId);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                MainActivity.myLatitude = myDataList.get(position).getLatitude();
                MainActivity.myLongitude = myDataList.get(position).getLongitude();
                MainActivity.mId = myDataList.get(position).getId();
                Fragment fragment;
                FragmentManager fragManager = getFragmentManager();
                FragmentTransaction transaction = fragManager.beginTransaction();
                fragment = new MapFragment();
                transaction.replace(R.id.fragment_place, fragment).addToBackStack("tag");
                transaction.commit();
            }
        });

        return view;
    }

    public void clearHistory(View v){}

}
