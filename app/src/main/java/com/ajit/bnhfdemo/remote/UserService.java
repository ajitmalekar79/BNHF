package com.ajit.bnhfdemo.remote;


import com.ajit.bnhfdemo.Doctor;
import com.ajit.bnhfdemo.Hospital;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    @GET("doctor/get/{id}")
    Call<Doctor> data(@Path("id") String id);

    @GET("doctor/log/{drId}/{password}")
    Call<Doctor> login(@Path("drId") String drId, @Path("password") String password);

    @PUT("doctor/update/dr")
    Call<Doctor> UpdateDoctor(@Body Doctor doctor);

    @POST("doctor/add")
    Call<Doctor> AddDoctor(@Body Doctor doctor);

    @POST("hosp/add/{drId}")
    Call<Hospital> AddHospital(@Body Hospital hospital,@Path("drId") String drId);

    @GET("hosp/show/hospital/{lat}/{lang}")
    Call<List<Hospital>> showHospital(@Path("lat") double lat, @Path("lang") double lang);

    @GET("hosp/get/hospital/{drId}")
    Call<List<Hospital>> getHospital(@Path("drId") String drId);

    @GET("hosp//doctor/{lat}/{lang}")
    Call<List<Doctor>> listDoctor(@Path("lat") double lat, @Path("lang") double lang);
}
