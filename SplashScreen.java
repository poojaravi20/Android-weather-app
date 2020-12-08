package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
//        Intent intent = new Intent(this, MainActivity.class);
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();

    }
}

