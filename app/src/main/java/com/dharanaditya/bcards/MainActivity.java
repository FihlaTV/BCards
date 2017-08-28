package com.dharanaditya.bcards;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dharanaditya.bcards.api.LinkedInService;
import com.dharanaditya.bcards.model.AccessCredentials;
import com.dharanaditya.bcards.model.BCard;
import com.dharanaditya.bcards.ui.BCardViewHolder;
import com.dharanaditya.bcards.ui.TestAdapter;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    // TODO Remove Log TAG Before open sourcing
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 3003;
    private static final int RC_GET_CODE = 7007;
    private DatabaseReference databaseReference;
    private Retrofit retrofit;
    private RecyclerView bcardsRecyclerView;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private FirebaseRecyclerAdapter<BCard, BCardViewHolder> bCardFirebaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUi();
        setSupportActionBar(toolbar);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            populateUi();
        } else {
            signIn();
        }
    }

    private void setupUi() {
        bcardsRecyclerView = (RecyclerView) findViewById(R.id.rv_bcards_list);
        bcardsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = (ProgressBar) findViewById(R.id.pb_loding);
        toolbar = (Toolbar) findViewById(R.id.tb_main_toolbar);

    }

    private void populateUi() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Toast.makeText(this, "Welcome " + firebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(firebaseUser.getUid());
        setupRecyclerViewAdapter(databaseReference);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GET_CODE) {
            if (resultCode == RESULT_OK) {
                handleIntent(data);
            }
        }
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                IdpResponse response = IdpResponse.fromResultIntent(data);
                populateUi();
            }
        }
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
                FirebaseAuth.getInstance().signOut();
                signIn();
                Log.d(TAG, "onOptionsItemSelected: Reached");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signIn() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                        ))
                        .setTheme(R.style.AuthTheme)
                        .build(), RC_SIGN_IN
        );
    }

    private void handleIntent(Intent intent) {
        if (intent.getData() != null && !intent.getData().getQueryParameter("code").isEmpty()) {
            String code = intent.getData().getQueryParameter("code");
            if (!code.isEmpty())
                addBCard(code);
            else
                showErrorDialog(MainActivity.this, "Cannot fetch Authentication Credentials", Integer.valueOf(null));
        }
    }

    private void addBCard(String code) {
        getAccessToken(code);
    }
    
    private void setupRecyclerViewAdapter(final DatabaseReference reference) {
        bCardFirebaseAdapter = new FirebaseRecyclerAdapter<BCard, BCardViewHolder>(
                BCard.class, R.layout.bcard_list_item, BCardViewHolder.class, reference
        ) {
            @Override
            protected void populateViewHolder(BCardViewHolder viewHolder, BCard model, int position) {
                viewHolder.bind(model.getFirstName(), model.getLastName(), model.getEmailAddress(), model.getHeadline(), model.getPictureUrl());
                viewHolder.itemView.setTag(position);
            }
        };

        bcardsRecyclerView.setAdapter(bCardFirebaseAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                bCardFirebaseAdapter.getRef(pos).removeValue();
            }
        }).attachToRecyclerView(bcardsRecyclerView);
    }

    public void requestAuthCode(View view) {
        if (NetworkUtils.isConnected(getApplicationContext())) {
            Uri requestUrl = Uri.parse(LinkedInService.REQUEST_AUTH_CODE_URL)
                    .buildUpon()
                    .appendQueryParameter(LinkedInService.PARAM_RESPONSE_TYPE, getString(R.string.response_type))
                    .appendQueryParameter(LinkedInService.PARAM_CLIENT_ID, getString(R.string.client_id))
                    .appendQueryParameter(LinkedInService.PARAM_REDIRECT_URL, getString(R.string.redirect_url))
                    .build();
//            TODO             Intent openBrowser = new Intent(Intent.ACTION_VIEW, requestUrl);
            Intent openBrowser = new Intent(MainActivity.this, WebViewActivity.class);
            openBrowser.setData(requestUrl);
            if (openBrowser.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(openBrowser, RC_GET_CODE);
            }
        } else {
            Toast.makeText(this, "Please Connect to the Internet", Toast.LENGTH_SHORT).show();
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
                        if (response.isSuccessful() && response.code() == 200) {
                            String token = response.body().getAccessToken();
                            fetchBCardData(token);
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            showErrorDialog(MainActivity.this, response.message(), response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessCredentials> call, Throwable t) {
                        progressBar.setVisibility(View.INVISIBLE);
                        showErrorDialog(MainActivity.this, "Make sure you have active internet service.", Integer.valueOf(null));
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
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    databaseReference.push().setValue(response.body());
                    // TODO notify User
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    showErrorDialog(MainActivity.this, response.message(), response.code());
                }
            }

            @Override
            public void onFailure(Call<BCard> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                showErrorDialog(MainActivity.this, "Make sure you have active internet service.", Integer.valueOf(null));
            }
        });
    }

    private void showErrorDialog(Context context, String message, int code) {
        if (message.isEmpty())
            message = "Sorry for inconvenience caused.\nContact our support team.";
        new AlertDialog.Builder(context)
                .setTitle("Error " + Integer.toString(code))
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .show();
    }

    @NonNull
    private String getBearerToken(String token) {
        return "Bearer " + token;
    }

    void testRecyclerView() {
        List<BCard> bCards = new ArrayList<>();
        bCards.add(new BCard("Dharan", "Aditya", "dharan.aditya@gmail.com", "I love to code", "https://media.licdn.com/mpr/mprx/0_0k9zITCg3oGCNW1q9z2v8CHykwTx-sDZx8CUSqcg34GOBo8M1zaR_PfyiWwiN4OZUQCR_vupWVhYlOHknc1div3r2Vh0lOXZ1c19HNqjFRzPUH0XxQ5ZehV2u7Vf4OPWsNBJ76WAGNh", ""));
        bCards.add(new BCard("Dharan", "Aditya", "dharan.aditya@gmail.com", "I love to code", "https://media.licdn.com/mpr/mprx/0_0k9zITCg3oGCNW1q9z2v8CHykwTx-sDZx8CUSqcg34GOBo8M1zaR_PfyiWwiN4OZUQCR_vupWVhYlOHknc1div3r2Vh0lOXZ1c19HNqjFRzPUH0XxQ5ZehV2u7Vf4OPWsNBJ76WAGNh", ""));
        bCards.add(new BCard("Dharan", "Aditya", "dharan.aditya@gmail.com", "I love to code", "", ""));
        TestAdapter testAdapter = new TestAdapter(bCards, this);
        bcardsRecyclerView.setAdapter(testAdapter);
    }


}
