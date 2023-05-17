package com.amsamu.mymedialists;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;

import com.amsamu.mymedialists.databinding.ActivityEntryDetailsBinding;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class EntryDetailsActivity extends AppCompatActivity {

    public ActivityEntryDetailsBinding binding;
    public int listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEntryDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d("EntryDetailsActivity", "created EntryDetailsActivity");
        listId = getIntent().getExtras().getInt("containerList");
        setUpTopBar();

        setUpDatePicker();
    }

    public void setUpTopBar() {
        binding.topAppBar.setNavigationOnClickListener(v -> returnToDisplayList());
        binding.topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_save) {
                insertToDB();
                returnToDisplayList();
            }
            return true;
        });
    }

    //View.OnFocusChangeListener focusChangeListener;

    public void setUpDatePicker() {
        //focusChangeListener = binding.editTextReleaseDate.getOnFocusChangeListener();
        MaterialDatePicker datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .build();

        binding.editTextReleaseDate.setOnClickListener(v -> {
            Log.d("EntryDetailsActivity", "clicked release date edit text");
            datePicker.show(getSupportFragmentManager(), "tag");
        });
        datePicker.addOnPositiveButtonClickListener(selection -> {
            String formattedDate = DateFormat.getDateFormat(getApplicationContext())
                    .format(new Date((long)selection));
            binding.editTextReleaseDate.setText(formattedDate);
        });
//        binding.editTextReleaseDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                focusChangeListener.onFocusChange(v, hasFocus);
//                if(hasFocus) {
//                    binding.editTextReleaseDate.performClick();
//                }
//            }
//        });
    }

    public void returnToDisplayList() {
        Intent intent = new Intent(this, DisplayListActivity.class);
        intent.putExtra("selectedList", listId);
        startActivity(intent);
        finish();
    }

    public void insertToDB() {

    }

}