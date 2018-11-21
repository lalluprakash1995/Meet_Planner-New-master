package com.example.lallu.meetingscheduler;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
    public static final String TAG="RESULT";
    public static final String DATABASE_NAME="MyMeetings.db";
    public static final String TABLE_NAME="SchedledMeetings";
    public static final String KEY_ID="Meeting_Id";
    public static final String KEY_DATE="Meeting_Date";
    public static final String KEY_TITLE="Meeting_title";
    public static final String KEY_AGENDA="Meeting_Agenda";
    public static final String KEY_SCHEDULED_AT="Meeting_Scheduled";
    public static final String KEY_TIME_START="Start_Time";
    public static final String KEY_TIME_ENDS="End_Time";
    public static final String KEY_CONTACTS="Contacts";
    public static final String KEY_LOCATION="Meeting_Location";
    public static final String KEY_MEETING="Meeting_head";
    private HashMap MeetingData;





    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null,1);
        Log.e(TAG,"dbcreated");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(
//                "create table SchedledMeetings " +
//                        "(Meeting_Id integer primary key,Meeting_Date text, Meeting_title text " +
//                        ",Meeting_Agenda text,Meeting_Scheduled text, Start_Time text,End_Time text,Contacts text,Meeting_Location text)"
//        );
        db.execSQL(
                "create table SchedledMeetings " +
                        "(Meeting_Id integer primary key,Meeting_Date date, Meeting_title text " +
                        ",Meeting_Agenda text,Meeting_Scheduled date, Start_Time text,End_Time text,Contacts text,Meeting_Location text)"
        );
        Log.e(TAG,"Table Created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query;
        query = "DROP TABLE IF EXISTS SchedledMeetings";
        db.execSQL(query);
        onCreate(db);
        Log.e(TAG,"Table Exist");



    }
    public boolean insertMeetings(String meetdate,String meetintitle,String meetagenda,String scheduleat,String starttime,String endtime,String phn,String location){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

       // contentValues.put("Meeting_head","AAAAA");

        contentValues.put("Meeting_Date",meetdate);
        contentValues.put("Meeting_title",meetintitle);
        contentValues.put("Meeting_Agenda",meetagenda);
        contentValues.put("Meeting_Scheduled",scheduleat);
        contentValues.put("Start_Time",starttime);
        contentValues.put("End_Time",endtime);
        contentValues.put("Contacts",phn);
        contentValues.put("Meeting_Location",location);
        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        Log.e(TAG,"Inserted");
        return true;


    }
    public Cursor getData(String addeddate) {
        SQLiteDatabase db = this.getReadableDatabase();
     //   Cursor res =  db.rawQuery( "select * from SchedledMeetings where Meeting_Id="+addeddate+"", null );
        Cursor res =  db.rawQuery( "select * from SchedledMeetings where Meeting_title='"+addeddate+"'", null );

        return res;
    }
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    //update table
    public boolean updatemeeting (String meetdate,String meetintitle,String meetagenda,String scheduleat,String starttime,String endtime,String phn,String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Meeting_Date",meetdate);
        contentValues.put("Meeting_title",meetintitle);
        contentValues.put("Meeting_Agenda",meetagenda);
        contentValues.put("Meeting_Scheduled",scheduleat);
        contentValues.put("Start_Time",starttime);
        contentValues.put("End_Time",endtime);
        contentValues.put("Contacts",phn);
        contentValues.put("Meeting_Location",location);
        db.update(TABLE_NAME, contentValues, "Meeting_title =? ", new String[] { meetintitle } );
        return true;
    }








    public Integer deleteContact (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
//        return db.delete("SchedledMeetings",
//                "Meeting_Id= ? ",
//                new String[] { Integer.toString(id) });
        return db.delete("SchedledMeetings",
                "Meeting_title= ? ",
                new String[] { id });
    }

    //Delete all meeting for today

    //Delete all meetings for
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteAll()
    {
        //SQLiteDatabase db = this.getWritableDatabase();
        // db.delete(TABLE_NAME,null,null);
        SQLiteDatabase db = this.getWritableDatabase();

       // db.execSQL("delete  from "+ TABLE_NAME);
        //db.execSQL("TRUNCATE table" + TABLE_NAME);
        Date c = Calendar.getInstance().getTime(); System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String current_Date = df.format(c);
        Log.e("Current_Date=",current_Date);
      db.execSQL("DELETE  FROM SchedledMeetings  ");
        db.close();
    }
    // Delete tomoorows meeting
    public Integer deleteTomorrow(String title){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete("SchedledMeetings","Meeting_Scheduled= ?",new String[]{title});

    }



// get All Dates
public ArrayList<String> getAllDates() {
    ArrayList<String> array_list = new ArrayList<String>();

    //hp = new HashMap();
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor res =  db.rawQuery( "select * from SchedledMeetings ORDER BY Meeting_Scheduled ASC", null );
    res.moveToFirst();

    while(res.isAfterLast() == false){
        array_list.add(res.getString(res.getColumnIndex(KEY_SCHEDULED_AT)));
        res.moveToNext();
    }
    return array_list;
}



    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select Meeting_title from SchedledMeetings ORDER BY Meeting_Scheduled ASC", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(KEY_TITLE)));
            res.moveToNext();
        }
        return array_list;
    }
