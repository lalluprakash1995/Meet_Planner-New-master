package com.example.lallu.meetingscheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DataBaseMeeting  extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBName1.db";
    public static final String MEETINGS_TABLE_NAME = "meetings";
    public static final String MEETINGS_COLUMN_ID = "id";
    public static final String MEETINGS_COLUMN_NAME = "meetingtitle";
    public static final String MEETINGS_COLUMN_EMAIL = "meetingdetails";
    public static final String MEETINGS_COLUMN_STREET = "date";
    public static final String MEETINGS_COLUMN_CITY = "time";
    public static final String MEETINGS_COLUMN_PHONE = "location";
    private HashMap hp;
    public DataBaseMeeting(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table MEETINGS1 " +
                        "(id integer primary key, meetingtitle text,meetingdetails text,date text, time text,location text)"

        );

        Log.e("Result","created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS MEETINGS1");
        onCreate(sqLiteDatabase);
    }
    public boolean insertmeeting (String meetingtitle, String meetingdetails, String date, String time,String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("meetingtitle", meetingtitle);
        contentValues.put("meetingdetails", meetingdetails);
        contentValues.put("date", date);
        contentValues.put("time", time);
        contentValues.put("location", location);
        db.insert("MEETINGS1", null, contentValues);
        return true;
    }
    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from meetings where id="+id+"", null );
        return res;
    }
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, MEETINGS_TABLE_NAME);
        return numRows;
    }
    public boolean updatemeeting (Integer id, String meetingtitle, String meetingdetails, String date, String time,String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("meetingtitle", meetingtitle);
        contentValues.put("meetingdetails", meetingdetails);
        contentValues.put("date", date);
        contentValues.put("time", time);
        contentValues.put("location", location);
        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }
    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("meetings",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }
    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from meetings", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(MEETINGS_COLUMN_NAME)));
            res.moveToNext();
            //testfor comm
        }
        return array_list;
    }

}
