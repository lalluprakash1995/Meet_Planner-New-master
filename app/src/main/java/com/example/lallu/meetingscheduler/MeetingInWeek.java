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

public class MeetingInWeek extends AppCompatActivity {
    String str;
    FloatingActionButton deleteweelbtn;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_in_week);
        final DBHelper dbh=new DBHelper(this);
        deleteweelbtn=(FloatingActionButton)findViewById(R.id.fab_delete_meetingweek);
        final ListView list=(ListView)findViewById(R.id.meetinglist);
        ActionBar ab=getSupportActionBar();
        ab.setTitle("This Week's Meetings");
        ArrayAdapter adapt;
        ArrayList arrayList=dbh.getAllMeetings();
        if (arrayList.size()==0){
            final AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(MeetingInWeek.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(MeetingInWeek.this);
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


        list.setAdapter(adapt);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                str=(String)list.getItemAtPosition(arg2);
                //  Log.e("Selected Item",itemValue);
                // TODO Auto-generated method stub
                //  int id_To_Search = arg2 + 1;

                Bundle dataBundle = new Bundle();
                //   dataBundle.putInt("id", id_To_Search);
                dataBundle.putString("title",str);

                Intent intent = new Intent(getApplicationContext(),ViewMeetings.class);

                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
        deleteweelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                final AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(MeetingInWeek.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(MeetingInWeek.this);
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
                //tommoroow
                String dt = current_Date;
                int x = 1;
                Calendar cal = GregorianCalendar.getInstance();
                for (int i=1;i<8;i++){
                    cal.add( Calendar.DAY_OF_YEAR, x);
                    Date sevenDaysAfter = cal.getTime();
                    String nextday=df.format(sevenDaysAfter);
                    Log.e("next",nextday);
                    dbh.deleteTomorrow(nextday);
                  //  x++;

                }
                                ArrayAdapter adapt;
                                ArrayList arrayList=dbh.getAllMeetings();

                                adapt=new ArrayAdapter(MeetingInWeek.this,android.R.layout.simple_list_item_1,arrayList);


                                list.setAdapter(adapt);
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
