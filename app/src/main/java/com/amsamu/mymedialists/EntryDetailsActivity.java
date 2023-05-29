package com.amsamu.mymedialists;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;

import com.amsamu.mymedialists.dao.TitleDao;
import com.amsamu.mymedialists.data.Title;
import com.amsamu.mymedialists.databinding.ActivityEntryDetailsBinding;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

        Log.d("EntryDetailsActivity", "created EntryDetailsActivity" + LocalDate.now().getDayOfMonth());
        listId = getIntent().getExtras().getInt("containerList");
        setUpTopBar();
        setUpImagePicker();
        setUpDatePicker();
        setShowMore();
    }

    public void setShowMore(){
        binding.buttonShowMore.setOnClickListener(v -> {
            if(binding.fieldLayoutReleaseDate.getVisibility()==View.VISIBLE){
                binding.fieldLayoutReleaseDate.setVisibility(View.GONE);
            }else if(binding.fieldLayoutReleaseDate.getVisibility()==View.GONE){
                binding.fieldLayoutReleaseDate.setVisibility(View.VISIBLE);
            }
        });
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

    public void setUpImagePicker() {
        File outputFile = new File(getApplicationContext().getFilesDir(), "aaa");
        binding.image.setImageURI(Uri.fromFile(outputFile));
        ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        try (InputStream is = getApplicationContext().getContentResolver().openInputStream(result)) {
                            Log.d("EntryDetailsActivity", "is: " + result.getPath());
                            Files.copy(is, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            binding.image.setImageURI(null);
                            binding.image.setImageURI(Uri.fromFile(outputFile));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

        binding.image.setOnClickListener(v -> {
            activityResultLauncher.launch("image/*");
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
                    .format(new Date((long) selection));
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
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        TitleDao titleDao = db.titleDao();

        Title title = new Title(titleDao.getHighestId() + 1, binding.editTextTitle.getText().toString());
        titleDao.insertAll(title);
    }

}