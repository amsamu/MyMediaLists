package com.amsamu.mymedialists;

import static com.amsamu.mymedialists.util.SharedMethods.formatDate;
import static com.amsamu.mymedialists.util.SharedMethods.showInfoDialog;
import static com.amsamu.mymedialists.util.SharedMethods.simpleFormatter;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.amsamu.mymedialists.database.AppDatabase;
import com.amsamu.mymedialists.database.tables.Entry;
import com.amsamu.mymedialists.databinding.ActivityEntryDetailsBinding;
import com.amsamu.mymedialists.util.EntryStatus;
import com.amsamu.mymedialists.util.ToastManager;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class EntryDetailsActivity extends AppCompatActivity {

    AppDatabase db;
    public ActivityEntryDetailsBinding binding;
    public int listId;
    public int entryId;
    Entry entry;
    boolean isNewEntry = false;
    File coverImageFile = null;
    File tmpCoverImageFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getDatabase(getApplicationContext());
        binding = ActivityEntryDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d("EntryDetailsActivity", "created EntryDetailsActivity" + LocalDate.now().getDayOfMonth());
        listId = getIntent().getExtras().getInt("containerList");
        entryId = getIntent().getExtras().getInt("entry");
        Log.d("EntryDetailsActivity", "containerList: " + listId + ", entry: " + entryId);

        if (entryId == -1) {
            isNewEntry = true;
            entryId = db.entryDao().getHighestId() + 1;
            entry = new Entry(entryId, listId);
        } else {
            binding.topAppBar.getMenu().setGroupVisible(R.id.details_group_options, true);
            entry = db.entryDao().getEntry(entryId);
            binding.topAppBar.setTitle(R.string.entry_details);
            loadFields();
        }

        setUpTopBar();
        setUpImagePicker();
        setShowMore();
        setUpStartDatePicker();
        setUpFinishDatePicker();
    }

    public void loadFields() {
        // Load cover image (if one was set)
        if (entry.coverImage != null) {
            coverImageFile = new File(entry.coverImage);
            Log.d("EntryDetailsActivity", "loading cover image from: " + coverImageFile.getPath());
            binding.image.setImageURI(null);
            binding.image.setImageURI(Uri.fromFile(coverImageFile));
            binding.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        // Load entry name
        binding.editTextEntryName.setText(entry.name);
        // Load author
        binding.editTextEntryAuthor.setText(entry.author);
        // Load release year
        if (entry.releaseYear != null) {
            binding.editTextReleaseYear.setText(String.valueOf(entry.releaseYear));
        }
        // Load publisher
        binding.editTextEntryPublisher.setText(entry.publisher);
        // Load status
        binding.textViewStatus.setText(entry.status.getString(this), false);
        // Load start date
        binding.editTextStartDate.setText(formatDate(this, entry.startDate));
        // Load finish date
        binding.editTextFinishDate.setText(formatDate(this, entry.finishDate));
    }

    public void setUpTopBar() {
        binding.topAppBar.setNavigationOnClickListener(v -> finish());
        binding.topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_save) {
                clickedActionSave();
            } else if (item.getItemId() == R.id.action_delete_entry) {
                confirmDeleteEntry();
            }
            return true;
        });
    }

    public void clickedActionSave() {
        if (!binding.editTextEntryName.getText().toString().isBlank()) {
            saveEntry();
        } else {
            showInfoDialog(this, R.string.must_fill_entry_name);
        }
    }

    public void saveEntry() {
        saveFields();
        if (isNewEntry) {
            insertToDB();
        } else {
            updateInDB();
        }
        finish();
    }

    public void updateInDB() {
        db.entryDao().update(entry);
        ToastManager.showToast(this, R.string.entry_updated, Toast.LENGTH_SHORT);
    }

    public void insertToDB() {
        db.entryDao().insertAll(entry);
        ToastManager.showToast(this, R.string.entry_created, Toast.LENGTH_SHORT);
    }

    public void confirmDeleteEntry() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.question_delete_entry)
                .setMessage(R.string.message_delete_entry)
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                })
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    deleteEntry();
                    finish();
                })
                .show();
    }

    public void deleteEntry() {
        if (coverImageFile != null) {
            coverImageFile.delete();
        }
        db.entryDao().deleteAll(entry);
        ToastManager.showToast(this, R.string.entry_deleted, Toast.LENGTH_SHORT);
    }

    public void setShowMore() {
        binding.buttonShowMore.setOnClickListener(v -> {
            if (binding.entryHiddenFields.getVisibility() == View.VISIBLE) {
                // If they were visible, hide them and set arrow downwards
                binding.entryHiddenFields.setVisibility(View.GONE);
                binding.buttonShowMore.setBackgroundResource(R.drawable.baseline_keyboard_arrow_down_24);
            } else if (binding.entryHiddenFields.getVisibility() == View.GONE) {
                // If they were hidden, show them and set arrow upwards
                binding.entryHiddenFields.setVisibility(View.VISIBLE);
                binding.buttonShowMore.setBackgroundResource(R.drawable.baseline_keyboard_arrow_up_24);
            }
        });
    }

    public void setUpImagePicker() {
        ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        // The chosen image is saved to a tmp directory and displayed in the image view
                        tmpCoverImageFile = new File(getApplicationContext().getFilesDir(), "entries_images/tmp/" + entryId + "_" + LocalDateTime.now().format(simpleFormatter));
                        tmpCoverImageFile.getParentFile().getParentFile().mkdir();
                        tmpCoverImageFile.getParentFile().mkdir();
                        try (InputStream is = getApplicationContext().getContentResolver().openInputStream(result)) {
                            Log.d("EntryDetailsActivity", "is: " + result.getPath());
                            Files.copy(is, tmpCoverImageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            binding.image.setImageURI(null);
                            binding.image.setImageURI(Uri.fromFile(tmpCoverImageFile));
                            binding.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

        binding.image.setOnClickListener(v -> {
            activityResultLauncher.launch("image/*");
        });
    }


    //
    public void setUpStartDatePicker() {
        MaterialDatePicker datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText(R.string.select_date)
                        .build();

        binding.editTextStartDate.setOnClickListener(v -> {
            Log.d("EntryDetailsActivity", "clicked start date edit text");
            datePicker.show(getSupportFragmentManager(), "tag");
        });
        datePicker.addOnPositiveButtonClickListener(selection -> {
            entry.startDate = new Date((long) selection);
            binding.editTextStartDate.setText(formatDate(this, entry.startDate));
        });
    }

    //
    public void setUpFinishDatePicker() {
        MaterialDatePicker datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText(R.string.select_date)
                        .build();

        binding.editTextFinishDate.setOnClickListener(v -> {
            Log.d("EntryDetailsActivity", "clicked finish date edit text");
            datePicker.show(getSupportFragmentManager(), "tag");
        });
        datePicker.addOnPositiveButtonClickListener(selection -> {
            entry.finishDate = new Date((long) selection);
            binding.editTextFinishDate.setText(formatDate(this, entry.finishDate));
        });
    }

    public void saveFields() {
        // Save cover image
        saveCoverImage();
        // Save entry name
        entry.name = binding.editTextEntryName.getText().toString();
        // Save entry author
        entry.author = binding.editTextEntryAuthor.getText().toString();
        // Save release year
        saveReleaseYear();
        // Save publisher
        entry.publisher = binding.editTextEntryPublisher.getText().toString();
        // Save status
        entry.status = EntryStatus.fromString(this, binding.textViewStatus.getText().toString());
    }

    void saveReleaseYear() {
        String text = binding.editTextReleaseYear.getText().toString();
        if (text != null && !text.isBlank()) {
            entry.releaseYear = Integer.parseInt(text);
        }
    }

    private void saveCoverImage() {
        // Delete previous cover image from disk
        if (tmpCoverImageFile != null) {
            if (coverImageFile != null) {
                coverImageFile.delete();
            }
            // Set new cover image path
            coverImageFile = new File(getApplicationContext().getFilesDir(), "entries_images/" + entryId + "_" + LocalDateTime.now().format(simpleFormatter));
            coverImageFile.getParentFile().mkdir();

            // Move cover image from entries_images/tmp directory to entries_images
            try {
                Files.move(tmpCoverImageFile.toPath(), coverImageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                entry.coverImage = coverImageFile.getPath();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}