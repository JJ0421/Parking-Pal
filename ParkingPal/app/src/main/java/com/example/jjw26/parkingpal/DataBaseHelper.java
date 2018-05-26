package com.example.jjw26.parkingpal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by jjw26 on 11/28/2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "GEO_Location";
    private static final String COL1 = "ID";
    private static final String COL2 = "Latitude";
    private static final String COL3 = "Longitude";
    private static final String COL4 = "Date";
    private static final String COL5 = "Active";

    public DataBaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,  " + COL2 + " TEXT, " + COL3 + " TEXT, " + COL4 + " TEXT, " + COL5 + " INTEGER)";
        sqLiteDatabase.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP IF TABLE EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
    }

    public boolean addData(String lat, String lon, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        //This will be removed once history is implemented
        //String del = "DELETE FROM "+TABLE_NAME;
        //String del = "DROP TABLE "+TABLE_NAME;
        //db.execSQL(del);

        try {
            String sql = "SELECT * FROM " + TABLE_NAME;
            Cursor cursor = db.rawQuery(sql, null);

            if (cursor.getCount() > 0) {
                sql = "UPDATE " + TABLE_NAME
                        + " SET " + COL5 + " = 0 "
                        + " WHERE " + COL1 + " = (SELECT " + COL1 + " FROM "
                        + TABLE_NAME + " WHERE " + COL5 + " = 1)";
                db.execSQL(sql);
            }
        }catch(Exception ex){
            String str = ex.toString();
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, lat);
        contentValues.put(COL3, lon);
        contentValues.put(COL4, date);
        contentValues.put(COL5, 1);
        Log.d(TAG, "addData: Adding " + lat + ", " + lon + ", " + date + " to " + TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean clearHistory(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM "+TABLE_NAME;
        db.execSQL(sql);
        sql = "SELECT * FROM "+TABLE_NAME;
        Cursor data = db.rawQuery(sql, null);

        if(data.getCount() == 0){
            return true;
        }else{
            return false;
        }
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlstr = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL1 + " DESC";
        Cursor data = db.rawQuery(sqlstr, null);
        return data;
    }

    public boolean deleteRow(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM "+TABLE_NAME;
        Cursor data = db.rawQuery(sql, null);
        int pCount = data.getCount();
        sql = "DELETE FROM "+TABLE_NAME+" WHERE "+COL1+" = "+id;
        db.execSQL(sql);
        sql = "SELECT * FROM "+TABLE_NAME;
        data = db.rawQuery(sql, null);
        if(data.getCount() == pCount-1) {
            return true;
        }else{
            return false;
        }
    }

    public boolean updateRow(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAME
                + " SET " + COL5 + " = 0 "
                + " WHERE " + COL1 + " = (SELECT " + COL1 + " FROM "
                + TABLE_NAME + " WHERE " + COL5 + " = 1)";
        db.execSQL(sql);
        sql = "UPDATE "+TABLE_NAME+" SET "+COL5+" = 1 WHERE "+COL1+" = "+id;
        db.execSQL(sql);
        sql = "SELECT * FROM "+TABLE_NAME+" WHERE "+COL5+" = 1";
        Cursor data = db.rawQuery(sql, null);
        data.moveToFirst();
        if(data.getString(0).equals(id)) {
            return true;
        }else{
            return false;
        }
    }
}
