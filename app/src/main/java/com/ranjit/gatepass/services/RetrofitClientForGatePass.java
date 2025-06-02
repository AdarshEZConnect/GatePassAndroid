package com.ranjit.gatepass.services;

import com.ranjit.gatepass.interfaces.GatePassApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientForGatePass {
    private static final String BASE_URL = "https://agrigenius-v1.onrender.com/";
    private static Retrofit retrofit;

    public static GatePassApiService getGroceryApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(GatePassApiService.class);
    }
}
