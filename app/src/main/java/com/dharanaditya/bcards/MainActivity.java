package com.dharanaditya.bcards;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.dharanaditya.bcards.api.LinkedInService;
import com.dharanaditya.bcards.model.AccessCredentials;
import com.dharanaditya.bcards.model.BCard;
import com.dharanaditya.bcards.ui.TestAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Retrofit retrofit;
    private RecyclerView bcardsList;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bcardsList = (RecyclerView) findViewById(R.id.rv_bcards_list);
        bcardsList.setLayoutManager(new LinearLayoutManager(this));
        progressBar = (ProgressBar) findViewById(R.id.pb_loding);
//        TODO Authenticate User
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent codeIntent = getIntent();
        Log.d(TAG, "onResume: " + codeIntent.toString());
        if (codeIntent.getData() != null && !codeIntent.getData().getQueryParameter("code").isEmpty()) {
            String code = codeIntent.getData().getQueryParameter("code");
        }
    }

    private void invalidateIntent() {
        getIntent().setData(null);
        getIntent().setAction("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_signout:
//                TODO Handle Firebase Sign Out
        }
        return super.onOptionsItemSelected(item);
    }

    void testRecyclerView() {
        List<BCard> bCards = new ArrayList<>();
        bCards.add(new BCard("Dharan", "Aditya", "dharan.aditya@gmail.com", "I love to code", "https://media.licdn.com/mpr/mprx/0_0k9zITCg3oGCNW1q9z2v8CHykwTx-sDZx8CUSqcg34GOBo8M1zaR_PfyiWwiN4OZUQCR_vupWVhYlOHknc1div3r2Vh0lOXZ1c19HNqjFRzPUH0XxQ5ZehV2u7Vf4OPWsNBJ76WAGNh", ""));
        bCards.add(new BCard("Dharan", "Aditya", "dharan.aditya@gmail.com", "I love to code", "https://media.licdn.com/mpr/mprx/0_0k9zITCg3oGCNW1q9z2v8CHykwTx-sDZx8CUSqcg34GOBo8M1zaR_PfyiWwiN4OZUQCR_vupWVhYlOHknc1div3r2Vh0lOXZ1c19HNqjFRzPUH0XxQ5ZehV2u7Vf4OPWsNBJ76WAGNh", ""));
        bCards.add(new BCard("Dharan", "Aditya", "dharan.aditya@gmail.com", "I love to code", "", ""));
        TestAdapter testAdapter = new TestAdapter(bCards, this);
        bcardsList.setAdapter(testAdapter);
    }

    public void requestAuthCode(View view) {
        Uri reqestCode = Uri.parse(LinkedInService.REQUEST_AUTH_CODE_URL)
                .buildUpon()
                .appendQueryParameter(LinkedInService.PARAM_RESPONSE_TYPE, getString(R.string.response_type))
                .appendQueryParameter(LinkedInService.PARAM_CLIENT_ID, getString(R.string.client_id))
                .appendQueryParameter(LinkedInService.PARAM_REDIRECT_URL, getString(R.string.redirect_url))
                .build();
        Intent openBrowser = new Intent(Intent.ACTION_VIEW, reqestCode);
        if (openBrowser.resolveActivity(getPackageManager()) != null) {
            startActivity(openBrowser);
        }
    }

    void getAccessToken(String code) {
        progressBar.setVisibility(View.VISIBLE);
        retrofit = new Retrofit.Builder()
                .baseUrl(LinkedInService.REQUEST_ACCESS_TOKEN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LinkedInService linkedInService = retrofit.create(LinkedInService.class);
        linkedInService.getAccessToken(getString(R.string.grant_type),
                code,
                getString(R.string.redirect_url),
                getString(R.string.client_id),
                getString(R.string.client_secret))
                .enqueue(new Callback<AccessCredentials>() {
                    @Override
                    public void onResponse(Call<AccessCredentials> call, Response<AccessCredentials> response) {
                        if (response.isSuccessful()) {
                            String token = response.body().getAccessToken();
                            fetchBCardData(token);
                            Log.d(TAG, "onResponse: Token -> " + token);
                        } else {
//                            TODO Handle
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessCredentials> call, Throwable t) {
                        progressBar.setVisibility(View.INVISIBLE);
                        invalidateIntent();
                        Log.d(TAG, "onFailure: " + call.toString());
//                        Todo Handle
                    }
                });
    }

    void fetchBCardData(String token) {
        retrofit = new Retrofit.Builder()
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
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.d(TAG, "onResponse: BCard -> " + response.body().toString());
                } else {
//                    TODO Handle
                }
                invalidateIntent();
            }

            @Override
            public void onFailure(Call<BCard> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                invalidateIntent();
                Log.d(TAG, "onFailure: " + call.toString());
//                TODO Handle
            }
        });
    }

    @NonNull
    private String getBearerToken(String token) {
        return "Bearer " + token;
    }


}
