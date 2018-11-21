package com.example.lallu.meetingscheduler;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
FloatingActionButton fa;
ListView meetingListview;
String itemValue;
    public final static String EXTRA_MESSAGE = "MESSAGE";
    DBHelper mydbhelper;
    ArrayAdapter adapt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mydbhelper=new DBHelper(this);
       ArrayList arrayList=mydbhelper.getAllCotacts();

         adapt=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);







        meetingListview=(ListView)findViewById(R.id.mylist_meetings);
        meetingListview.setAdapter(adapt);

      // Action of Listview
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



        fa=(FloatingActionButton)findViewById(R.id.floatingActionButton3);
        fa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Your Text",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),AddMeetingActivity.class));
            }
        });





    }

    @Override
    protected void onResume() {

        ArrayList arrayList=mydbhelper.getAllCotacts();

        adapt=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        meetingListview=(ListView)findViewById(R.id.mylist_meetings);
        meetingListview.setAdapter(adapt);
        super.onResume();

    }

    public void asd(View view) {
        startActivity(new Intent(getApplicationContext(),MeetingInWeek.class));
    }
}
