package com.ranjit.gatepass.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageKitClient {
    public static Retrofit getClient() {
        return new Retrofit.Builder()
                .baseUrl("https://upload.imagekit.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
