package com.example.picoplacaapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Log.e("LOCKB", "InterruptedException Error");
            Thread.currentThread().interrupt();
        }
        goToActivity();
        finish();
    }

    private void goToActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

}

