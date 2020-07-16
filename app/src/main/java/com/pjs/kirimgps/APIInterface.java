package com.pjs.kirimgps;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface APIInterface {

    @FormUrlEncoded
    @POST("GPS")
    Call<ResponseBody> KirimLokasi(
        @Field("latitude") String latitude,
        @Field("longitude") String longitude,
        @Field("currentDate") String currentDate,
        @Field("currentTime") String currentTime
    );
}