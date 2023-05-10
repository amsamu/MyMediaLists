package com.amsamu.mymedialists;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.amsamu.mymedialists.databinding.ActivityMainBinding;

public class NotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}