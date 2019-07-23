package com.ajit.bnhfdemo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ajit.bnhfdemo.remote.ApiUtils;
import com.ajit.bnhfdemo.remote.UserService;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.IOException;
import java.sql.Time;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorSchedule extends AppCompatActivity implements View.OnClickListener{

    Button btnDatePicker, btnTimePicker,btnTimePic,btnDatepic,btnUpdate, btnPlace, btnChView;
    EditText txtDate, txtTime,txtTm,txtDat,hospName;
    private int mYear, mMonth, mDay, mHour, mMinute;
    //TextView hospName;
    //String[] hospital = {"Samarth Hospital", "Ashwini Hospital Dr Kshirsagar", "Shubham Clinic Dr Raut", "Goverment Hospital", "ZP GOVERNMENT HOSPITAL BHOR", "Gurukrupa Hospital"};


    /*
    **
    * Declarations For Hospital Info
     */

    //RelativeLayout addHospView,timeView,hospList;

    ListView allHospList;
    static ArrayList<String> hList;

    TextView hospCity;
    UserService userService;
    Hospital hospObj;
    static String hName,hCity;
    static double hLat,hLang;
    Util ut;
    static String drId;

    int PLACE_PICKER_REQUEST= 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_schedule);

        btnDatePicker=(Button)findViewById(R.id.startdatebtn);
        txtDate=(EditText)findViewById(R.id.start_date);
        btnDatepic=(Button)findViewById(R.id.enddatebtn);
        txtDat=(EditText)findViewById(R.id.end_date);
        btnTimePicker=(Button)findViewById(R.id.intimebtn);
        txtTime=(EditText)findViewById(R.id.in_time);
        btnTimePic=(Button)findViewById(R.id.outtimebtn);
        txtTm=(EditText)findViewById(R.id.out_time);
        btnUpdate=(Button)findViewById(R.id.update);
        //btnPlace = findViewById(R.id.btnPlace);
        //hospName = findViewById(R.id.hospName);
        //hosp=(EditText)findViewById(R.id.autoCompleteTextView);

        /*btnChView = findViewById(R.id.chView);
        addHospView = findViewById(R.id.addHosp);
        timeView = findViewById(R.id.viewTime);
        hospList = findViewById(R.id.hospList);

        addHospView.setVisibility(View.GONE);
        timeView.setVisibility(View.GONE);*/

        hospCity = findViewById(R.id.txCity);
        hospName = findViewById(R.id.txName);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        btnTimePic.setOnClickListener(this);
        btnDatepic.setOnClickListener(this);

        userService = ApiUtils.getUserService();
        drId = getIntent().getExtras().getString("drId");

        hList =new ArrayList<>();

        //GetHospital(drId);

        /*ListAdapter adapter = new ArrayAdapter<>(DoctorSchedule.this,android.R.layout.simple_list_item_1,hList);
        allHospList.setAdapter(adapter);*/



        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(hospName.getText().toString().isEmpty() ||
                        hospCity.getText().toString().isEmpty()){
                    Toast.makeText(DoctorSchedule.this,"Something missing",Toast.LENGTH_SHORT).show();
                }else {


                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat tdf = new SimpleDateFormat("hh:mm:ss");

                    String strCurrent =txtDate.getText().toString();
                    //String strTime = tdf.format(current);

                    System.out.println(strCurrent);
                    //System.out.println(strTime);
                    toast(strCurrent);



                    try {
                        Date startDateD = sdf.parse(strCurrent);

                        Date endDateD = sdf.parse(txtDat.getText().toString());

                        toast(""+startDateD);
                        toast(""+endDateD);

                        Date inTimeD = tdf.parse(txtTime.getText().toString());
                        Date outTimeD = tdf.parse(txtTm.getText().toString());

                        Log.d("StartDate", ""+startDateD);
                        Log.d("EndDate", ""+endDateD);
                        Log.d("In Time", ""+inTimeD);
                        Log.d("OutTime", ""+outTimeD);

                        String inTime = txtTime.getText().toString();
                        String outTime = txtTm.getText().toString();
                        String startDate = sdf.format(endDateD);
                        String endDate = sdf.format(startDateD);

                        Log.d("StartDate", ""+startDate);
                        Log.d("EndDate", ""+endDate);
                        Log.d("In Time", ""+inTime);
                        Log.d("OutTime", ""+outTime);


                        hospObj = new Hospital();
                        String hospNm = hospName.getText().toString();

                        toast(hLat+" ,"+hLang);

                        hospObj= new Hospital(hospObj.getHospId(),hospNm,hCity,hLat,hLang,inTime,outTime,startDate,endDate);
                        AddHospital(hospObj,drId);

                    } catch (ParseException e) {
                        e.printStackTrace();
                        toast(""+e);
                    }




                    }
            }
        });

        /*btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("In OnClickListner");
                goPlacePicker();
            }
        });*/


        /*btnChView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hospList.setVisibility(View.GONE);
                addHospView.setVisibility(View.VISIBLE);
                timeView.setVisibility(View.VISIBLE);
            }
        });*/


    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDat.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnDatepic) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute+ ":00");
                            //txtTm.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if (v == btnTimePic) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTm.setText(hourOfDay + ":" + minute+ ":"+"00");
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        /*if(v == btnPlace){
            goPlacePicker();
        }*/
    }

    private void AddHospital(final Hospital hospital,String drId) {



        Call<Hospital> call = userService.AddHospital(hospital,drId);
        Log.d("Details", "UserService");



        call.enqueue(new Callback<Hospital>() {

            @Override
            public void onResponse(Call<Hospital> call, Response<Hospital> response) {

                Log.d("Response:","In Response");
                if(response.isSuccessful()){
                    hospObj = new Hospital();
                    hospObj = response.body();

                    Log.d("Response", "Success");
                    toast("Hospital "+hospObj.getHospName()+" Registered Successfully");



                }else{
                    toast("Invalid Request..");
                }
            }

            @Override
            public void onFailure(Call<Hospital> call, Throwable t) {

                Log.d("Response", "Fail");
                toast("Data Not Updated..");

            }
        });
    }



    public void goPlacePicker(View view){
        //HIS IS THE PLACE TO CALL PLACE PICKER

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        Intent intent;
        try {
            intent = builder.build(DoctorSchedule.this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);


        } catch (GooglePlayServicesRepairableException e) {
            Log.e("In PlacePicker", "GooglePlayServicesRepairableException "+e.getMessage());
            toast(e.getMessage());
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e("In PlacePicker", "GooglePlayServicesRepairableException "+e.getMessage());
            toast(e.getMessage());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode==PLACE_PICKER_REQUEST){
            if(resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(data, DoctorSchedule.this);

                hLat = place.getLatLng().latitude;
                hLang = place.getLatLng().longitude;

                Geocoder geocoder = new Geocoder(this);
                List<Address> list = null;
                try {
                    list = geocoder.getFromLocation(hLat,hLang,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address address = list.get(0);

                hName = (String) place.getName();
                hCity = address.getLocality();



                hospName.setText(hName);
                hospCity.setText(hCity);

                /**/


                //Toast.makeText(DoctorSchedule.this,"Schedule Update Successesfully",Toast.LENGTH_SHORT).show();



            }
        }
    }

    void toast (String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}
