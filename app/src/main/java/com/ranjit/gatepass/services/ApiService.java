package com.ranjit.gatepass.services;

import com.ranjit.gatepass.models.Department;
import com.ranjit.gatepass.models.GatePassRequest;
import com.ranjit.gatepass.models.GatePassResponse;
import com.ranjit.gatepass.models.RegisterRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("api/gatepasses/android-user/{id}")
    Call<List<GatePassResponse>> getGatePasses(@Path("id") int id, @Header("Authorization") String token);
    @POST("api/gatepasses/apply")
    Call<Void> applyGatePass(
            @Body GatePassRequest request,
            @Header("Authorization") String token
    );
    @POST("api/public/register")
    Call<Void> registerUser(@Body RegisterRequest request);
    @GET("api/public/departments")
    Call<List<Department>> getDepartments();


}
