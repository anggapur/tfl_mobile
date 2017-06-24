package com.example.angga.coba2;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class splashScreen extends AppCompatActivity {

    //Set waktu lama splashscreen
    private static int splashInterval = 2000;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        pb = (ProgressBar)findViewById(R.id.progressBar1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent i = new Intent(splashScreen.this, MainActivity.class);
                startActivity(i); // menghubungkan activity splashscren ke main activity dengan intent


                //jeda selesai Splashscreen
                this.finish();
            }

            private void finish()
            {

            }
        },splashInterval);
    }
}
