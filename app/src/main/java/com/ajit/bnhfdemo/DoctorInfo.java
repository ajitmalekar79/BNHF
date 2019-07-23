package com.ajit.bnhfdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ajit.bnhfdemo.remote.ApiUtils;
import com.ajit.bnhfdemo.remote.UserService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorInfo extends AppCompatActivity {

    TextView txName,txGender,txEmail,txPhone,txQualify,txSpecial,txCharge,txLanguage;
    UserService userService;

    String id,drId,password;

    static Doctor drObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_info);

        txCharge = findViewById(R.id.txtCharges);
        txEmail = findViewById(R.id.txtEmail);
        txGender = findViewById(R.id.txtGender);
        txLanguage =  findViewById(R.id.txtLanguage);
        txName = findViewById(R.id.txtName);
        txPhone = findViewById(R.id.txtMobile);
        txQualify = findViewById(R.id.txtQualification);
        txSpecial = findViewById(R.id.txtSpeciality);

        userService = ApiUtils.getUserService();

        id = getIntent().getExtras().getString("id");
        drId = getIntent().getExtras().getString("drId");
        password = getIntent().getExtras().getString("password");

        getDetails(id);
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


                    txName.append(drObj.getFirstName()+" "+drObj.getLastName());
                    txEmail.append(drObj.getEmailId());
                    txPhone.append(drObj.getMobile());
                    txLanguage.append(drObj.getLanguage());
                    txSpecial.append(drObj.getSpecialization());
                    txCharge.append(drObj.getVisitingCharge());

                    txQualify.append(drObj.getQualification());
                    txGender.append(drObj.getGender());

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

    void toast(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }


}
