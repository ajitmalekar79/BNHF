package com.ajit.bnhfdemo;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ajit.bnhfdemo.remote.ApiUtils;
import com.ajit.bnhfdemo.remote.UserService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllSchedule extends AppCompatActivity {

    ListView allHospList;
    static ArrayList<String> hList;
    Button loadHosp;

    static String drId;

    UserService userService;

    FloatingActionButton addHospital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_schedule);

        addHospital = findViewById(R.id.addHospital);
        final ListView listView = findViewById(R.id.hospList);
        loadHosp = findViewById(R.id.loadHosp);

        userService = ApiUtils.getUserService();

        drId = getIntent().getExtras().getString("drId");

        hList = new ArrayList<>();


        //toast("List Done..");

        loadHosp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetHospital(drId);

                ArrayAdapter adapter = new ArrayAdapter<>(AllSchedule.this,android.R.layout.simple_list_item_1,hList);
                listView.setAdapter(adapter);

                ((ArrayAdapter) adapter).notifyDataSetChanged();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        toast("Position:"+position+" Id:"+id);

                    }
                });
           }
        });

        addHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllSchedule.this,DoctorSchedule.class);
                intent.putExtra("drId",drId);
                startActivity(intent);
            }
        });
    }



    private void GetHospital(String doctorId) {



        Call<List<Hospital>> call = userService.getHospital(doctorId);
        Log.d("Details", "UserService");



        call.enqueue(new Callback<List<Hospital>>() {

            @Override
            public void onResponse(Call<List<Hospital>> call, Response<List<Hospital>> response) {


                if(response.isSuccessful()){
                    Log.d("Response:","In Response");

                    List<Hospital> listhospObj = response.body();

                    for (Hospital hospital:listhospObj){

                        hList.add(hospital.getHospName());
                    }


                    /*hospName.setText(hospObj.getHospName());
                    hospCity.setText(hospObj.getHospCity());
                    txtDate.setText(hospObj.getToDate());
                    txtDat.setText(hospObj.getFromDate());
                    txtTime.setText(hospObj.getInTime());
                    txtTm.setText(hospObj.getOutTime());


                    Log.d("Response", "Success");
                    toast("Hospital "+hospObj.getHospName()+" Registered Successfully");*/

                }else{
                    toast("Invalid Request..");
                }
            }

            @Override
            public void onFailure(Call<List<Hospital>> call, Throwable t) {

                Log.d("Response", "Fail");
                toast("Data Not Updated..");

            }
        });
    }

    void toast (String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
