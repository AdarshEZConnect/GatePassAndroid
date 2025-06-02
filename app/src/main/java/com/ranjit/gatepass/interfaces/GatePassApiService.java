package com.ranjit.gatepass.interfaces;

import com.ranjit.gatepass.models.GatePassModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GatePassApiService {

    @GET("api/gatePass/all")
    Call<List<GatePassModel>> getAllItems();

    @GET("api/grocery/products/{productName}")
    Call<List<GatePassModel>> getItemsByProduct(@Path("productName") String productName);

    @GET("api/grocery/{id}")
    Call<GatePassModel> getItemById(@Path("id") Long id);

    @POST("api/grocery/add")
    Call<GatePassModel> addItem(@Body GatePassModel item);
}
