package com.ajit.bnhfdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ajit.bnhfdemo.Blog.DoctorBlog;
import com.ajit.bnhfdemo.remote.ApiUtils;
import com.ajit.bnhfdemo.remote.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorProfile extends AppCompatActivity {

    EditText firstName,lastName,emailId,qualification,speciality,visitingCharge,mobileNo,language;
    TextView txtUid;
    RadioButton male,female;
    Button updateButton;
    String id,drId,password;
    UserService userService;

     static Doctor drObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        final TextView schedule= (TextView)findViewById(R.id.btnSchedule);
        final TextView blog= (TextView)findViewById(R.id.btnBlog);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        emailId = findViewById(R.id.emailId);
        mobileNo = findViewById(R.id.mobileNo);
        language = findViewById(R.id.language);
        qualification = findViewById(R.id.qualification);
        speciality = findViewById(R.id.specialization);
        visitingCharge = findViewById(R.id.visitingCharge);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        txtUid = findViewById(R.id.txUid);

        updateButton = findViewById(R.id.updatebtn);

        id = getIntent().getExtras().getString("id");
        drId = getIntent().getExtras().getString("drId");
        password = getIntent().getExtras().getString("password");
        userService = ApiUtils.getUserService();

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorProfile.this,AllSchedule.class);
                intent.putExtra("drId",drId);
                startActivity(intent);
            }
        });

        blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fName=firstName.getText().toString();
                String lName=lastName.getText().toString();

                Intent intent = new Intent(DoctorProfile.this, DoctorBlog.class);
                intent.putExtra("drId",drId);
                intent.putExtra("name","Dr. "+fName+" "+lName);
                intent.putExtra("code","0");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        /*
        *
        * Code For setting Values
         */

        getDetails(id);


        /*
        ****
        * * COde For Update Data
        *
         */

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstname,lastname,emailid,qualifi,specialisation,visitingcharge,mobileno,languag;

                firstname = firstName.getText().toString();
                lastname = lastName.getText().toString();
                emailid = emailId.getText().toString();
                qualifi = qualification.getText().toString();
                specialisation = speciality.getText().toString();
                visitingcharge = visitingCharge.getText().toString();
                mobileno = mobileNo.getText().toString();
                languag = language.getText().toString();


                drObj.setFirstName(firstname);
                drObj.setLanguage(languag);
                drObj.setEmailId(emailid);
                drObj.setLastName(lastname);
                drObj.setMobile(mobileno);
                drObj.setQualification(qualifi);
                drObj.setSpecialization(specialisation);
                drObj.setVisitingCharge(visitingcharge);

                drObj.setDoctorId(drId);
                drObj.setId(id);
                drObj.setPassword(password);

                updateDetails(drObj);
            }
        });

    }

    private void getDetails(final String id) {


        Call<Doctor> call = userService.data(id);
        Log.d("Details", "UserService");

        call.enqueue(new Callback<Doctor>() {

            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {

                Log.d("Response:","In Response");
                if(response.isSuccessful()){
                    drObj = response.body();

                    Log.d("Response", "Success");
                    //toast("Data Retrieved Successfully");


                    txtUid.append(drObj.getDoctorId());
                    firstName.setText(drObj.getFirstName());
                    lastName.setText(drObj.getLastName());
                    emailId.setText(drObj.getEmailId());
                    mobileNo.setText(drObj.getMobile());
                    language.setText(drObj.getLanguage());
                    speciality.setText(drObj.getSpecialization());
                    visitingCharge.setText(drObj.getVisitingCharge());

                    qualification.setText(drObj.getQualification());
                    String gender = drObj.getGender();
                    if(gender.equals("MALE") || gender.equals("Male") || gender.equals("male")){
                        male.toggle();
                    }else if(gender.equals("FEMALE") || gender.equals("Female") || gender.equals("female")){
                        female.toggle();
                    }

                }else{
                    //toast("Invalid Id.");
                }
            }

            @Override
            public void onFailure(Call<Doctor> call, Throwable t) {

                Log.d("Response", "Fail");
                toast("Data Not Retrieved..");

            }
        });
    }

    private void updateDetails(Doctor doctor) {


        Call<Doctor> call = userService.UpdateDoctor(doctor);
        Log.d("Details", "UserService");

        call.enqueue(new Callback<Doctor>() {

            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {

                Log.d("Response:","In Response");
                //if(response.isSuccessful()){
                    //drObj = response.body();

                    Log.d("Response", "Success");
                    toast("Data Updated Successfully");



                /*}else{
                    toast("Invalid Request..");
                }*/
            }

            @Override
            public void onFailure(Call<Doctor> call, Throwable t) {

                Log.d("Response", "Fail");
                toast("Data Not Updated..");

            }
        });
    }

    private void toast(String success) {
        Toast.makeText(this,success,Toast.LENGTH_SHORT).show();
    }
}
