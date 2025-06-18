package com.ranjit.gatepass.services;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ImageKitService {
    @Multipart
    @POST("https://upload.imagekit.io/api/v1/files/upload")
    Call<ResponseBody> uploadImage(
        @Part MultipartBody.Part file,
        @Part("fileName") RequestBody fileName,
        @Part("publicKey") RequestBody publicKey
    );
}
