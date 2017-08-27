package com.dharanaditya.bcards.api;

import android.util.Log;

import com.dharanaditya.bcards.model.AccessCredentials;
import com.dharanaditya.bcards.model.BCard;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dharan1011 on 27/8/17.
 */

public final class NetworkUtils {
    public static final String TAG = NetworkUtils.class.getSimpleName();

    public static String getAccessToken(String code) {
        final String[] temp = {""};
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LinkedInService.REQUEST_ACCESS_TOKEN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LinkedInService linkedInService = retrofit.create(LinkedInService.class);
        linkedInService.getAccessToken("authorization_code",
                code,
                "http://com.dharanaditya.bcards/callback",
                "816dfsm481zdzf",
                "RJKB2yNH3dIsbQXM")
                .enqueue(new Callback<AccessCredentials>() {
                    @Override
                    public void onResponse(Call<AccessCredentials> call, Response<AccessCredentials> response) {
                        Log.d(TAG, "onResponse: " + response.code());
                        if (response.isSuccessful()) {
                            String token = response.body().getAccessToken();
                            Log.d(TAG, "onResponse: Token -> " + token);
                            getBcardData(token);
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessCredentials> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + call.toString());
                    }
                });
        return temp[0];
    }

    public static String getBearerToken(String token) {
        return "Bearer " + token;
    }

    public static BCard getBcardData(String token) {
        final BCard[] bCard = {null};
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LinkedInService.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LinkedInService linkedInService = retrofit.create(LinkedInService.class);
        linkedInService.getBCardData(getBearerToken(token), LinkedInService.PARAM_FORMAT).enqueue(new Callback<BCard>() {

            @Override
            public void onResponse(Call<BCard> call, Response<BCard> response) {
                Log.d(TAG, "onResponse: " + call.request().headers().toString());
                Log.d(TAG, "onResponse: " + response.code());
                if (response.isSuccessful()) {
                    bCard[0] = response.body();
                    Log.d(TAG, "onResponse: BCard -> " + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<BCard> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call.toString());
            }
        });
        return bCard[0];
    }
}
