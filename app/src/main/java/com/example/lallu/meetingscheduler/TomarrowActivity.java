package com.example.lallu.meetingscheduler;

import android.annotation.TargetApi;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TomarrowActivity extends AppCompatActivity {
    ListView meetingListview;
    String itemValue;
    public final static String EXTRA_MESSAGE = "MESSAGE";
    DBHelper mydbhelper;
    ArrayAdapter adapt;
    FloatingActionButton deletetommorrwmeetings;

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomarrow);
        ActionBar ab=getSupportActionBar();
        ab.setTitle("Tomorrow's Meetings");

        deletetommorrwmeetings=(FloatingActionButton)findViewById(R.id.fab_delete_meetingtoday);
        mydbhelper=new DBHelper(this);
        ArrayList arrayList=mydbhelper.getTommorrowsMeeting();
        if (arrayList.size()==0){
            final AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(TomarrowActivity.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(TomarrowActivity.this);
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
        meetingListview=(ListView)findViewById(R.id.meetingtomorrow);
        meetingListview.setAdapter(adapt);
        meetingListview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                itemValue=(String)meetingListview.getItemAtPosition(arg2);
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
        deletetommorrwmeetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(TomarrowActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(TomarrowActivity.this);
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
                                cal.add( Calendar.DAY_OF_YEAR, x);
                                Date sevenDaysAfter = cal.getTime();
                                String nextday=df.format(sevenDaysAfter);
                                Log.e("next",nextday);

                                mydbhelper.deleteTomorrow(nextday);

                                ArrayList arrayList=mydbhelper.getTommorrowsMeeting();

                                adapt=new ArrayAdapter(TomarrowActivity.this,android.R.layout.simple_list_item_1,arrayList);
                                meetingListview=(ListView)findViewById(R.id.meetingtomorrow);
                                meetingListview.setAdapter(adapt);
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
