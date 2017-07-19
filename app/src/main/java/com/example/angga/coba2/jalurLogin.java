package com.example.angga.coba2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * A login screen that offers login via email/password.
 */
public class jalurLogin extends AppCompatActivity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jalur_login);

        //back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login/Daftar");

    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}

