package com.example.lallu.meetingscheduler;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class AddMeetingActivity extends AppCompatActivity {
    int flag=0,flag1=0;
    int shour,smins,ehour,emins;
    Date d1,d3;

    EditText pickdate,starttime,endtime,phonenum,agenda,meetingtitle,locton;
    String amPm,strlocation="aaaaaaaa";
    Calendar calender=Calendar.getInstance();
    private static final int REQUEST_CODE_PICK_CONTACTS=1;
    private Uri uriContact;
    private String contactID,Contactnum,contactName1;
    String stragenda,strdate,strstarttime,strendtime,strtitle,strphn,strloc;
    ArrayList<String> checktitle;

    //DataBaseMeeting dbconnect;
    DBHelper dbconnect;
    Button bt;
    private boolean validate(EditText[] fields){
        for(int i = 0; i < fields.length; i++){
            EditText currentField = fields[i];
            if(currentField.getText().toString().length() <= 0){
                //
                Toast.makeText(AddMeetingActivity.this, "All fields must be fill", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);
//        appBarLayout=(AppBarLayout)findViewById(R.id.appbar);
//        toolbar=(Toolbar)findViewById(R.id.tollbar);
        agenda=(EditText)findViewById(R.id.agenda_edt);
        pickdate=(EditText)findViewById(R.id.meeting_date_edittext);
        starttime=(EditText)findViewById(R.id.start_time_edt);
        endtime=(EditText)findViewById(R.id.End_time_edt);
        phonenum=(EditText)findViewById(R.id.phone_number_edt);
        meetingtitle=(EditText)findViewById(R.id.title_edt);
        locton=(EditText)findViewById(R.id.location_edt);
        ActionBar ab=getSupportActionBar();
        ab.setTitle("Add New Meeting");

dbconnect=new DBHelper(this);

        final int[] d1 = new int[1];
        final int[] d3 = new int[1];
        // To choose the meeting Date

        final DatePickerDialog.OnDateSetListener date=new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calender.set(Calendar.YEAR,year);
                calender.set(Calendar.MONTH,month);
                calender.set(Calendar.DAY_OF_MONTH,dayOfMonth);
              //   d1[0] =dayOfMonth;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    upDateLabel();
                }
            }
        };
        pickdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddMeetingActivity.this, date, calender
                        .get(Calendar.YEAR), calender.get(Calendar.MONTH),
                        calender.get(Calendar.DAY_OF_MONTH)).show();
              //  d3[0] =Calendar.DAY_OF_MONTH;

            }
        });



    // To choose start time from Time Picker

        starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar strttime=Calendar.getInstance();
                final int hour=strttime.get(Calendar.HOUR_OF_DAY);
                int mins=strttime.get(Calendar.MINUTE);

                TimePickerDialog mtimePickerDialog=new TimePickerDialog(AddMeetingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                          /*  if (hourOfDay==12){
                                hourOfDay=12;
                            }
                            else
                                hourOfDay=hourOfDay%12;*/
                        } else {
                            amPm = "AM";

                        }
                        shour=hourOfDay;smins=minute;
                        String abc=String.format("%02d:%02d", hourOfDay, minute) + amPm;
                        String newString = abc.replace("00", "12");

                        starttime.setText(newString);
                       // starttime.setText(""+hourOfDay+":"+minute+" "+amPm);
                    }
                },hour,mins,false);
                mtimePickerDialog.setTitle("Meeting Starts At");
                mtimePickerDialog.show();
            }
        });

        // To Choose End Time

        endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar strttime=Calendar.getInstance();
                final int hour=strttime.get(Calendar.HOUR_OF_DAY);
                final int mins=strttime.get(Calendar.MINUTE);

                TimePickerDialog mtimePickerDialog=new TimePickerDialog(AddMeetingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";

                         /*  // hourOfDay=hourOfDay%12;
                            if (hourOfDay==12){
                                hourOfDay=12;
                            }
                            else
                                hourOfDay=hourOfDay%12;*/

                        }
                       // else
                        else {
                            amPm = "AM";
                        }
                        ehour=hourOfDay;emins=minute;
                        String abc=String.format("%02d:%02d", hourOfDay, minute) + amPm;
                        String newString = abc.replace("00", "12");



                      //  endtime.setText(String.format("%02d:%02d", hourOfDay, minute) + amPm);
                        endtime.setText(newString);
                    }
                },hour,mins,false);
                mtimePickerDialog.setTitle("Meeting Starts At");
                mtimePickerDialog.show();
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void upDateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sdf = new SimpleDateFormat(myFormat,Locale.US);
        }

         d1=calender.getTime();
        d3 = Calendar.getInstance().getTime(); //System.out.println("Current time => " + c);
      if (d1.compareTo(d3)<0){
          pickdate.setError("Invalid Date");
      }
      else{
          pickdate.setError(null);
          pickdate.setText(sdf.format(calender.getTime()));

      }


    }

    public void SelectFromContacts(View view) {
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();

            retrieveContactName();
            retrieveContactNumber();
            //   blockListView.setAdapter(dap);
        }
    }

    private void retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Contactnum=contactNumber.replaceAll("\\s+","");
            if(Contactnum.length()==10){
//                countryedt.setText("91");
//                phnedt.setText(numBlock);
//
}
            else if(Contactnum.length()>10){
               // countryedt.setText("");
//                phonenum.setText(Contactnum);
            }
            else{
                Toast.makeText(getApplicationContext(),"No Number Found",Toast.LENGTH_LONG).show();
            }
        }

        cursorPhone.close();

        Log.d(TAG, "Contact Phone Number: " + contactNumber);
    }

    private void retrieveContactName() {



        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName1 = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))+":";
