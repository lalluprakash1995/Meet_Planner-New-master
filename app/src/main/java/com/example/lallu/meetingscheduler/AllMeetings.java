package com.example.lallu.meetingscheduler;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AllMeetings extends AppCompatActivity {
    ArrayList<String> meetinghead;
    ArrayList<String> titlecheck;
    private int storagepermissioncode=1;
    adapter ad;

    ArrayList<String> meetingdate;
    ListView list;
    DBHelper mydbhelper;
    FloatingActionButton addmeetingfab,deleteallmeetings;
    com.getbase.floatingactionbutton.FloatingActionButton weekfab,tomorrowfab,todayfab;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_meetings);
        ActionBar ab=getSupportActionBar();
        ab.setTitle("All Meetings Scheduled");

        mydbhelper=new DBHelper(this);
        titlecheck=new ArrayList<>();

        addmeetingfab=findViewById(R.id.fab_add_meeting);
        addmeetingfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //.......
                if(ContextCompat.checkSelfPermission(AllMeetings.this,
                        Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(AllMeetings.this, "ALREADY GRANTED PERMISSION", Toast.LENGTH_SHORT).show();
                }
                else{
                    requeststroagepermission();
                   // startActivity(new Intent(AllMeetings.this,AddMeetingActivity.class));

                }//.......
                startActivity(new Intent(AllMeetings.this,AddMeetingActivity.class));
            }
        });
        todayfab=findViewById(R.id.fab_today);
        todayfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllMeetings.this,TodaysMeeting.class));

            }
        });

        weekfab=findViewById(R.id.fab_week);
        weekfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllMeetings.this,MeetingInWeek.class));
            }
        });
        tomorrowfab=findViewById(R.id.fab_tomorrow);
        tomorrowfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllMeetings.this,TomarrowActivity.class));
            }
        });

        // delete expired meetings
        Date c = Calendar.getInstance().getTime(); System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String current_Date = df.format(c);
        Log.e("Current_Date=",current_Date);
        //tommoroow
        String dt = current_Date;
        int x = -1;
        Calendar cal = GregorianCalendar.getInstance();
        cal.add( Calendar.DAY_OF_YEAR, x);
        Date sevenDaysAfter = cal.getTime();
        String previousday=df.format(sevenDaysAfter);
        Log.e("Prevoius",previousday);

        mydbhelper.deleteTomorrow(previousday);












        meetinghead=mydbhelper.getAllCotacts();
        meetingdate=mydbhelper.getAllDates();

        list=(ListView)findViewById(R.id.hosplist);
         ad=new adapter();
        list.setAdapter(ad);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String itm=((TextView)view.findViewById(R.id.txtview1)).getText().toString();

                Toast.makeText(getApplicationContext(), itm,Toast.LENGTH_SHORT).show();
                Bundle dataBundle = new Bundle();
                //   dataBundle.putInt("id", id_To_Search);
                dataBundle.putString("title",itm);

                Intent intent = new Intent(getApplicationContext(),ViewMeetings.class);

                intent.putExtras(dataBundle);
                startActivity(intent);
                // View v1 = adapterView.getChildAt(position);

            }
        });
        deleteallmeetings=findViewById(R.id.fab_delete_meeting);
        deleteallmeetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(AllMeetings.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(AllMeetings.this);
                }
                builder.setTitle("Delete Meetings ?")
                        .setMessage("Are you sure you want to delete all scheduled meetings?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                mydbhelper.deleteAll();

                                meetinghead=mydbhelper.getAllCotacts();
                                meetingdate=mydbhelper.getAllDates();
                                list=(ListView)findViewById(R.id.hosplist);
                                ad=new adapter();
                                list.setAdapter(ad);
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
//                mydbhelper.deleteAll();
//
//                meetinghead=mydbhelper.getAllCotacts();
//                meetingdate=mydbhelper.getAllDates();
//                list=(ListView)findViewById(R.id.hosplist);
//                ad=new adapter();
//                list.setAdapter(ad);
            }
        });






    }
    protected void onResume() {
        meetinghead=mydbhelper.getAllCotacts();
        meetingdate=mydbhelper.getAllDates();




        //alert start
        if (meetinghead.size()==0) {

            final AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(AllMeetings.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(AllMeetings.this);
            }
            builder.setTitle("Alert...!! ?")
                    .setMessage("No Scheduled Meetings")
                    .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(AllMeetings.this, AddMeetingActivity.class));
                            dialog.dismiss();


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

            list=(ListView)findViewById(R.id.hosplist);
            ad=new adapter();
            list.setAdapter(ad);







        //alert ends






        super.onResume();
    }
    private void requeststroagepermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)){
            new AlertDialog.Builder(this)
                    .setTitle("PERMISSION NEEDED")
                    .setMessage("This is needed for Recording voice")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AllMeetings.this, new String[] {Manifest.permission.RECORD_AUDIO},storagepermissioncode);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }
        else{
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_CONTACTS},storagepermissioncode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==storagepermissioncode){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "DENIED PERMISSION", Toast.LENGTH_SHORT).show();
        }
    }
    class adapter extends BaseAdapter{
        LayoutInflater inflater;

        @Override
        public int getCount() {
            return meetingdate.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.viewmeetcustomlistviewdesign,null);
            ViewHolder holder=new ViewHolder();
            holder.Hna=(TextView)view.findViewById(R.id.txtview1);
            holder.Hna.setText(meetinghead.get(i));
            holder.Pla=(TextView)view.findViewById(R.id.txtview2);
            holder.Pla.setText(meetingdate.get(i));
            return view;
        }
    }
    class ViewHolder{
        TextView Hna;
        TextView Pla;



    }
}
