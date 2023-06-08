package com.amsamu.mymedialists;

import static com.amsamu.mymedialists.util.JsonOperations.readJsonFile;
import static com.amsamu.mymedialists.util.JsonOperations.writeJsonFile;
import static com.amsamu.mymedialists.util.SharedMethods.simpleFormatter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.amsamu.mymedialists.database.AppDatabase;
import com.amsamu.mymedialists.database.tables.Entry;
import com.amsamu.mymedialists.database.tables.MediaList;
import com.amsamu.mymedialists.databinding.ActivitySettingsBinding;
import com.amsamu.mymedialists.util.ToastManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;
    AppDatabase db;
    JsonObject dbJsonObj;
    ActivityResultLauncher<String> exportResultLauncher;
    ActivityResultLauncher<String[]> importResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getDatabase(getApplicationContext());
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpTopBar();
        setUpBackupOptions();
        exportResultLauncher = setExportResultLauncher();
        importResultLauncher = setImportResultLauncher();
    }

    public ActivityResultLauncher<String> setExportResultLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.CreateDocument("application/json"), result -> {
                    if (result != null) {
                        try (OutputStream os = getApplicationContext().getContentResolver().openOutputStream(result)) {
                            ;
                            writeJsonFile(os, dbJsonObj);
                            ToastManager.showToast(this, R.string.exported, Toast.LENGTH_SHORT);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    public ActivityResultLauncher<String[]> setImportResultLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.OpenDocument(), result -> {
                    if (result != null) {
                        try (InputStream is = getApplicationContext().getContentResolver().openInputStream(result)) {
                            confirmImport(readJsonFile(is));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    public void setUpTopBar() {
        binding.topAppBar.setNavigationOnClickListener(v -> finish());
    }

    public void setUpBackupOptions() {
        binding.settingsExport.setOnClickListener(v -> {
            exportDB();
        });
        binding.settingsImport.setOnClickListener(v -> {
            importResultLauncher.launch(new String[]{"application/json"});
        });
    }

    public void exportDB() {
        Gson gson = new Gson();
        dbJsonObj = new JsonObject();

        List<MediaList> mediaLists = db.mediaListDao().getAll();
        List<Entry> entries = db.entryDao().getAll();
        encodeImages(entries);

        JsonArray mListsObject = gson.fromJson(gson.toJson(mediaLists), JsonArray.class);
        JsonArray entriesObject = gson.fromJson(gson.toJson(entries), JsonArray.class);

        dbJsonObj.add("mediaLists", mListsObject);
        dbJsonObj.add("entries", entriesObject);
        Log.d("JSON", dbJsonObj.toString());

        exportResultLauncher.launch("mymedialists_data_" + LocalDateTime.now().format(simpleFormatter));
    }


    public void confirmImport(JsonObject importJsonObj) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.question_import_dabase)
                .setMessage(R.string.message_confirm_import)
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                })
                .setPositiveButton(R.string.do_import, (dialog, which) -> {
                    importDB(importJsonObj);
                })
                .show();
    }

    public void importDB(JsonObject importJsonObj) {
        Gson gson = new Gson();
        if (importJsonObj.has("mediaLists") && importJsonObj.has("entries")) {
            Log.d("JSON", importJsonObj.toString());
            MediaList[] mediaLists = gson.fromJson(importJsonObj.get("mediaLists"), new TypeToken<MediaList[]>() {
            }.getType());

            Entry[] entries = gson.fromJson(importJsonObj.get("entries"), new TypeToken<Entry[]>() {
            }.getType());
            if(decodeImages(entries)){
                newDBFromImport(mediaLists, entries);
            }
        } else {
            ToastManager.showToast(this, R.string.cant_import, Toast.LENGTH_SHORT);
        }
    }

    public void encodeImages(List<Entry> entryList) {
        for (Entry entry : entryList) {
            if (entry.coverImage != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(entry.coverImage);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bytes = baos.toByteArray();
                entry.coverImage = Base64.encodeToString(bytes, Base64.DEFAULT);
            }
        }
    }

    public boolean decodeImages(Entry[] entryList) {
        boolean success = false;
        try {
            for (Entry entry : entryList) {
                if (entry.coverImage != null) {
                    byte[] bytes = Base64.decode(entry.coverImage, Base64.DEFAULT);
                    File coverImageFile = new File(getApplicationContext().getFilesDir(), "entries_images/" + entry.id + "_" + LocalDateTime.now().format(simpleFormatter));
                    coverImageFile.getParentFile().mkdir();
                    Files.write(coverImageFile.toPath(), bytes);
                    entry.coverImage = coverImageFile.getPath();
                }
            }
            success = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return success;
    }

    public void newDBFromImport(MediaList[] mediaLists, Entry[] entries){
        db.mediaListDao().deleteEverything();
        db.entryDao().deleteEverything();

        db.mediaListDao().insertAll(mediaLists);
        db.entryDao().insertAll(entries);

        ToastManager.showToast(this, R.string.imported, Toast.LENGTH_SHORT);
    }


}