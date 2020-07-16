package com.pjs.kirimgps;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import github.nisrulz.easydeviceinfo.base.EasyLocationMod;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyApp {

    EasyLocationMod easyLocationMod;
    Context mContext;
    String latitude , longitude;
    String currentDate;
    String currentTime;
    APIInterface apiInterface;

    public MyApp(Context mContext)
    {
        this.mContext = mContext;
    }
    public void cobaGPS()
    {
        easyLocationMod = new EasyLocationMod(mContext);
        Log.d("APPLOG","COBAGPS");
        double[] l = easyLocationMod.getLatLong();
        latitude = String.valueOf(l[0]);
        longitude = String.valueOf(l[0]);

        currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        Log.d("GPS", latitude+"|"+longitude);
        Log.d("Time", currentDate+"|"+currentTime);
//        connectServer();
    }

    private void connectServer()
    {
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<ResponseBody> call = apiInterface.KirimLokasi(latitude,longitude,currentDate,currentTime);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("Call request", call.request().toString());
                Log.d("Call request header", call.request().headers().toString());
                Log.d("Response raw header", response.headers().toString());
                Log.d("Response raw", String.valueOf(response.raw().body()));
                Log.d("Response code", String.valueOf(response.code()));

                if (response.isSuccessful()) {
                    ResponseBody responseBody = (ResponseBody) response.body();
                    String responseBodyString= null;
                    try {
                        responseBodyString = responseBody.string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d("Response body", responseBodyString);
                }
                else {
                Log.d("Response errorBody", String.valueOf(response.errorBody()));
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ERROR",t.getMessage());
            }
        });
    }
}
