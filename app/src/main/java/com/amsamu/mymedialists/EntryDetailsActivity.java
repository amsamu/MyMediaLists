package com.amsamu.mymedialists;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.amsamu.mymedialists.databinding.ActivityEntryDetailsBinding;

public class EntryDetailsActivity extends AppCompatActivity {

    public ActivityEntryDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEntryDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d("EntryDetailsActivity","created EntryDetailsActivity");

        setUpActionBar();
    }

    public void setUpActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show nav menu button
    }
}