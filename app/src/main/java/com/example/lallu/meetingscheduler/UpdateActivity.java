package com.example.lallu.meetingscheduler;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class UpdateActivity extends AppCompatActivity {
    Calendar myCalendar=Calendar.getInstance();
    private Uri uriContact;
    private String contactID,Contactnum,contactName1;
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    EditText update_title,update_agenda,update_Schedate,update_start,update_end,update_location,update_phn;
    String bundletitle,bundleagenda,bundledate,bundleschedate,bundlestarttime,bundleendtime,bundlelocation,bundlephn;
    String amPm;
    DBHelper dbupdate;
    Button time_startbt,time_endbt;
    private Calendar mcalendar;

    private int day, month, year,mHour,mMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        update_title=(EditText)findViewById(R.id.title_edt);
        update_agenda=(EditText)findViewById(R.id.agenda_edt);
        update_Schedate=(EditText)findViewById(R.id.meeting_date_edittext);
        update_start=(EditText)findViewById(R.id.start_time_edt1);
        update_end=(EditText)findViewById(R.id.end_time_edt1);
        update_phn=(EditText)findViewById(R.id.phone_number_edt);
        update_location=(EditText)findViewById(R.id.location_edt);
        time_startbt=(Button)findViewById(R.id.time_start);
        time_endbt=(Button)findViewById(R.id.time_end);
        ActionBar ab=getSupportActionBar();
        ab.setTitle("Edit Details");

        dbupdate=new DBHelper(this);

        update_Schedate.setOnClickListener(mClickListener);
        mcalendar=Calendar.getInstance();
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);

        Bundle obj=getIntent().getExtras();
        bundletitle=obj.getString("key_title");
        bundleagenda=obj.getString("key_agenda");
        bundledate=obj.getString("key_date");
        bundleschedate=obj.getString("key_scheduled");
        bundlestarttime=obj.getString("key_start");
        bundleendtime=obj.getString("Key_end");
        bundlephn=obj.getString("key_phn");
        bundlelocation=obj.getString("key_loc");

        update_title.setText(bundletitle);
        update_title.setEnabled(false);
        update_agenda.setText(bundleagenda);
//        update_agenda.setEnabled(false);
        update_Schedate.setText(bundleschedate);
//        update_Schedate.setEnabled(false);
        update_start.setText(bundlestarttime);
//        update_start.setEnabled(false);
        update_end.setText(bundleendtime);
//        update_end.setEnabled(false);
        update_location.setText(bundlelocation);
//        update_location.setEnabled(false);
        update_phn.setText(bundlephn);
//        update_phn.setEnabled(false);

        // To choose start time
        time_startbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
//set time to edit text
                                update_start.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        time_endbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
//set time to edit text
                                update_end.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
    }


    public void UpdateData(View view) {
        bundletitle=update_title.getText().toString();
        bundleagenda=update_agenda.getText().toString();
        bundleschedate=update_Schedate.getText().toString();
        bundlestarttime=update_start.getText().toString();
        bundleendtime=update_end.getText().toString();
        bundlephn=update_phn.getText().toString();
        bundlelocation=update_location.getText().toString();
        Toast.makeText(getApplicationContext(),bundletitle,Toast.LENGTH_SHORT).show();
        if (dbupdate.updatemeeting(bundledate, bundletitle,bundleagenda,bundleschedate, bundlestarttime, bundleendtime,bundlephn,bundlelocation)
                ==true){
            Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    public void SelectFromContacts(View view) {
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);

    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
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

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
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
                update_phn.setText(Contactnum);}
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


            contactName1 = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))+":";

        }

        cursor.close();

        Log.d(TAG, "Contact Name: " + contactName1);
        Log.e("Contactname:",contactName1);

    }


    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DateDialog();
        }
    };

    public void DateDialog() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                update_Schedate.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(UpdateActivity.this, listener, year, month, day);
        dpDialog.show();
    }

}
