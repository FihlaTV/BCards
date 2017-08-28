package com.dharanaditya.bcards;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by dharan1011 on 27/8/17.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
