package com.example.lallu.meetingscheduler;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TodaysMeeting extends AppCompatActivity {
    ListView todayMeet;
    String itemValue;
    public final static String EXTRA_MESSAGE = "MESSAGE";
    DBHelper mydbhelper;
    ArrayAdapter adapt;
    FloatingActionButton deletetodaymeetings;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_meeting);
        ActionBar ab=getSupportActionBar();
        ab.setTitle("Todays Meetings");
        deletetodaymeetings=(FloatingActionButton)findViewById(R.id.fab_delete_meetingtodaymeerting);
        mydbhelper=new DBHelper(this);
        ArrayList arrayList=mydbhelper.getTodayMeeting();
        if (arrayList.size()==0){
            final AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(TodaysMeeting.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(TodaysMeeting.this);
            }
            builder.setTitle("Alert...!! ?")
                    .setMessage("No Meetings Found..")

                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }


        adapt=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        todayMeet=(ListView)findViewById(R.id.lvtodaysmeetting);
        todayMeet.setAdapter(adapt);
        todayMeet.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                itemValue=(String)todayMeet.getItemAtPosition(arg2);
                //  Log.e("Selected Item",itemValue);
                // TODO Auto-generated method stub
                //  int id_To_Search = arg2 + 1;

                Bundle dataBundle = new Bundle();
                //   dataBundle.putInt("id", id_To_Search);
                dataBundle.putString("title",itemValue);

                Intent intent = new Intent(getApplicationContext(),ViewMeetings.class);

                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
        deletetodaymeetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(TodaysMeeting.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(TodaysMeeting.this);
                }
                builder.setTitle("Delete Meetings ?")
                        .setMessage("Are you sure you want to delete all scheduled meetings?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            public void onClick(DialogInterface dialog, int which) {
                                //Current date
                                Date c = Calendar.getInstance().getTime(); System.out.println("Current time => " + c);
                                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                String current_Date = df.format(c);
                                Log.e("Current_Date=",current_Date);


                                mydbhelper.deleteTomorrow(current_Date);

                                ArrayList arrayList=mydbhelper.getTommorrowsMeeting();

                                adapt=new ArrayAdapter(TodaysMeeting.this,android.R.layout.simple_list_item_1,arrayList);
                                todayMeet=(ListView)findViewById(R.id.lvtodaysmeetting);
                                todayMeet.setAdapter(adapt);
                                finish();





                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();



            }
        });




    }
}