//            if(enterNum.getText().length()>2){
//                blockList_contactName.add(contactName);}
//            else{
//                Toast.makeText(getApplicationContext(),"No Number Found",Toast.LENGTH_LONG).show();
//            }
        }

        cursor.close();

        Log.d(TAG, "Contact Name: " + contactName1);
        Log.e("Contactname:",contactName1);
        phonenum.setText(contactName1+"\n"+Contactnum);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void SaveData(View view) throws ParseException {
        stragenda=agenda.getText().toString();
        strdate=pickdate.getText().toString();
        strstarttime=starttime.getText().toString();

        strendtime=endtime.getText().toString();



        strphn=phonenum.getText().toString();
        strtitle=meetingtitle.getText().toString();
        strloc=locton.getText().toString();

        // To check the title already exist or not
        checktitle=dbconnect.getAllCotacts();
        for (int i=0;i<checktitle.size();i++){
            if (strtitle.equalsIgnoreCase(checktitle.get(i))){
                flag++;
                meetingtitle.setError("Title already exist");
            }
            else
                flag=0;
        }

        // To check start and end time
        flag1=0;
        if (shour==ehour){
            if (smins<emins){
                flag1=0;
            }
            else {
                endtime.setError("Enter valid time");
                flag1++;

            }

        }
        else if (shour>ehour){
            endtime.setError("Enter valid time");
            flag1++;

        }
        else flag1=0;



      //  Current date
//
        Date c = Calendar.getInstance().getTime(); System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String current_Date = df.format(c);
      //  Toast.makeText(getApplicationContext(),current_Date,Toast.LENGTH_SHORT).show();
        Boolean check=validate(new EditText[]{meetingtitle,agenda,pickdate,starttime,endtime,phonenum,locton});

        // for check date
//        boolean valid=checkDate(current_Date,strdate);



if (check==true && flag==0 && flag1==0 ) {


    if (dbconnect.insertMeetings(current_Date, strtitle, stragenda, strdate, strstarttime, strendtime, strphn, strloc)) {

        Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
        finish();
    } else {
        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
    }
}

    }


}