//select by date
@RequiresApi(api = Build.VERSION_CODES.N)
public ArrayList<String> getAllMeetings() {
    ArrayList<String> array_list = new ArrayList<String>();

    //hp = new HashMap();
    SQLiteDatabase db = this.getReadableDatabase();
    //Cursor res =  db.rawQuery( "select * from SchedledMeetings", null );
    // choose current date
    Date c = Calendar.getInstance().getTime(); System.out.println("Current time => " + c);
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    String current_Date = df.format(c);
    Log.e("Current_Date=",current_Date);

    // next week date

    String dt = current_Date;
    int x = 7;
    Calendar cal = GregorianCalendar.getInstance();
    cal.add( Calendar.DAY_OF_YEAR, x);
    Date sevenDaysAfter = cal.getTime();
    String nextweek=df.format(sevenDaysAfter);
    Log.e("next",nextweek);









    try {
        Cursor res = db.rawQuery("SELECT * FROM SchedledMeetings WHERE Meeting_Scheduled BETWEEN '"+current_Date+"' AND '"+nextweek+"';", null);
        //Cursor res=db.rawQuery("select * from SchedledMeetings where `Meeting_Scheduled` >= '"+current_Date+"' and `Meeting_Scheduled` <= '"+nextweek+"'",null);


        res.moveToFirst();

        while (res.isAfterLast() == false) {
           // array_list.add(res.getString(res.getColumnIndex(KEY_TITLE)));
            array_list.add(res.getString(res.getColumnIndex(KEY_TITLE)));
            //for testing

            for (int i=0;i<array_list.size();i++ ){
                Log.e("Arrary list:",array_list.get(i));
            }
            res.moveToNext();
        }

    }
    catch (Exception e){
        Log.e("Result","no data");
    }
    return array_list;
}


// Tommorrows meeting
@RequiresApi(api = Build.VERSION_CODES.N)
public ArrayList<String> getTommorrowsMeeting() {
    ArrayList<String> array_list = new ArrayList<String>();
    SQLiteDatabase db = this.getReadableDatabase();
    //Current date
    Date c = Calendar.getInstance().getTime(); System.out.println("Current time => " + c);
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    String current_Date = df.format(c);
    Log.e("Current_Date=",current_Date);
    //tommoroow
    String dt = current_Date;
    int x = 1;
    Calendar cal = GregorianCalendar.getInstance();
    cal.add( Calendar.DAY_OF_YEAR, x);
    Date sevenDaysAfter = cal.getTime();
    String nextday=df.format(sevenDaysAfter);
    Log.e("next",nextday);

    try {
        Cursor res = db.rawQuery("SELECT * FROM SchedledMeetings WHERE Meeting_Scheduled= '"+nextday+"';", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(KEY_TITLE)));
            res.moveToNext();
        }

    }
    catch (Exception e){
        Log.e("Result","no data");
    }
    return array_list;
}


    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Integer deletealltodaysmeeting(String id) {
        Date c = Calendar.getInstance().getTime(); System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String current_Date = df.format(c);
        Log.e("Current_Date=",current_Date);
        SQLiteDatabase db = this.getWritableDatabase();
//        return db.delete("SchedledMeetings",
//                "Meeting_Id= ? ",
//                new String[] { Integer.toString(id) });
        return db.delete("SchedledMeetings",
                "Meeting_Date= "+current_Date,
                new String[] { id });


    }

    //Todays Meeting
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<String> getTodayMeeting() {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        //Current date
        Date c = Calendar.getInstance().getTime(); System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String current_Date = df.format(c);
        Log.e("Current_Date=",current_Date);


        try {
            Cursor res = db.rawQuery("SELECT * FROM SchedledMeetings WHERE Meeting_Scheduled= '"+current_Date+"';", null);
            res.moveToFirst();

            while (res.isAfterLast() == false) {
                array_list.add(res.getString(res.getColumnIndex(KEY_TITLE)));
                res.moveToNext();
            }

        }
        catch (Exception e){
            Log.e("Result","no data");
        }
        return array_list;
    }

}
